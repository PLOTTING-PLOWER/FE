package com.example.plotting_fe.home.dto.response

import com.example.plotting_fe.plogging.dto.response.PloggingGetStarListResponse
import com.example.plotting_fe.plogging.dto.response.PloggingListResponse
import com.example.plotting_fe.plogging.dto.response.PloggingResponse
import com.example.plotting_fe.plogging.dto.response.PlowerListResponse

data class HomeResponse(
    val ploggingGetStarResponseList: PloggingGetStarListResponse,
    val plowerResponseList: PlowerListResponse,
    val userNickname: String,
    val userImageUrl: String
) 