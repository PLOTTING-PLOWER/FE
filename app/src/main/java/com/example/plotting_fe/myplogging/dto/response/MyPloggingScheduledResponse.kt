package com.example.plotting_fe.myplogging.dto.response

import com.example.plotting_fe.plogging.dto.PloggingType

data class MyPloggingScheduledResponse(
    val ploggingId: Long,
    val ploggingType: PloggingType,
    val title: String,
    val startTime: String,
    val spendTime: Long,
    val currentPeople: Long,
    val maxPeople: Long,
    val startLocation: String,
    val isAssigned : Boolean,
) {
}