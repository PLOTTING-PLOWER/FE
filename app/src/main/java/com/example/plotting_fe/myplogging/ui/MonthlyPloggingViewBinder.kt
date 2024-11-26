package com.example.plotting_fe.myplogging.ui

import android.view.View
import android.widget.TextView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.response.MonthResponse

class MonthlyPloggingViewBinder(private val includeView: View) {

    private val tvCount: TextView = includeView.findViewById(R.id.tv_count)
    private val tvMonthTime: TextView = includeView.findViewById(R.id.tv_month_time)

    fun bind(data: MonthResponse.MonthData) {
        tvCount.text = "${data.participationCount}"
        tvMonthTime.text = "${data.totalHour}"
    }
}