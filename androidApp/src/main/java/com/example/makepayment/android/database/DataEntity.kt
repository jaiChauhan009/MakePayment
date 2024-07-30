package com.example.makepayment.android.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PairStringConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromPair(pair: Pair<String, String>?): String? {
        return gson.toJson(pair)
    }

    @TypeConverter
    fun toPair(value: String?): Pair<String, String>? {
        val type = object : TypeToken<Pair<String, String>>() {}.type
        return gson.fromJson(value, type)
    }
}
class PairLongConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromPair(pair: Pair<Long, Long>?): String? {
        return gson.toJson(pair)
    }

    @TypeConverter
    fun toPair(value: String?): Pair<Long, Long>? {
        val type = object : TypeToken<Pair<Long, Long>>() {}.type
        return gson.fromJson(value, type)
    }
}

class PairConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: MutableMap<String?, List<Pair<String, String>>>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(json: String): MutableMap<String?, List<Pair<String, String>>> {
        val mapType = object : TypeToken<MutableMap<String?, List<Pair<String, String>>>>() {}.type
        return gson.fromJson(json, mapType)
    }
}

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phoneNo: String,
    val email: String,
    val ifsc: String,
    val currentAmount:Int,
    val addharNo: String,
    val selectedFile: ByteArray?,
    val userName: String
)

@TypeConverters(PairStringConverter::class, PairLongConverter::class)
@Entity(tableName ="Payment")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transitionId: Pair<Long,Long>,
    val transitionUser: Pair<String?, String>,
    val amount:Int,
    val timeStamps:String
)

@TypeConverters(PairConverter::class)
@Entity(tableName ="Friend")
data class Friend(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var friend: MutableMap<String?, List<Pair<String, String>>>
)