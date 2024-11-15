package com.example.plotting_fe.myplogging.ui

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter : ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        return "${barEntry?.y?.toInt()} 시간" // Y값에 "시간"을 추가하여 반환
    }
}
