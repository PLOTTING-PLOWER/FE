package com.example.plotting_fe.home.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.request.AlarmRequest
import com.example.plotting_fe.home.dto.response.AlarmListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AlarmController {

    @POST("/alarm/fcm-token")
    fun sendFcmToken(@Body alarmRequest: AlarmRequest): Call<ResponseTemplate<Void>>

    @GET("/alarm/list")
    fun getAlarmList(): Call<ResponseTemplate<AlarmListResponse>>
}