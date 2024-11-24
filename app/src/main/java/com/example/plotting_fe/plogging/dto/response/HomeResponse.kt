package com.example.plotting_fe.plogging.dto.response

data class HomeResponse(
    val ploggingResponseList: List<PloggingResponse>,
    val plowerResponseList: PlowerListResponse,
    val userNickname: String
)
