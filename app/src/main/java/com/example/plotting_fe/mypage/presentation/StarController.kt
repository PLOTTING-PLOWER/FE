package com.example.plotting_fe.mypage.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.mypage.dto.response.MyUserStarListResponse
import retrofit2.Call
import retrofit2.http.GET

interface StarController {
    @GET("/star/users/list")
    fun getMyUserStarList(): Call<ResponseTemplate<MyUserStarListResponse>>
}