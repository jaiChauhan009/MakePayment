package com.example.makepayment.android

import android.app.Application
import com.example.makepayment.android.database.MyDatabase

class MyApplication : Application() {
    val database: MyDatabase by lazy {
        MyDatabase.getDatabase(this)
    }
}