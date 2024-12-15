package com.example.projekuas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekuas.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fungsi onClick untuk tombol login
        binding.btnLogin.setOnClickListener {
            //intent untuk memulai aktivitas login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}