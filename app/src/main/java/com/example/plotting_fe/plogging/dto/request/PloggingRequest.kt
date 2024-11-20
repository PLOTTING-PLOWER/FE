package com.example.plotting_fe.plogging.dto.request

import com.example.plotting_fe.plogging.dto.PloggingType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class PloggingRequest(
    var title: String,
    var content: String,
    var maxPeople: Long,
    var type: PloggingType,
    var recruitStartDate: LocalDate,
    var recruitEndDate: LocalDate,
    var startTime: LocalTime,
    var spendTime: Long,
    var startLocation: String,
    var endLocation: String
)