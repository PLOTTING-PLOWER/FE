package com.example.plotting_fe.plogging.dto.request

data class CommentUploadRequest(
    val content: String,
    val parentCommentId: Long,
    val depth: Long,
    val isCommentPublic: Boolean
)