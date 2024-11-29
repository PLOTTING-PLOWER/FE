package com.example.plotting_fe.home.dto.response

import com.example.plotting_fe.mypage.dto.Person
import com.google.gson.annotations.SerializedName

data class RankingResponse (
    val userId : Long,
    val nickname: String ,
    val profileImageUrl: String ,
    val hourRank: Long ,
    val totalHours: Long ,
    val totalCount:Long
)