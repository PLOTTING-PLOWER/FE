package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.R
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.home.ui.MainFragment

class PloggingMapActivity : AppCompatActivity() {

    private lateinit var home : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plogging_map)

        home  = findViewById(R.id.home)

        // PloggingMapFragment를 추가
        val mapFragment = PloggingMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_container, mapFragment)
            .commit()

        // 뒤로 가기 버튼 이벤트 연결
        ClickUtil.onBackButtonClick(this, home)
    }
}
