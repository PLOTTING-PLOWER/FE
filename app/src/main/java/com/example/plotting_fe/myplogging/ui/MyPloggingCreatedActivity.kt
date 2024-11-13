package com.example.plotting_fe.myplogging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingCreatedBinding
import com.example.plotting_fe.myplogging.dto.PloggingData
import com.example.plotting_fe.plogging.dto.PloggingType

class MyPloggingCreatedActivity : AppCompatActivity() {

    private var _binding: ActivityMyPloggingCreatedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingCreatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        val recyclerView = binding.rvPlogging
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 설정
        val dummyData = listOf(
            PloggingData(2, PloggingType.ASSIGN, "2024.12.13 10:00", "여의도 봄꽃 플로깅", "여의도, 서울", 1, 2, 10),
            PloggingData(3, PloggingType.DIRECT, "2024.12.24 17:00", "한강공원 플로깅", "한강, 서울", 3, 5, 5))

        val adapter = MyPloggingCreatedAdapter(dummyData)
        recyclerView.adapter = adapter

        // RecyclerView 설정
        val completeRecyclerView = binding.rvCompletePlogging
        completeRecyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 설정
        val completeDummyData = listOf(
            PloggingData(5, PloggingType.ASSIGN, "2024.11.13 10:00", "여의도 봄꽃 플로깅", "여의도, 서울", 1, 2, 10),
            PloggingData(6, PloggingType.DIRECT, "2024.11.24 17:00", "한강공원 플로깅", "한강, 서울", 3, 5, 5))

        val completeAdapter = MyCompletePloggingCreatedAdapter(completeDummyData)
        completeRecyclerView.adapter = completeAdapter
    }
}