package com.example.plotting_fe.plogging.dto.response

data class HomeResponse(

    val ploggingResponseList: PloggingListResponse,
    val plowerResponseList: PlowerListResponse,
    val userNickname: String
)
