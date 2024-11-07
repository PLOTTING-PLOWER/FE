package com.example.plotting_fe.plogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.plogging.dto.request.CommentUpdateRequest
import com.example.plotting_fe.plogging.dto.response.CommentResponse
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import com.example.plotting_fe.plogging.dto.response.PloggingUserListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("/ploggings/{ploggingId}/comments")
    fun getComments(
        @Path("ploggingId") ploggingId: Long,
        @Query("userId") userId: Long
    ): Call<ResponseTemplate<CommentResponse>>

    @PATCH("/ploggings/comments/{commentId}")
    fun updateComment(
        @Path("commentId") commentId: Long,
        @Body request: CommentUpdateRequest
    ): Call<ResponseTemplate<Void>>

    @DELETE("/ploggings/comments/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Long
    ): Call<ResponseTemplate<Void>>
}