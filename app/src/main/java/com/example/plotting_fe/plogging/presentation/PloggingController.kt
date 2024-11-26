package com.example.plotting_fe.plogging.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.response.HomeResponse
import com.example.plotting_fe.plogging.dto.PloggingType
import com.example.plotting_fe.plogging.dto.request.CommentUpdateRequest
import com.example.plotting_fe.plogging.dto.request.CommentUploadRequest
import com.example.plotting_fe.plogging.dto.request.PloggingRequest
import com.example.plotting_fe.plogging.dto.response.CommentResponse
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import com.example.plotting_fe.plogging.dto.response.PloggingResponse
import com.example.plotting_fe.plogging.dto.response.PloggingUserListResponse
import com.fasterxml.jackson.annotation.JsonFormat
import com.plotting.server.plogging.dto.response.PloggingMapResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface PloggingController {

    //플로깅 검색
    @GET("/ploggings/{title}")
    fun getPloggingWithTitle(
        @Path("title") title: String
    ): Call<ResponseTemplate<PloggingResponse>>

    //플로깅 홈
    @GET("/ploggings/home/{userId}")
    fun getHome(
        @Query("userId") userId: Long,
    ): Call<ResponseTemplate<HomeResponse>>

    //플로깅 모임 등록
    @POST("/ploggings")
    fun createPlogging(
        @Body request: PloggingRequest
    ): Call<ResponseTemplate<PloggingRequest>>

    // FIXME: time 관련 String으로 변경
    // 플로깅 리스트 조회
    @GET("/ploggings/filter")
    fun findListByFilter(
        @Query("region") region: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("type") type: String,
        @Query("spendTime") spendTime: Long,
        @Query("startTime") startTime: String,
        @Query("maxPeople") maxPeople: Long
    ): Call<ResponseTemplate<List<PloggingResponse>>>


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
        @Path("ploggingId") ploggingId: Long
    ): Call<ResponseTemplate<String>>

    @GET("/ploggings/{ploggingId}/comments")
    fun getComments(
        @Path("ploggingId") ploggingId: Long
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

    @POST("/ploggings/{ploggingId}/comments")
    fun uploadComment(
        @Path("ploggingId") ploggingId: Long,
        @Body request: CommentUploadRequest
    ): Call<ResponseTemplate<Void>>

    @GET("/ploggings/map/info")
    fun getPloggingInBounds(
        @Query("lat1") lat1: Double,
        @Query("lon1") lon1: Double,
        @Query("zoom") zoom: Int
    ): Call<ResponseTemplate<List<PloggingMapResponse>>>
}