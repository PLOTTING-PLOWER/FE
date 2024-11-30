package com.example.plotting_fe.myplogging.ui

import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.response.MonthResponse
import java.util.Calendar

class MonthlyPloggingViewBinder(private val includeView: View) {

    private val tvCount: TextView = includeView.findViewById(R.id.tv_count)
    private val tvMonthTime: TextView = includeView.findViewById(R.id.tv_month_time)

    fun bind(data: List<MonthResponse.MonthData>) {
        // 로그 출력
        Log.d("MonthData", "Received data: $data")

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1

        // 현재 월 데이터 찾기
        val currentMonthData = data.find { it.year == currentYear && it.month == currentMonth }

        // UI 업데이트
        if (currentMonthData != null) {
            tvCount.text = "${currentMonthData.participationCount}"
            tvMonthTime.text = "${currentMonthData.totalHour / 60}"
        } else {
            // 현재 월 데이터가 없는 경우 기본값 설정
            tvCount.text = "0"
            tvMonthTime.text = "0"
        }
    }

}