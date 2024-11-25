package com.example.plotting_fe.mypage.dto.response

data class MyProfileResponse (
    val nickname :String,
    val email: String,
    val profileMessage: String,
    val profileImageUrl: String,
    val isProfilePublic: Boolean
)