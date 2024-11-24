package com.example.plotting_fe.plogging.ui

import com.example.plotting_fe.plogging.dto.PloggingType

data class PloggingStarResponse(
    val ploggingId: Long,
    val title: String,
    val currentPeople :Long,
    val maxPeople: Long,
    val ploggingType: PloggingType,
    val recruitEndDate : String,
    val startTime: String,
    val spendTime: Long,
    val startLocation : String
)
