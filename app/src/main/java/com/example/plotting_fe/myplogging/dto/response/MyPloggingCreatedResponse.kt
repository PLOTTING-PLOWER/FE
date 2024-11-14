package com.example.plotting_fe.myplogging.dto.response

import com.example.plotting_fe.myplogging.dto.PloggingData

data class MyPloggingCreatedResponse(
    val myPloggings: List<PloggingData>
) {
}
