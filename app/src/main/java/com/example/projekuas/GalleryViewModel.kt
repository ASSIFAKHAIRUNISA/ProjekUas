package com.example.projekuas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryViewModel : ViewModel() {

    private val _gallery = MutableLiveData<List<Gallery>>()
    val gallery: LiveData<List<Gallery>> get() = _gallery

    // Fungsi untuk mengambil data dari API
    fun fetchGallery() {
        viewModelScope.launch {
            ApiClient.api.getGallery().enqueue(object : Callback<List<Gallery>> {
                override fun onResponse(call: Call<List<Gallery>>, response: Response<List<Gallery>>) {
                    if (response.isSuccessful) {
                        _gallery.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<Gallery>>, t: Throwable) {
                    // Handle error
                }
            })
        }
    }
}
