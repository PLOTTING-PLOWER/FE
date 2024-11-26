package com.example.plotting_fe.mypage.dto

import com.example.plotting_fe.plogging.dto.PloggingType
import java.time.LocalDate
import java.time.LocalDateTime

data class Plogging(
    val ploggingId: Long,
    val title: String,
    val currentPeople:Long,
    val maxPeople:Long,
    val ploggingType: PloggingType,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation:String
)
