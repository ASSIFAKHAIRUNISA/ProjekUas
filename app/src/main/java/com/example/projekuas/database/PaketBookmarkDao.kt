package com.example.projekuas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PaketBookmarkDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insert(paketBookmark: PaketBookmark)

    @Update
    fun update(paketBookmark: PaketBookmark)

    @Query("DELETE FROM paket_bookmark_table")
    fun truncateTable()

    @Query("SELECT * FROM paket_bookmark_table")
    fun allPostsLocal(): LiveData<List<PaketBookmark>>

    @Query("SELECT * FROM paket_bookmark_table ORDER BY id DESC")
    fun allLocal(): List<PaketBookmark>

    @Query("SELECT * FROM paket_bookmark_table WHERE paketId = :id")
    fun getPaketById(id: String?): PaketBookmark

    @Query("DELETE FROM paket_bookmark_table WHERE paketId = :id")
    fun delete(id: String?)
}