package com.example.plotting_fe.plogging.dto.request

data class PloggingRequest(
    var title: String,
    var content: String,
    var maxPeople: Long,
    var type: String,
    var recruitStartDate: String,
    var recruitEndDate: String,
    var startTime: String,
    var spendTime: Long,
    var startLocation: String,
    var endLocation: String
)