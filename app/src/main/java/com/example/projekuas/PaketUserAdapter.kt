package com.example.projekuas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projekuas.database.LocalRoomDatabase
import com.example.projekuas.database.PaketBookmark
import com.example.projekuas.databinding.ItemGalleryBinding
import com.example.projekuas.model.Gallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaketUserAdapter(
    private val paketUserList: List<Gallery>,
    private val context: Context
) : RecyclerView.Adapter<PaketUserAdapter.PaketUserViewHolder>() {

    inner class PaketUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.judul)
        val tema: TextView = itemView.findViewById(R.id.tema)
        val tahun: TextView = itemView.findViewById(R.id.tahun)
        val pelukis: TextView = itemView.findViewById(R.id.pelukis)
        val gambar: ImageView = itemView.findViewById(R.id.gambarUrl)
        val btnBookmark: ImageButton = itemView.findViewById(R.id.btnBookmark)
        // Tidak perlu tombol Edit dan Delete untuk User
        val btnEditItem: ImageButton = itemView.findViewById(R.id.btnEditItem)
        val btnDeleteItem: ImageButton = itemView.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaketUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return PaketUserViewHolder(itemView)
    }

    // Menghubungkan data dengan ViewHolder
    override fun onBindViewHolder(holder: PaketUserViewHolder, position: Int) {
        val currentItem = paketUserList[position]

        holder.judul.text = currentItem.judul
        holder.tema.text = currentItem.tema
        holder.tahun.text = currentItem.tahun
        holder.pelukis.text = currentItem.pelukis
        // Load gambar dengan Glide
        Glide.with(context)
            .load(currentItem.gambar_url)
            .into(holder.gambar)

        // Menangani klik pada item untuk membuka DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("id", currentItem.id ?: "")
                putExtra("judul", currentItem.judul ?: "Tidak Ada Judul")
                putExtra("tema", currentItem.tema ?: "Tidak Ada Tema")
                putExtra("tahun", currentItem.tahun ?: "Tidak Ada Tahun")
                putExtra("pelukis", currentItem.pelukis ?: "Tidak Ada Pelukis")
                putExtra("detail", currentItem.detail ?: "Tidak Ada Detail")
                putExtra("gambar_url", currentItem.gambar_url ?: "")
            }
            context.startActivity(intent)
        }


        // Menyembunyikan tombol Edit dan Delete untuk user
        holder.btnEditItem.visibility = View.GONE
        holder.btnDeleteItem.visibility = View.GONE

        // Memeriksa apakah gallery sudah ada di bookmark
        isBookmark(currentItem.id) { isBookmarkk ->
            holder.btnBookmark.setImageResource(
                if (isBookmarkk) R.drawable.baseline_bookmark
                else R.drawable.baseline_bookmark_border_24
            )
        }

        // Menangani klik tombol favorit
        holder.btnBookmark.setOnClickListener {
            toggleBookmark(currentItem) { isBookmarkk ->
                holder.btnBookmark.setImageResource(
                    if (isBookmarkk) R.drawable.baseline_bookmark
                    else R.drawable.baseline_bookmark_border_24
                )
            }
        }
    }

    // Mendapatkan jumlah item dalam RecyclerView
    override fun getItemCount(): Int = paketUserList.size

    // Fungsi untuk memeriksa apakah gallery sudah ada di favorit
    private fun isBookmark(id: String?, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
            val isBookmarkk = db?.getPaketById(id) != null
            withContext(Dispatchers.Main) {
                callback(isBookmarkk)
            }
        }
    }

    // Fungsi untuk menambah atau menghapus gallery dari favorit
    private fun toggleBookmark(paket: Gallery, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = LocalRoomDatabase.getDatabase(context)?.paketBookmarkDao()
            val paketBookmark = PaketBookmark(
                paketId = paket.id.toString()
            )

            val isBookmarkk = db?.getPaketById(paket.id) != null
            if (isBookmarkk) {
                db?.delete(paket.id)
            } else {
                db?.insert(paketBookmark)
            }
            withContext(Dispatchers.Main) {
                callback(!isBookmarkk)
            }
        }
    }
}
