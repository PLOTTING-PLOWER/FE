package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.plotting_fe.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PloggingDetailActivity : AppCompatActivity() {
    private lateinit var ploggingId: String  // fixme : 홈에서 참여하기 누르면 넘어가기 위해서 1) ploggingId 추가함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plogging_detail)

        ploggingId = intent.getStringExtra("ploggingId") ?: ""  // fixme : 홈에서 참여하기 누르면 넘어가기 위해서 1) ploggingId 추가함

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)


        val adapter: PloggingViewPagerAdapter = PloggingViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.setText("상세정보")
                1 -> tab.setText("댓글")
            }
        }.attach()

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}