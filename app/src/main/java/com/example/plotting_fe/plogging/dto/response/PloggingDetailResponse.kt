package com.example.plotting_fe.plogging.dto.response

import com.example.plotting_fe.plogging.dto.PloggingType

data class PloggingDetailResponse(
    val creator: String,
    val title: String,
    val content: String,
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingType: PloggingType,
    val recruitStartDate: String,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation: String,
    val endLocation: String,
)
