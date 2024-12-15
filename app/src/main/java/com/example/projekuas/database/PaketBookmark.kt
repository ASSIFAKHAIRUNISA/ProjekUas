package com.example.projekuas.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paket_bookmark_table")
data class PaketBookmark (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "paketId")
    val paketId: String
)