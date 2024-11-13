package com.example.plotting_fe.myplogging.dto

import com.example.plotting_fe.plogging.dto.PloggingType
import com.example.plotting_fe.plogging.dto.Reply

data class PloggingData(
    val id: Long,
    val type: PloggingType,
    val startTime: String,
    val title: String,
    val location: String,
    val duration: Long,
    val currentPeople: Long,
    val maxPeople: Long
) {
}