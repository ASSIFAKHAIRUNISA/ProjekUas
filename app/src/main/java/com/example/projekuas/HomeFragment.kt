package com.example.projekuas

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekuas.database.Local
import com.example.projekuas.database.LocalDao
import com.example.projekuas.database.LocalRoomDatabase
import com.example.projekuas.databinding.FragmentHomeBinding
import com.example.projekuas.model.Gallery
import com.example.projekuas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: PaketUserAdapter
    private var itemList: ArrayList<Gallery> = ArrayList()
    private lateinit var recyclerViewItem: RecyclerView

    // Room Database
    private lateinit var mLocalDao: LocalDao
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        executorService = Executors.newSingleThreadExecutor()
        val db = LocalRoomDatabase.getDatabase(requireContext())
        if (db != null) {
            mLocalDao = db.localDao()!!
        }

        if (isInternetAvailable(requireActivity())) {
            fetchData()
            Toast.makeText(requireActivity(), "Establishing Connection", Toast.LENGTH_SHORT).show()
        } else {
            fetchDataOffline()
            Toast.makeText(requireActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        recyclerViewItem = binding.recyclerView
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 1)

        itemAdapter = PaketUserAdapter(itemList, requireContext())
        recyclerViewItem.adapter = itemAdapter
    }

    private fun truncateTable() {
        executorService.execute { mLocalDao.truncateTable() }
    }

    private fun insert(local: Local) {
        executorService.execute { mLocalDao.insert(local) }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun fetchData() {
        ApiClient.api.getGallery().enqueue(object : Callback<List<Gallery>> {
            override fun onResponse(call: Call<List<Gallery>>, response: Response<List<Gallery>>) {
                if (response.isSuccessful) {
                    itemList.clear()
                    response.body()?.let { paketList ->
                        if (paketList.isEmpty()) {
                            Toast.makeText(requireActivity(), "Tidak ada gallery tersedia", Toast.LENGTH_SHORT).show()
                            return
                        }

                        itemList.addAll(paketList)
                        truncateTable()
                        paketList.forEach { paket ->
                            val local = Local(
                                id = 0,
                                judul = paket.judul,
                                tema = paket.tema,
                                tahun = paket.tahun,
                                pelukis = paket.pelukis,
                                detail = paket.detail,
                                gambar_url = paket.gambar_url
                            )
                            insert(local)
                        }
                    }
                    itemAdapter.notifyDataSetChanged()
                } else {
                    Log.e("FetchData", "Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Gallery>>, t: Throwable) {
                Log.e("FetchData", "Error: ${t.message}")
            }
        })
    }


    private fun fetchDataOffline() {
        executorService.execute {
            val localData = mLocalDao.allLocal() // Mengambil data menggunakan allLocal()
            requireActivity().runOnUiThread {
                itemList.clear()
                itemList.addAll(localData.map { paket ->
                    Gallery(
                        id = paket.id.toString(),
                        judul = paket.judul,
                        tema = paket.tema,
                        tahun = paket.tahun,
                        pelukis = paket.pelukis,
                        detail = paket.detail ?: "",
                        gambar_url = paket.gambar_url
                    )
                })
                itemAdapter.notifyDataSetChanged()
            }
        }
    }
}