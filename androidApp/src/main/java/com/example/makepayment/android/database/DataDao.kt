package com.example.makepayment.android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface PaymentData {
    @Query("SELECT * FROM payment ORDER BY id DESC")
    fun getAllPayments(): LiveData<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(payment: Payment)
}

@Dao
interface FriendData {
    @Query("SELECT * FROM friend ORDER BY id DESC")
    fun getAllFriends():Friend

    @Update
    fun updateFriend(friend: Friend)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(friend: Friend)
}

@Dao
interface UserData {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE phoneNo = :phoneNo")
    fun getUser(phoneNo: String): User?
}