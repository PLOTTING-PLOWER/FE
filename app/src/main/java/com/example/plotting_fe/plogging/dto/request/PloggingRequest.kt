package com.example.plotting_fe.plogging.dto.request

import com.example.plotting_fe.plogging.dto.PloggingType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class PloggingRequest(
    var title: String,
    var content: String,
    var maxPeople: Long,
    var type: PloggingType,
    var recruitStartDate: LocalDate,
    var recruitEndDate: LocalDate,
    var startTime: LocalDateTime,
    var spendTime: Long,
    var startLocation: String,
    var endLocation: String
)