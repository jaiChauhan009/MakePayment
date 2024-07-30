package com.example.makepayment.android.utils


import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.core.content.ContextCompat
import com.example.makepayment.android.database.MyDatabase
import com.example.makepayment.android.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SendMessageScreen(message: String, phoneNumber: String) {
    val context = LocalContext.current
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                sendMessage(context, phoneNumber, message)
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
        ) {
        sendMessage(context, phoneNumber, message)
        } else {
        requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
    }
}



fun sendMessage(context: Context, phoneNumber: String, message: String) {
    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        // Handle exceptions (e.g., SMS sending failed)
        Toast.makeText(context, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}


object AuthenticationManager {
    private lateinit var auth: FirebaseAuth

    fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    fun SendOTP(phoneNumber: String, activity: Activity, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,activity: Activity) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity){ task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "OTP verified successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "OTP verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


object AuthManager {

    private lateinit var auth: FirebaseAuth

    fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    fun Login(email: String, password: String,activity: Activity) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signUp(email: String, password: String,activity: Activity) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Sign up successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

fun saveUserToDatabase(user: User, myDatabase: MyDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        myDatabase.userDao().insert(user)
    }
}
