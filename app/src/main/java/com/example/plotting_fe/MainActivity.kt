package com.example.plotting_fe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity
import com.example.plotting_fe.plogging.ui.PloggingMapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.a_activity_main)

        // 시스템 바 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).post {
            val navController = findNavController(R.id.nav_host_fragment)
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

            // 기본 네비게이션 설정
            NavigationUI.setupWithNavController(bottomNavigationView, navController)

            // BottomNavigationView 클릭 이벤트 추가
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_map -> {
                        // PloggingMapActivity로 이동
                        startActivity(Intent(this, PloggingMapActivity::class.java))
                        true
                    }
                    R.id.navigation_steps -> {
                        // MyPloggingHomeActivity로 이동
                        startActivity(Intent(this, MyPloggingHomeActivity::class.java))
                        true
                    }
                    else -> {
                        // 기본 네비게이션 처리
                        NavigationUI.onNavDestinationSelected(menuItem, navController)
                        true
                    }
                }
            }
        }
    }
}
