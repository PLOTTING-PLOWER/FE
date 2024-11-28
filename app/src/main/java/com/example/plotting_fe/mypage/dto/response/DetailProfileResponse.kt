package com.example.plotting_fe.mypage.dto.response

data class DetailProfileResponse (
    val nickname: String,
    val profileMessage: String ,
    val profileImageUrl: String,
    val isStar :Boolean,         // 내가 즐겨찾기 했는지 여부
    val totalPloggingNumber: Long,   // 총 플로깅 획수
    val totalPloggingTime: Long ,     // 총 플로깅 시간
    val ranking:Long                 // 랭킹
)