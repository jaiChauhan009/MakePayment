package com.example.makepayment.android.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.room.Room
import com.example.makepayment.android.CreateUser
import com.example.makepayment.android.MainActivity
import com.example.makepayment.android.database.MyDatabase
import com.example.makepayment.android.database.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.io.File
import java.io.InputStream

@Composable
fun UserDataUploadScreen(context: Activity) {
    var email by  remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var ifsc by remember { mutableStateOf("") }
    var addharNo by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var userName by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedFileUri = uri
    }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationStatus by remember { mutableStateOf("") }
    val myDatabase: MyDatabase = Room.databaseBuilder(context,
        MyDatabase::class.java,
        "my_database")
        .build()

    AuthenticationManager.initializeFirebase()
    AuthManager.initializeFirebase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
            Button(
                onClick = {
                    AuthenticationManager.SendOTP(
                        phoneNumber = phoneNumber,
                        activity = context,
                        callbacks =object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                AuthenticationManager.signInWithPhoneAuthCredential(credential,context)
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                verificationStatus = "Verification failed: ${e.message}"
                            }

                            override fun onCodeSent(vId: String, token: PhoneAuthProvider.ForceResendingToken) {
                                verificationStatus = "OTP sent successfully"
                                verificationId = vId
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send OTP")
            }
        }
        Row {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    val credential = PhoneAuthProvider.getCredential(verificationId,otp)
                    AuthenticationManager.signInWithPhoneAuthCredential(credential,context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify OTP")
            }
        }
        if (verificationStatus.isNotEmpty()) {
            Text(
                text = verificationStatus,
                color = if(verificationStatus.startsWith("OTP verification failed")) Color.Red else Color.Green,
                modifier = Modifier.padding(top = 16.dp))
        }
        if(!verificationStatus.startsWith("OTP verification failed")){
            OutlinedTextField(value = userName,
                onValueChange = { userName = it },
                label = { Text(text = "userName")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text(text = "password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = amount,
                onValueChange = { amount = it },
                label = { Text(text = "Amount you are Saving")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = ifsc,
                onValueChange = { ifsc = it },
                label = { Text(text = "IFSC")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = addharNo,
                onValueChange = { addharNo = it },
                label = { Text(text = "Addhar No")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                launcher.launch("*/*")
            },modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Select File")
            }
            selectedFileUri?.let{
                Text("Selected File: ${File(it.path!!).name}",fontSize = 16.sp)
            }
            val inputStream: InputStream? = selectedFileUri?.let {
                context.contentResolver.openInputStream(
                    it
                )
            }
            inputStream?.use { input ->
                selectedFileByteArray = input.readBytes()
            }
            Button(onClick = {
                if(selectedFileUri != null){
                    val user = User(
                        phoneNo = phoneNumber,
                        email = email,
                        ifsc = ifsc,
                        currentAmount = amount.toInt(),
                        addharNo = addharNo,
                        selectedFile = selectedFileByteArray,
                        userName = userName
                    )
                    saveUserToDatabase(user,myDatabase)
                    AuthManager.signUp(email,password,context)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("userName", userName)
                    intent.putExtra("phoneNumber", phoneNumber)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Create")
            }
        }
    }
}


@Composable
fun UserLogin(activity: Activity){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp),
        contentAlignment = Alignment.Center){
        Column {
            AuthManager.initializeFirebase()
            var userName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            OutlinedTextField(value = userName,
                onValueChange = { userName = it },
                label = { Text(text = "UserName")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text(text = "email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Button(onClick = {
                AuthManager.Login(email,password,activity)
            }) {
                val intent = Intent(activity,MainActivity::class.java)
                intent.putExtra("userName",userName)
                activity.startActivity(intent)
                Text(text = "Login")
            }
        }
    }
}

