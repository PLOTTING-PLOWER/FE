package com.example.plotting_fe

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.home.ui.Alarm
import com.example.plotting_fe.home.ui.AlarmAdapter

class AlarmActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alarm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_alarm)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val alarmList = listOf(
            Alarm("뉴진스 하니님 이 상도동 한바퀴 플로깅에 참여를 신청했습니다.","2024.10.03"),
            Alarm("뉴진스 해린님 이 상도동 한바퀴 플로깅에 참여를 신청했습니다.","2024.11.29"),
            Alarm("뉴진스 다니엘님 이 상도동 한바퀴 플로깅에 참여를 신청했습니다.","2024.11.29")
        )

        val adapter = AlarmAdapter(alarmList)
        recyclerView.adapter = adapter

    }
}