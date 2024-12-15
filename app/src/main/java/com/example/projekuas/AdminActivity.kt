package com.example.projekuas

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projekuas.databinding.ActivityAdminBinding
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var galleryAdapter: GalleryAdapter
    private var galleryList = mutableListOf<Gallery>()

    // Define ActivityResultLaunchers for Add and Edit Activities
    private lateinit var editActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and set up the adapter
        initializeRecyclerView()

        // Initialize ActivityResultLaunchers
        initializeActivityResultLaunchers()

        // Check if there is an internet connection, then fetch data
        checkInternetAndFetchData()

        // Setup listeners for FAB button (to add a new Gallery)
        setupListeners()
    }

    // Initializes RecyclerView and sets up the adapter
    private fun initializeRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2) // Grid dengan 2 kolom
        galleryAdapter = GalleryAdapter(
            galleryList,
            isAdmin = true,
            onEditClick = { paket ->
                paket.id?.let { id ->
                    // Create an intent to pass the ID for editing
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra("ID_PAKET", id)
                    // Launch EditActivity using ActivityResultLauncher
                    editActivityResultLauncher.launch(intent)
                }
            },
            onDeleteClick = { paket ->
                paket.id?.let { id ->
                    deleteGallery(id)
                }
            },
            onCardClick = { paket ->
                paket.id?.let { id ->
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("ID_PAKET", id)
                    startActivity(intent)
                }
            }
        )
        binding.recyclerView.adapter = galleryAdapter
    }

    // Initializes the ActivityResultLaunchers for Add and Edit Activities
    private fun initializeActivityResultLaunchers() {
        // Launch EditActivity and handle the result
        editActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh data after editing
                fetchGalleryFromServer()
            }
        }

        // Launch AddActivity and handle the result
        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh data after adding a new Gallery
                fetchGalleryFromServer()
            }
        }
    }

    // Checks if the device is connected to the internet and fetches data
    private fun checkInternetAndFetchData() {
        if (isInternetAvailable()) {
            fetchGalleryFromServer()
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetches the list of Gallery from the server using Retrofit
    private fun fetchGalleryFromServer() {
        ApiClient.api.getGallery().enqueue(object : Callback<List<Gallery>> {
            override fun onResponse(call: Call<List<Gallery>>, response: Response<List<Gallery>>) {
                if (response.isSuccessful) {
                    val gallery = response.body() ?: emptyList()
                    galleryList.clear()
                    galleryList.addAll(gallery)
                    galleryAdapter.notifyDataSetChanged() // Notify adapter that data has been updated
                } else {
                    Toast.makeText(this@AdminActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Gallery>>, t: Throwable) {
                Toast.makeText(this@AdminActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Deletes the Gallery using Retrofit
    private fun deleteGallery(id: String) {
        ApiClient.api.deleteGallery(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminActivity, "Gallery successfully deleted", Toast.LENGTH_SHORT).show()
                    fetchGalleryFromServer() // Refresh data after deletion
                } else {
                    Toast.makeText(this@AdminActivity, "Failed to delete Gallery", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Sets up listeners, such as clicking on the FAB to add a new Gallery
    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            // Launch AddActivity using ActivityResultLauncher
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        binding.btnLogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Checks if the internet is available on the device
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
