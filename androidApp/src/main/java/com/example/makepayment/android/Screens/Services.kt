package com.example.makepayment.android.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.makepayment.android.R

data class Services(
    val title:String,
    val Image:Int,
)

val serviceList = listOf<Services>(
    Services("Mobile Recharge",R.drawable.mobilerecharge),
    Services("Contact Pay",R.drawable.contactpay),
    Services("Scanner",R.drawable.scanner),
    Services("Service Pay",R.drawable.servicepay),
    Services("Bill pay",R.drawable.billpay),
    Services("More",R.drawable.download)
)
val serviceList2 = listOf(
    Services("Offer", R.drawable.offer),
    Services("Reward",R.drawable.images),
    Services("Referral",R.drawable.refferal),
)

@Composable
fun Service(){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Services", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(serviceList){
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp).clickable {

                        }){
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
                            Image(painter = painterResource(id = it.Image) ,
                                contentDescription = null )
                        }
                        Text(text = it.title)
                    }
                }
            }
        Text(text = "For User")
        LazyRow {
            items(serviceList2){
                FriendItem(friend = it)
            }
        }
    }
}


@Composable
fun FriendItem(friend:Services ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp).clickable {

                }
                .background(color = Color.Blue, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(painter = painterResource(id = friend.Image) , contentDescription = null )
        }
        Text(text = friend.title)
    }
}