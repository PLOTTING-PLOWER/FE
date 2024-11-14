package com.example.plotting_fe.myplogging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingWaitingBinding
import com.example.plotting_fe.myplogging.dto.WaitingPeople

class MyPloggingWaitingActivity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingWaitingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        val recyclerView = binding.rvPlogging
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 설정
        val dummyData = listOf(
            WaitingPeople(1, "슝슝이", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
            WaitingPeople(2, "피치", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
            WaitingPeople(3, "버즈", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
        )

        val adapter = MyPloggingWaitingAdapter(dummyData)
        recyclerView.adapter = adapter
    }
}