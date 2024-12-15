package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menggunakan Shared Preferences untuk cek status login
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val role = sharedPref.getString("role", null)

        if (isLoggedIn) {
            // Periksa role user
            when (role) {
                "admin" -> {
                    // Pindah ke AdminActivity
                    startActivity(Intent(this, AdminActivity::class.java))
                }
                "user" -> {
                    // Pindah ke UserActivity
                    startActivity(Intent(this, UserActivity::class.java))
                }
            }
        } else {
            // Jika belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Tutup MainActivity setelah berpindah
        finish()
    }
}