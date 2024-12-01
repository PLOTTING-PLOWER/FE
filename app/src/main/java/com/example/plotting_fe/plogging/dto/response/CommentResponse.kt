package com.example.plotting_fe.plogging.dto.response

data class CommentResponse(
    val nickname: String,
    val profileImageUrl: String,
    val comments: List<commentList>
) {
    data class commentList(
        val commentId: Long,
        val profileImageUrl: String,
        val nickname: String,
        val content: String,
        val createdDate: String,
        val depth: Long,
        val parentCommentId: Long,
        val isCommentPublic: Boolean,
        val isWriter: Boolean,
        val childComments: List<childComment>
    )

    data class childComment(
        val commentId: Long,
        val profileImageUrl: String,
        val nickname: String,
        val content: String,
        val createdDate: String,
        val depth: Long,
        val parentCommentId: Long,
        val isCommentPublic: Boolean,
        val isWriter: Boolean
    )
}
