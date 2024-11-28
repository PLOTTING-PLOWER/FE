package com.example.plotting_fe.plogging.dto.response

import com.example.plotting_fe.plogging.dto.Participant

data class PloggingUserListResponse(
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingUserList: List<Participant>
) {
}
