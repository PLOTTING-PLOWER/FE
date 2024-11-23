package com.example.plotting_fe.user.presentation


import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.user.dto.request.LoginRequest
import com.example.plotting_fe.user.dto.request.SignUpRequest
import com.example.plotting_fe.user.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthController {
    @POST("/auth/signup")
    fun signUp(@Body request: SignUpRequest): Call<ResponseTemplate<Void>>

    @GET("/auth/check-nickname")
    fun checkNickname(@Query("nickname") nickname: String): Call<ResponseTemplate<Boolean>>

    @POST("/auth/login/self")
    fun login(@Body request: LoginRequest): Call<ResponseTemplate<LoginResponse>>

    @POST("/login/oauth2/code/naver")
    fun exchangeNaverCode(@Query("code") code: String): Call<ResponseTemplate<LoginResponse>>

}