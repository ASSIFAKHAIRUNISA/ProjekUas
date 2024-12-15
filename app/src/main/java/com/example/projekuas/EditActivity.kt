package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityEditBinding
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var gallery: Gallery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        val paketId = intent.getStringExtra("ID_PAKET")
        if (paketId != null) {
            getGalleryById(paketId)
        }

        binding.btnSimpan.setOnClickListener {
            val judul = binding.etJudul.text.toString()
            val tema = binding.etTema.text.toString()
            val tahun = binding.etTahun.text.toString()
            val pelukis = binding.etPelukis.text.toString()
            val gambarUrl = binding.etGambarUrl.text.toString()
            val detail = binding.etdetail.text.toString()

            if (gallery != null) {
                val updatedGallery = Gallery(
                    id = gallery?.id,
                    judul = judul,
                    tema = tema,
                    tahun = tahun,
                    pelukis = pelukis,
                    gambar_url = gambarUrl,
                    detail = detail
                )
                updateGallery(updatedGallery)
            } else {
                Toast.makeText(this, "ID Paket tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGalleryById(id: String) {
        val call = ApiClient.api.getGalleryById(id)
        call.enqueue(object : Callback<Gallery> {
            override fun onResponse(call: Call<Gallery>, response: Response<Gallery>) {
                if (response.isSuccessful) {
                    gallery = response.body()
                    gallery?.let {
                        binding.etJudul.setText(it.judul)
                        binding.etTema.setText(it.tema)
                        binding.etTahun.setText(it.tahun)
                        binding.etPelukis.setText(it.pelukis)
                        binding.etGambarUrl.setText(it.gambar_url)
                        binding.etdetail.setText(it.detail)
                    }
                } else {
                    Toast.makeText(this@EditActivity, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Gallery>, t: Throwable) {
                Toast.makeText(this@EditActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateGallery(gallery: Gallery) {
        val id = gallery.id
        if (id.isNullOrEmpty()) {
            Toast.makeText(this@EditActivity, "ID gallery tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val call = ApiClient.api.updateGallery(id, gallery)
        call.enqueue(object : Callback<Gallery> {
            override fun onResponse(call: Call<Gallery>, response: Response<Gallery>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditActivity, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("ID_PAKET", gallery.id)  // Kirim ID untuk mengupdate data di aktivitas sebelumnya
                    setResult(RESULT_OK, intent)
                    finish()  // Kembali ke AdminActivity setelah data berhasil diperbarui
                } else {
                    Toast.makeText(this@EditActivity, "Gagal memperbarui data. Status: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Gallery>, t: Throwable) {
                Toast.makeText(this@EditActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


