package com.example.legalscope

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.legalscope.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();

        binding = ActivityMainBinding.inflate(layoutInflater);

        setContentView(binding.root);

        binding.addButton.setOnClickListener(){
            val intent = Intent(this, Add::class.java);
            startActivity(intent);
        }

        binding.accessListData.setOnClickListener(){
            val intent = Intent(this, viewDataPemilih:: class.java)
            startActivity(intent);
        }

        binding.accessInformasi.setOnClickListener(){
            val intent = Intent(this, Informasi:: class.java)
            startActivity(intent);
        }

        binding.keluar.setOnClickListener(){
            finish();
        }
    }
}