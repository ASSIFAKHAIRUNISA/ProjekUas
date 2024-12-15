package com.example.projekuas.model

import com.google.gson.annotations.SerializedName

data class Gallery(
    @SerializedName("_id")
    val id: String?,  // ID bisa nullable saat create, tapi pastikan valid saat update dan delete

    @SerializedName("judul")
    val judul: String,

    @SerializedName("tema")
    val tema: String,

    @SerializedName("pelukis")
    val pelukis: String,

    @SerializedName("tahun")
    val tahun: String,

    @SerializedName("gambar_url")
    val gambar_url: String,

    @SerializedName("detail")
    val detail: String,
)
