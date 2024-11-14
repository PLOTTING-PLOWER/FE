package com.example.plotting_fe.myplogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.myplogging.dto.response.MyPloggingCreatedResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingWaitingResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPloggingController {

    @GET("/my-ploggings/created")
    fun getMyPloggingCreated(
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<MyPloggingCreatedResponse>>

    @GET("/my-ploggings/{ploggingId}/waiting/users")
    fun getWaitingUsers(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<MyPloggingWaitingResponse>>

    @POST("/my-ploggings/{ploggingId}/request/{ploggingUserId}")
    fun reqeustApprove(
        @Path("ploggingId") ploggingId: Long,
        @Path("ploggingUserId") ploggingUserId: Long
    ): Call<ResponseTemplate<String>>

    @DELETE("/my-ploggings/request/{ploggingUserId}")
    fun requestReject(
        @Path("ploggingUserId") ploggingUserId: Long
    ): Call<ResponseTemplate<Void>>
}