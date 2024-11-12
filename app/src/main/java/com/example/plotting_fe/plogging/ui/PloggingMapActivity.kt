package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.R

class PloggingMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plogging_map)

        // PloggingMapFragment를 추가
        val mapFragment = PloggingMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_container, mapFragment)
            .commit()
    }
}
