package com.example.projekuas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.projekuas.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup navigation
        with(binding) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            bottomNavigationView.setupWithNavController(navController)
        }
    }
}