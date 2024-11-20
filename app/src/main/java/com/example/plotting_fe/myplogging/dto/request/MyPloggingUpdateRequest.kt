package com.example.plotting_fe.myplogging.dto.request

data class MyPloggingUpdateRequest(
    val title: String,
    val content: String,
    val maxPeople: Long,
    val recruitStartTime: String,
    val recruitEndTime: String,
    val startTime: String,
    var spendTime: Long
)
