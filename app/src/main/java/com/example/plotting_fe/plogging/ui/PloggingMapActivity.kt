package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.R
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

        // 홈으로 이동
        home.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            // MainFragment로 전환
            transaction.replace(R.id.fragment_container, MainFragment()) // R.id.fragment_container는 프래그먼트를 표시할 컨테이너 ID
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
