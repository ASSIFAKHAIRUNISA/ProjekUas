
package com.example.projekuas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projekuas.databinding.ItemGalleryBinding
import com.example.projekuas.model.Gallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryAdapter(
    private var galleryList: List<Gallery>,
    private val isAdmin: Boolean,
    private val onEditClick: (Gallery) -> Unit,
    private val onDeleteClick: (Gallery) -> Unit,
    private val onCardClick: (Gallery) -> Unit // Listener baru untuk klik card
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gallery: Gallery) {
            with(binding) {
                // Set data ke tampilan
                judul.text = gallery.judul
                tema.text = gallery.tema
                tahun.text = gallery.tahun
                pelukis.text = gallery.pelukis

                // Load gambar dengan Glide
                Glide.with(root.context)
                    .load(gallery.gambar_url)
                    .into(gambarUrl)

                // Konfigurasi tombol Edit dan Delete
                if (isAdmin) {
                    btnEditItem.visibility = View.VISIBLE
                    btnDeleteItem.visibility = View.VISIBLE
                    btnBookmark.visibility = View.GONE
                    btnEditItem.setOnClickListener { onEditClick(gallery) }
                    btnDeleteItem.setOnClickListener { onDeleteClick(gallery) }
                } else {
                    btnEditItem.visibility = View.GONE
                    btnDeleteItem.visibility = View.GONE
                    btnBookmark.visibility = View.VISIBLE
                }

                // Konfigurasi klik card untuk membuka DetailActivity
                root.setOnClickListener {
                    onCardClick(gallery) // Memanggil listener untuk klik card
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(galleryList[position])
    }

    override fun getItemCount(): Int = galleryList.size

    // Update data jika perlu (misalnya setelah menghapus atau menambah data)
    fun updateData(newList: List<Gallery>) {
        galleryList = newList
        notifyDataSetChanged() // Perbarui data di adapter
    }
}
