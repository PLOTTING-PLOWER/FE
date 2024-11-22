package com.example.plotting_fe.myplogging.dto.request

data class MyPloggingUpdateRequest(
    val title: String,
    val content: String,
    val maxPeople: Long,
    val recruitStartDate: String,
    val recruitEndDate: String,
    val startTime: String,
    var spendTime: Long
)
