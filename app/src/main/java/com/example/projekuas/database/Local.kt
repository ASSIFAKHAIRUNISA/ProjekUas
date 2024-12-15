package com.example.projekuas.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_table")
data class Local(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "judul")
    val judul: String,

    @ColumnInfo(name = "tema")
    val tema: String,

    @ColumnInfo(name = "pelukis")
    val pelukis: String,

    @ColumnInfo(name = "tahun")
    val tahun: String,

    @ColumnInfo(name = "gambar_url")
    val gambar_url: String,

    @ColumnInfo(name = "detail")
    val detail: String?,
)