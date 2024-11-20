package com.example.plotting_fe.myplogging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.databinding.ActivityMyPloggingUpdate2Binding

class MyPloggingUpdate2Activity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingUpdate2Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingUpdate2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}