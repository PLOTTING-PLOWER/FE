package com.example.plotting_fe.home.dto.response

import com.example.plotting_fe.mypage.dto.Person
import com.google.gson.annotations.SerializedName

data class RankingListResponse (
    val topRankings: List<RankingResponse>,
    val myRanking: RankingResponse
)