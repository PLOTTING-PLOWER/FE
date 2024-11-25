package com.example.plotting_fe.mypage.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.mypage.dto.request.MyProfileRequest
import com.example.plotting_fe.mypage.dto.response.MyProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface MypageController {

    @GET("/mypage/profile")
    fun getMyProfile(): Call<ResponseTemplate<MyProfileResponse>>

    @Multipart
    @PATCH("/mypage/profile/edit")
    fun updateMyProfile(
        @Part profileImage: MultipartBody.Part?,    // 이미지 파일
        @Part("profileData") myProfileRequest: MyProfileRequest
    ): Call<ResponseTemplate<Void>>


}