package com.example.makepayment.android.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.makepayment.android.database.Friend
import com.example.makepayment.android.database.MyDatabase
import com.example.makepayment.android.database.Payment
import com.example.makepayment.android.utils.SendMessageScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(name: String, phoneNo: String){
    val myDatabase = Room.databaseBuilder(LocalContext.current,
        MyDatabase::class.java,
        "my_database")
        .build()
    val friend by remember { myDatabase.friendDao().getAllFriends().friend}
    val payments by myDatabase.paymentDao().getAllPayments().observeAsState(emptyList())
    var text by remember { mutableStateOf(TextFieldValue()) }
    var receiver by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray,
                tonalElevation = 5.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = { Text("Search...") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            myDatabase.friendDao().getAllFriends().friend[name]?.find {
                                it.first.substring(0, text.text.length) == text.text
                            }
                        },
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text("Search")
                    }
                }
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                OutlinedTextField(value = receiver ,
                    label = { Text("Receiver") },
                    onValueChange = {receiver = it} )
                OutlinedTextField(value = amount ,
                    label = { Text("Amount") },
                    onValueChange = {amount = it} )
                val userMessage = "$amount sent to $receiver from $name at ${LocalDateTime.now()}"
                val receiverMessage = "$amount received from $name at ${LocalDateTime.now()}"
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        myDatabase.paymentDao().insert(
                            Payment(
                                transitionId = Pair(phoneNo.toLong(),receiver.toLong()),
                                transitionUser = Pair(name,name),
                                amount = amount.toInt(),
                                timeStamps = LocalDateTime.now().toString()
                            )
                        )
                        val existingFriend = friend.find { it.first == receiver }
                    if(existingFriend!= null) {
                        val updatedFriends = existingFriend.copy(existingFriend.second.plus(Pair(receiver,receiver)))
                        myDatabase.friendDao().updateFriend(
                            Friend(
                                friend = mutableMapOf(name to listOf(updatedFriends) )
                            )
                        )
                    }else{
                        myDatabase.friendDao().insert(
                            Friend(
                                friend = mutableMapOf(name to listOf(Pair(receiver,receiver) ))
                            )
                        )
                    } }
                }) {
                    Text(text = "Transfer Money")
                }
                SendMessageScreen(message = userMessage, phoneNumber = phoneNo)
                SendMessageScreen(message = receiverMessage, phoneNumber = receiver )
            }
            LazyRow(
                modifier = Modifier.padding(8.dp)
            ) {
                items(friend){
                    FriendItem(friend = it)
                }
            }
            if (payments.isEmpty()) {
                Text("No payments found")
            } else {
                payments.takeLast(6).forEach { payment ->
                    Text("Payment ID: ${payment.id} - Amount: ${payment.amount}")
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}



@Composable
fun FriendItem(friend: Pair<String,String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .background(color = Color.Blue, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = friend.first,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = friend.second,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}