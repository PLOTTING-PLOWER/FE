package com.example.plotting_fe.plogging.dto

data class Comment (
    val id: Long,
    val username: String,
    val timestamp: String,
    val content: String,
    val profileImageUrl: String,
    val depth: Long,
    val parentCommentId: Long,
    val isCommentPublic: Boolean,
    val isWriter: Boolean,
    val replies: List<Reply> = emptyList() // 답변 목록
)

data class Reply (
    val id: Long,
    val username: String,
    val timestamp: String,
    val content: String,
    val profileImageUrl: String,
    val depth: Long,
    val parentCommentId: Long,
    val isCommentPublic: Boolean,
    val isWriter: Boolean,
)