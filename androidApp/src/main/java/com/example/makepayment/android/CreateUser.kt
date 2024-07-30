package com.example.makepayment.android

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.makepayment.android.utils.UserDataUploadScreen
import com.example.makepayment.android.utils.UserLogin

class CreateUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                var visible by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxSize()){
                    Column {
                        if(visible){
                            UserDataUploadScreen(context = this@CreateUser)
                        }else{
                            UserLogin(activity = this@CreateUser)
                        }
                        Row {
                            Button(onClick = { visible= true }) {
                                Text(text = "SingUp")
                            }
                            Button(onClick = { visible= false }) {
                                Text(text = "Login")
                            }
                        }
                    }
                }
            }
        }
    }
}


