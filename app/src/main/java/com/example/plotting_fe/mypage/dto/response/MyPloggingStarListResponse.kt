package com.example.plotting_fe.mypage.dto.response

import com.example.plotting_fe.mypage.dto.Plogging
import com.google.gson.annotations.SerializedName

data class MyPloggingStarListResponse (
    @SerializedName("responses")
    val responses: List<Plogging>
)