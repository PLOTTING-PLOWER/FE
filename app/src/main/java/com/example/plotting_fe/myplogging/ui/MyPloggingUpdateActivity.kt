package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.databinding.ActivityMyPloggingUpdateBinding

class MyPloggingUpdateActivity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, MyPloggingUpdate2Activity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}