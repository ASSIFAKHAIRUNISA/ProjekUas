package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityAddBinding
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Ketika tombol simpan ditekan
        binding.btnSimpan.setOnClickListener {
            val judul = binding.etJudul.text.toString()
            val tema = binding.etTema.text.toString()
            val pelukis = binding.etPelukis.text.toString()
            val tahun = binding.etTahun.text.toString()
            val gambarUrl = binding.etGambarUrl.text.toString()
            val detail = binding.etdetail.text.toString()

            // Cek jika semua kolom diisi
            if (judul.isNotEmpty() && tema.isNotEmpty() && tahun.isNotEmpty() && pelukis.isNotEmpty() && gambarUrl.isNotEmpty() && detail.isNotEmpty()) {
                val gallery = Gallery(
                    id = null,
                    judul = judul,
                    tema = tema,
                    pelukis = pelukis,
                    tahun = tahun,
                    gambar_url = gambarUrl,
                    detail = detail
                )

                // Kirim data ke API
                tambahGallery(gallery)
            } else {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tambahGallery(gallery: Gallery) {
        val call = ApiClient.api.createGallery(gallery)

        call.enqueue(object : Callback<Gallery> {
            override fun onResponse(call: Call<Gallery>, response: Response<Gallery>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddActivity, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("ID_PAKET", response.body()?.id)  // Kirim ID baru jika perlu
                    setResult(RESULT_OK, intent)
                    finish()  // Kembali ke Activity sebelumnya setelah data berhasil ditambahkan
                } else {
                    Toast.makeText(this@AddActivity, "Gagal menambahkan data. Status: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Gallery>, t: Throwable) {
                Toast.makeText(this@AddActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
