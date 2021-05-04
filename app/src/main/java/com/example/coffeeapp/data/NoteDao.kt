package com.example.coffeeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Запросы на сортировку и поиск
@Dao
interface NoteDao {

    fun getNotes(searchQuery: String, sortOrder: SortOrder): Flow<List<Note>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getNoteListByLastDate(searchQuery)
            SortOrder.BY_TAGS -> getNoteListByTags(searchQuery)
            SortOrder.BY_ACID -> getNoteListByAcid(searchQuery)
            SortOrder.BY_SWEET -> getNoteListBySweet(searchQuery)
            SortOrder.BY_BALANCE -> getNoteListByBalance(searchQuery)
        }

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY lastDate DESC")
    fun getNoteListByLastDate(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE id IN (SELECT note_id FROM tag_table WHERE NAME LIKE '%' || :searchQuery || '%')")
    fun getNoteListByTags(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY acidSB DESC")
    fun getNoteListByAcid(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY sweetSB DESC")
    fun getNoteListBySweet(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY balanceSB DESC")
    fun getNoteListByBalance(searchQuery: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}