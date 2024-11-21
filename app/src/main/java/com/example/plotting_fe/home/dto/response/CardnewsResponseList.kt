package com.example.plotting_fe.home.dto.response

data class CardnewsResponseList(
    val items: List<CardnewsResponse>
) {
    data class CardnewsResponse(
        val id: Long,
        val title: String
    )
}


