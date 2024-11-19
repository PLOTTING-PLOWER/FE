package com.example.plotting_fe.plogging.dto.response

data class PlowerListResponse (
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingUserList: List<PloggingUser>
){
    data class PloggingUser(
        val profileImageUrl: String,
        val nickname: String
    )
}