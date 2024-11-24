package com.example.plotting_fe.mypage.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.mypage.dto.response.MyProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MypageController {

    @GET("/mypage/profile")
    fun getMyProfile(): Call<ResponseTemplate<MyProfileResponse>>

}