package com.example.plotting_fe.plogging.dto.response

data class PloggingResponse(
    val ploggingId: Long,
    val title: String,
    val maxPeople: Long,
    val ploggingType: String,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation: String,
)