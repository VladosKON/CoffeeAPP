package com.example.coffeeapp.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

//Сущность записи о кофе в DB
@Entity(tableName = "note_table")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val title: String = "",
    val roastDate: String = "",
    val roastInfo: String = "",
    val dryAr: String = "",
    val dryArSB: Int = 1,
    val wetAr: String = "",
    val wetArSB: Int = 1,
    val sweet: String = "",
    val sweetSB: Int = 1,
    val acid: String = "",
    val acidSB: Int = 1,
    val body: String = "",
    val bodySB: Int = 1,
    val balanceSB: Int = 1,
    val flavour: String = "",
    val lastDate: Long = System.currentTimeMillis()
) : Parcelable {
    val createdDateFormatted: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat(
            "dd MMMM yyyy",
            Locale.getDefault()
        ).format(Date(lastDate))
}