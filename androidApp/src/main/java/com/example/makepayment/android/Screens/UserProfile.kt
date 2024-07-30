package com.example.makepayment.android.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.makepayment.android.database.MyDatabase
import com.example.makepayment.android.database.User
import kotlinx.coroutines.launch


@Composable
fun Profile(phoneNo:String){
    Box(modifier = Modifier.fillMaxSize()){
        val myDatabase = Room.databaseBuilder(
            LocalContext.current,
            MyDatabase::class.java,
            "my_database")
            .build()
        val coroutineScope = rememberCoroutineScope()

        var user by remember {
            mutableStateOf<User?>(null)
        }
        LaunchedEffect(phoneNo) {
            coroutineScope.launch {
                user = myDatabase.userDao().getUser(phoneNo)
            }
        }
        Column {
            UserDetailsCard(phoneNo = phoneNo)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }){
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Icon(imageVector = Icons.Default.List, contentDescription = null )
                    Text(text = "See transaction history")
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }){
                Row (horizontalArrangement = Arrangement.SpaceEvenly){
                    Icon(imageVector = Icons.Default.Email, contentDescription = null )
                    Text(text = "Check Bank Balance")
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null ,
                    modifier = Modifier.clickable {  })
                Icon(imageVector = Icons.Default.Build, contentDescription = null ,
                    modifier = Modifier.clickable {  })
                Text(text = "Term & Conditions", modifier = Modifier.clickable {  })
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null ,
                    modifier = Modifier.clickable {  })
            }
        }
    }
}


@Composable
fun UserDetailsCard(phoneNo: String) {
    val myDatabase = Room.databaseBuilder(
        LocalContext.current,
        MyDatabase::class.java,
        "my_database")
        .build()
    val coroutineScope = rememberCoroutineScope()

    var user by remember {
        mutableStateOf<User?>(null)
    }
    LaunchedEffect(phoneNo) {
        coroutineScope.launch {
            user = myDatabase.userDao().getUser(phoneNo)
        }
    }

    var isFront by remember { mutableStateOf(true) }

    // Front card content
    val frontContent = @Composable {
        Column {
            Text("User Details", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Text("User Name: ${user?.userName}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)
            Text("Phone Number: ${user?.phoneNo}")
            Text("Email: ${user?.email}")

            // Click to flip to back side
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { isFront = !isFront }) {
                Text("Show More Details")
            }
        }
    }

    val backContent = @Composable {
        Column {
            Text("Additional Details", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Text("IFSC Code: ${user?.ifsc}")
            Text("Current Amount: ${user?.currentAmount}")
            Text("Aadhar Number: ${user?.addharNo}")

            // Click to flip back to front
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { isFront = !isFront }) {
                Text("Back to User Details")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        AnimatedVisibility(
            visible = isFront,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                frontContent()
            }
        }

        AnimatedVisibility(
            visible = !isFront,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                backContent()
            }
        }
    }
}
