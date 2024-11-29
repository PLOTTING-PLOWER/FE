package com.example.plotting_fe.home.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.response.RankingListResponse
import com.example.plotting_fe.mypage.dto.response.MyPloggingStarListResponse
import com.example.plotting_fe.mypage.dto.response.MyUserStarListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RankingController {
    @GET("/ranking/list")
    fun getRankingList(): Call<ResponseTemplate<RankingListResponse>>
}