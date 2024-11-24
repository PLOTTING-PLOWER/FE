package com.example.plotting_fe.plogging.dto.response

import com.example.plotting_fe.plogging.dto.PloggingType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class PloggingResponse(
    val ploggingId: Long,
    val title: String,
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingType: PloggingType,
    val recruitEndDate: LocalDate,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val startTime: LocalDateTime,
    val spendTime: Long,
    val startLocation: String,
)