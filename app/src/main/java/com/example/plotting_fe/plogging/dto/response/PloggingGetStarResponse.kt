package com.example.plotting_fe.plogging.dto.response

data class PloggingGetStarResponse(
    val ploggingId: Long,
    val title: String,
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingType: String,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation: String,
    var isStar: Boolean // var로 변경하여 값을 변경 가능하게 만듬
)
