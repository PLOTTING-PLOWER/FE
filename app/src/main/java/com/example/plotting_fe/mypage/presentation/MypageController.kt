package com.example.plotting_fe.mypage.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.mypage.dto.request.MyProfileRequest
import com.example.plotting_fe.mypage.dto.response.DetailProfileResponse
import com.example.plotting_fe.mypage.dto.response.MyPageResponse
import com.example.plotting_fe.mypage.dto.response.MyProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    @GET("/mypage/main")
    fun getMyPage() : Call<ResponseTemplate<MyPageResponse>>

    @POST("/mypage/update-alarm")
    @FormUrlEncoded
    fun updateAlarm(
        @Field("isAlarmAllowed") isAlarmAllowed: Boolean
    ) : Call<ResponseTemplate<Void>>

    @GET("/mypage/{profileId}")
    fun getDetailProfile(
        @Path("profileId") profileId: Long
    ) :Call<ResponseTemplate<DetailProfileResponse>>
}