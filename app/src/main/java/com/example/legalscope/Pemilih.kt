package com.example.legalscope

import android.net.Uri

data class Pemilih(
    val id: Int,
    val nik: String,
    val nama: String,
    val noHp: String,
    val jenisKelamin: String,
    val tanggal: String,
    val alamat: String,
    val gambar: Uri
)
