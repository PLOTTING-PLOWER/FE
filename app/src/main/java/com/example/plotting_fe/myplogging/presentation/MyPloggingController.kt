package com.example.plotting_fe.myplogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.myplogging.dto.request.MyPloggingUpdateRequest
import com.example.plotting_fe.myplogging.dto.response.MonthResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingCreatedResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingParticipatedResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingSummaryResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingWaitingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPloggingController {

    @GET("/my-ploggings/created")
    fun getMyPloggingCreated(
    ): Call<ResponseTemplate<MyPloggingCreatedResponse>>

    @PATCH("/my-ploggings/created/{ploggingId}")
    fun updateMyPlogging(
        @Path("ploggingId") ploggingId: Long,
        @Body request: MyPloggingUpdateRequest
    ): Call<ResponseTemplate<Void>>

    @DELETE("/my-ploggings/created/{ploggingId}")
    fun deleteMyPlogging(
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<Void>>

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

    @GET("/my-ploggings/months")
    fun getMyMonthlyPlogging(
    ): Call<ResponseTemplate<MonthResponse>>

    @GET("/my-ploggings/participated")
    fun getMyPloggingParticipated(
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<List<MyPloggingParticipatedResponse>>>

    @GET("/my-ploggings/scheduled")
    fun getMyPloggingScheduled(
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<List<MyPloggingScheduledResponse>>>

    @GET("/my-ploggings/summary")
    fun getPloggingSummary(
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<MyPloggingSummaryResponse>>

    @DELETE("/my-ploggings/{ploggingId}/Cancel")
    fun reqeustCancel(
        @Path("ploggingId") ploggingId: Long,
        @Path("UserId") UserId: Long
    ): Call<ResponseTemplate<MyPloggingSummaryResponse>>


}