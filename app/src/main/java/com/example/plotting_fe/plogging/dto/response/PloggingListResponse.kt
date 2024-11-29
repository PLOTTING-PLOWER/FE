package com.example.plotting_fe.plogging.dto.response

import com.example.plotting_fe.plogging.dto.PloggingType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class PloggingListResponse(
    val currentPeople: Long,
    val ploggingResponseList: List<PloggingResponse>
)

