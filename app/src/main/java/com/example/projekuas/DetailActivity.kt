package com.example.projekuas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.example.projekuas.databinding.ActivityDetailBinding
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @UnstableApi
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali untuk menutup aktivitas ini
        binding.btnBack.setOnClickListener {
            finish() // Menggunakan onBackPressed untuk kembali ke aktivitas sebelumnya
        }

        // Mengambil data dari intent
        val id = intent.getStringExtra("id") ?: "ID Tidak Tersedia"
        val judul = intent.getStringExtra("judul") ?: "Judul Tidak Tersedia"
        val tema = intent.getStringExtra("tema") ?: "Tema Tidak Tersedia"
        val tahun = intent.getStringExtra("tahun") ?: "Tahun Tidak Tersedia"
        val pelukis = intent.getStringExtra("pelukis") ?: "Pelukis Tidak Tersedia"
        val detail = intent.getStringExtra("detail") ?: "Detail Tidak Tersedia"
        val gambarUrl = intent.getStringExtra("gambar_url") ?: ""

        // Log untuk debugging
        Log.d("DetailActivity", "ID: $id, Nama: $judul, Deskripsi: $tema, Lokasi: $tahun")

        // Validasi data gallery
        if (judul == "Judul Tidak Tersedia") {
            Toast.makeText(this, "Data gallery tidak lengkap!", Toast.LENGTH_SHORT).show()
        }

        // Menampilkan data pada UI
        binding.judulDetail.text = judul
        binding.temaDetail.text = tema
        binding.pelukisDetail.text = pelukis
        binding.tahunDetail.text = tahun
        binding.detailDetail.text = detail

        // Jika URL gambar ada, muat gambar menggunakan Glide
        if (gambarUrl.isNotEmpty()) {
            Glide.with(this).load(gambarUrl).into(binding.imageDetail)
        }

        // Mengambil data gallery dari API jika ada ID paket
        val paketId = intent.getStringExtra("ID_PAKET")
        if (paketId != null) {
            getGalleryDetail(paketId)
        } else {
            Toast.makeText(this, "Paket tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGalleryDetail(id: String) {
        val call = ApiClient.api.getGalleryById(id)
        call.enqueue(object : Callback<Gallery> {
            override fun onResponse(call: Call<Gallery>, response: Response<Gallery>) {
                if (response.isSuccessful) {
                    val paket = response.body()
                    paket?.let {
                        // Update UI dengan data dari paket
                        binding.judulDetail.text = it.judul
                        binding.temaDetail.text = it.tema
                        binding.tahunDetail.text = it.tahun
                        binding.pelukisDetail.text = it.pelukis
                        binding.detailDetail.text = it.detail
                        Glide.with(this@DetailActivity).load(it.gambar_url).into(binding.imageDetail)
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data gallery", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Gallery>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





