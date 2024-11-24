package com.example.plotting_fe.plogging.dto.request

import com.example.plotting_fe.plogging.dto.PloggingType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class PloggingRequest(
    var title: String,
    var content: String,
    var maxPeople: Long,
    var type: PloggingType,
    @JsonFormat(pattern = "yyyy-MM-dd") var recruitStartDate: LocalDate,
    @JsonFormat(pattern = "yyyy-MM-dd") var recruitEndDate: LocalDate,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") var startTime: LocalDateTime,
    var spendTime: Long,
    var startLocation: String,
    var endLocation: String
)