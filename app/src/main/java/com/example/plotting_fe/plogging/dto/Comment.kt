package com.example.plotting_fe.plogging.dto

data class Comment (
    val id: String,
    val username: String,
    val timestamp: String,
    val content: String,
    val replies: List<Reply> = emptyList() // 답변 목록
)

data class Reply (
    val id: String,
    val username: String,
    val timestamp: String,
    val content: String
)