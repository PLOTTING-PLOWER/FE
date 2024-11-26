package com.example.plotting_fe.myplogging.dto.response

data class MyPloggingSummaryResponse(

    val totalPloggingCount: Int,  // 총 플로깅 횟수
    val totalSpendTime: Long,     // 총 플로깅 시간 (시)
    val nickname: String,         // 유저 닉네임
    val profileImageUrl: String  // 유저 프로필 이미지 URL

)