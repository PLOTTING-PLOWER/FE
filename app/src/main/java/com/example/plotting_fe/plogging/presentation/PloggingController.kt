package com.example.plotting_fe.plogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import com.example.plotting_fe.plogging.dto.response.PloggingUserListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PloggingController {

    @GET("/ploggings/{ploggingId}/info")
    fun getPloggingDetail(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<PloggingDetailResponse>>

    @GET("/ploggings/{ploggingId}/users")
    fun getPloggingUsers(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<PloggingUserListResponse>>

    @POST("/ploggings/{ploggingId}")
    fun joinPlogging(
        @Path("ploggingId") ploggingId: Long,
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<String>>
}