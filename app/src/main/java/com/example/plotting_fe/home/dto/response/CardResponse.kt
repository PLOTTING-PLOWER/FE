package com.example.plotting_fe.home.dto.response

data class CardResponse(
    val cardId: Long,
    val cardTitle: String,
    val cardUrls: List<String>
)
