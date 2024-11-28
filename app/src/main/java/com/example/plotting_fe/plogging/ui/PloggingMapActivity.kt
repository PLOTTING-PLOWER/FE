package com.example.plotting_fe.plogging.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity

class PloggingMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plogging_map)

        val search: TextView = findViewById(R.id.search_text)
        val filter : ImageView = findViewById(R.id.filter)

        filter.setOnClickListener {
            val intent = Intent(this, GetPloggings::class.java)
            startActivity(intent)
        }

        search.setOnClickListener {
            val intent = Intent(this, GetPloggings::class.java)
            startActivity(intent)
        }

        // PloggingMapFragment를 추가
        val mapFragment = PloggingMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_container, mapFragment)
            .commit()
    }
}
