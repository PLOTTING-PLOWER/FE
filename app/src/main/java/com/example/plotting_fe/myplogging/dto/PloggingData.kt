package com.example.plotting_fe.myplogging.dto

import com.example.plotting_fe.plogging.dto.PloggingType

data class PloggingData(
    val ploggingId: Long,
    val ploggingType: PloggingType,
    val title: String,
    val content: String,
    val startTime: String,
    val spendTime: Long,
    val currentPeople: Long,
    val maxPeople: Long,
    val startLocation: String,
    val recruitStartDate: String,
    val recruitEndDate: String,
    val isRecruiting: Boolean,
    val isStar: Boolean
) {
}