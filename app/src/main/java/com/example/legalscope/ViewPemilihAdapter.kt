package com.example.legalscope

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class viewPemilihAdapter(private var listPemilih: List<Pemilih>, private val context: Context) :
    RecyclerView.Adapter<viewPemilihAdapter.PemilihViewHolder>() {

    class PemilihViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaView: TextView = itemView.findViewById(R.id.namaTextView)
        val nikView: TextView = itemView.findViewById(R.id.nikTextView)
        val noHpView: TextView = itemView.findViewById(R.id.noHpTextView)
        val jkView: TextView = itemView.findViewById(R.id.jenisKelaminTextView)
        val tglView: TextView = itemView.findViewById(R.id.tanggalTextView)
        val alamatView: TextView = itemView.findViewById(R.id.alamatTextView)
        val imgView: ImageView = itemView.findViewById(R.id.gambarImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PemilihViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pemilih, parent, false)
        return PemilihViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPemilih.size
    }

    override fun onBindViewHolder(holder: PemilihViewHolder, position: Int) {
        val pemilih = listPemilih[position]

        // Handle image with Glide
        Glide.with(holder.itemView.context)
            .load(pemilih.gambar) // Load the image URI
            .into(holder.imgView) // Display it in the ImageView

        holder.namaView.text = pemilih.nama
        holder.nikView.text = pemilih.nik
        holder.noHpView.text = pemilih.noHp
        holder.jkView.text = pemilih.jenisKelamin
        holder.tglView.text = pemilih.tanggal
        holder.alamatView.text = pemilih.alamat


    }

    fun refreshData(newPemilih: List<Pemilih>) {
        listPemilih = newPemilih
        notifyDataSetChanged()
    }
}
