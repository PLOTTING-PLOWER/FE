package com.example.plotting_fe.utils

import android.content.Intent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity
import com.example.plotting_fe.plogging.ui.PloggingMapActivity

object UIUtils {

    // Edge-to-Edge 모드 설정 (static으로 변경)
    @JvmStatic
    fun enableEdgeToEdgeMode(activity: ComponentActivity) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    // 시스템 바 인셋 처리 (static)
    @JvmStatic
    fun handleSystemBarInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 네비게이션 설정
    @JvmStatic
    fun setupBottomNavigation(activity: ComponentActivity) {
        val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_map -> {
                    // PloggingMapActivity로 이동
                    activity.startActivity(Intent(activity, PloggingMapActivity::class.java))
                    true
                }
                R.id.navigation_steps -> {
                    // MyPloggingHomeActivity로 이동
                    activity.startActivity(Intent(activity, MyPloggingHomeActivity::class.java))
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
