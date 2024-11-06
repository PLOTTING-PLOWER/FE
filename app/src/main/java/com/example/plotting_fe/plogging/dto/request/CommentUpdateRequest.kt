package com.example.plotting_fe.plogging.dto.request

data class CommentUpdateRequest(
    var content: String,
    val isCommentPublic: Boolean
)