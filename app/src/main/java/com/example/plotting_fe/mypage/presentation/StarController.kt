package com.example.plotting_fe.mypage.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.mypage.dto.response.MyPloggingStarListResponse
import com.example.plotting_fe.mypage.dto.response.MyUserStarListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StarController {
    @GET("/star/users/list")
    fun getMyUserStarList(): Call<ResponseTemplate<MyUserStarListResponse>>

    @GET("/star/plogging/list")
    fun getMyPloggingStarList(): Call<ResponseTemplate<MyPloggingStarListResponse>>

    @POST("/star/users/{starId}")
    fun updateUserStar(
        @Path("starId") starId: Long
    ): Call<ResponseTemplate<Boolean>>

    @POST("/star/plogging/{ploggingId}")
    fun updatePloggingStar(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<Boolean>>

}