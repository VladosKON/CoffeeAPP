package com.example.coffeeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Запросы на работу со Странами
@Dao
interface TagDao {

    @Query("SELECT * FROM tag_table WHERE note_id = :note_id")
    fun getTagListByNoteId(note_id: Int): Flow<List<Tag>>

    @Query("SELECT * FROM tag_table")
    fun getTagList(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag)

    @Update
    suspend fun update(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)
}