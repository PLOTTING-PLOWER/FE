package com.example.plotting_fe.mypage.dto

data class Person(
    val userId : Long,
    val profileImageUrl :String,
    val nickname: String,
    val profileMessage: String,
    val isProfilePublic: Boolean        // 비공개 여부
)