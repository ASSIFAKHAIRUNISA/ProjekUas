package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projekuas.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(this)

        // Handle Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Login Button Listener
        binding.btnLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                when {
                    username == "admin" && password == "2703" -> {
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        prefManager.saveRole("admin")
                        navigateToRolePage("admin")
                    }
                    username == "assifa" && password == "12345" -> {
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        prefManager.saveRole("user")
                        navigateToRolePage("user")
                    }
                    else -> {
                        Toast.makeText(this, "Login gagal, periksa username/password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Harap isi username dan password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk navigasi sesuai role
    private fun navigateToRolePage(role: String) {
        val intent = when (role) {
            "admin" -> Intent(this, AdminActivity::class.java)
            "user" -> Intent(this, UserActivity::class.java)
            else -> null
        }
        intent?.let {
            startActivity(it)
            finish() // Tutup LoginActivity setelah navigasi
        }
    }
}