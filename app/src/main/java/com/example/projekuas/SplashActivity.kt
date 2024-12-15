package com.example.projekuas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.projekuas.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_DISPLAY_LENGTH = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogo.animate().setDuration(3000).alpha(1f).withEndAction() {
            // intent untuk berpindah ke activity GetStartedActivity
            val mainIntent = Intent(this@SplashActivity, GetStartedActivity::class.java)
            // memulai activity GetStartedActivity
            startActivity(mainIntent)
            // menutup activity splash, jadi pengguna tidak dapat kembali ke halaman ini dgn menekan tombol kembali
            finish()
        }
    }
}