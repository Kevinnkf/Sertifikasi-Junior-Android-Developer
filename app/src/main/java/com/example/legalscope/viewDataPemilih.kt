package com.example.legalscope

 import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
 import androidx.recyclerview.widget.LinearLayoutManager
 import com.example.legalscope.databinding.ActivityViewDataPemilihBinding

class viewDataPemilih : AppCompatActivity() {

    private lateinit var binding: ActivityViewDataPemilihBinding
    private lateinit var db: PemilihDBHelper
    private lateinit var adapter: viewPemilihAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewDataPemilihBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = PemilihDBHelper(this)
        adapter = viewPemilihAdapter(db.viewPemilih(),this)
        binding.dataRecycleView.layoutManager = LinearLayoutManager(this)
        binding.dataRecycleView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(db.viewPemilih())
    }
}

