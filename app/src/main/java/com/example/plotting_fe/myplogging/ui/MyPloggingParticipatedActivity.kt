package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R

class MyPloggingParticipatedActivity : AppCompatActivity() {

    private lateinit var recyclerViewParticipatedPlogging: RecyclerView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplogging_participated)

        // UI 요소 초기화
        recyclerViewParticipatedPlogging = findViewById(R.id.recyclerView_participated_plogging)
        backButton = findViewById(R.id.iv_back)

        // 뒤로가기 버튼 클릭 이벤트 설정
        backButton.setOnClickListener {
            val intent = Intent(this, MyPloggingHomeActivity::class.java)
            startActivity(intent) // MyPloggingHomeActivity로 이동
            finish() // 현재 Activity 종료
        }

        // RecyclerView 초기화
        recyclerViewParticipatedPlogging.layoutManager = LinearLayoutManager(this)
    }
}
