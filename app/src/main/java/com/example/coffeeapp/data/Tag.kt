package com.example.coffeeapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Класс сущности Стран
@Entity(tableName = "tag_table", foreignKeys = [ForeignKey(entity = Note::class, parentColumns = ["id"], childColumns = ["note_id"], onDelete = CASCADE)])
@Parcelize
data class Tag (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    var note_id: Int = 0
) : Parcelable {
}