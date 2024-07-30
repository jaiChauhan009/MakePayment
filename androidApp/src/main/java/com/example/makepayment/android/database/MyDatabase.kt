package com.example.makepayment.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Payment::class,Friend::class,User::class],version = 1,exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun paymentDao():PaymentData
    abstract fun friendDao():FriendData
    abstract fun userDao():UserData

    companion object{
        @Volatile
        private var INSTANCE: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

