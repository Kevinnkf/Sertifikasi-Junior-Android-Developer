package com.example.legalscope

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.legalscope.databinding.ActivityAddBinding
import com.example.legalscope.databinding.ActivityInformasiBinding

class Informasi : AppCompatActivity() {

    private lateinit var binding: ActivityInformasiBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInformasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}