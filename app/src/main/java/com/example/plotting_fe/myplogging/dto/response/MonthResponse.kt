package com.example.plotting_fe.myplogging.dto.response

data class MonthResponse(
    val responses: List<MonthData>
) {
    data class MonthData(
        val year: Int,
        val month: Int,
        val participationCount: Long,
        val totalHour: Long
    )
}
