package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.plogging.dto.Comment
import com.example.plotting_fe.plogging.dto.Reply
import com.example.plotting_fe.plogging.dto.response.CommentResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PloggingCommentFragment : Fragment() {

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val comments = mutableListOf<Comment>()

    private val ploggingId = 1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plogging_comment, container, false)

        commentsRecyclerView = view.findViewById(R.id.rv_comment)
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 예시 댓글 데이터 추가
        val replyList = listOf(
            Reply("1", "YU", "2024.10.08 12:30 PM", "답변 내용"),
            Reply("2", "PARK", "2024.10.08 12:31 PM", "또 다른 답변 내용")
        )

        comments.add(Comment("1", "LEE", "2024.10.08 12:00 PM", "같이 플로깅 할래?", replyList))
        comments.add(Comment("2", "OH", "2024.10.08 12:00 PM", "또 다른 댓글", emptyList()))

        loadInfo(view)

        commentAdapter = CommentAdapter(comments) { comment ->
            showOptionsDialog(comment)
        }
        commentsRecyclerView.adapter = commentAdapter

        return view
    }

    private fun loadInfo(view: View) {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.getComments(ploggingId, 1).enqueue(object :
            Callback<ResponseTemplate<CommentResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<CommentResponse>>,
                response: Response<ResponseTemplate<CommentResponse>>,
            ) {
                if (response.isSuccessful) {
                    val body = response.body()?.results?.comments
                    body?.let {
                        comments.clear() // 기존 댓글 리스트를 비우고
                        comments.addAll(it.map { comment ->
                            // Comment 객체로 변환
                            Comment(
                                comment.commentId.toString(),
                                comment.nickname,
                                comment.createdDate,
                                comment.content,
                                comment.childComments.map { childComment ->
                                    Reply(
                                        childComment.commentId.toString(),
                                        childComment.nickname,
                                        childComment.createdDate,
                                        childComment.content
                                    )
                                }
                            )
                        })

                        commentAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
                    }
                } else {
                    // 에러 메시지 로깅
                    val errorBody = response.errorBody()?.string()
                    Log.d("post", "onResponse 실패 + ${response.code()}, 에러: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<CommentResponse>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun showOptionsDialog(item: Any) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_comment_options, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.tv_edit).setOnClickListener {
            when (item) {
                is Comment -> {
                    // comment 수정 로직
                }
                is Reply -> {
                    // reply 수정 로직
                }
            }

            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
            when (item) {
                is Comment -> {
                    // comment 수정 로직
                }
                is Reply -> {
                    // reply 수정 로직
                }
            }

            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }
}