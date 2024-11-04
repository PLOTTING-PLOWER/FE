package com.example.plotting_fe.plogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PloggingController {

    @GET("/ploggings/{ploggingId}/info")
    fun getPloggingDetail(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<PloggingDetailResponse>>
}