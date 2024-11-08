package com.example.plotting_fe.plogging.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.plogging.dto.Comment
import com.example.plotting_fe.plogging.dto.Reply
import com.example.plotting_fe.plogging.dto.request.CommentUpdateRequest
import com.example.plotting_fe.plogging.dto.request.CommentUploadRequest
import com.example.plotting_fe.plogging.dto.response.CommentResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PloggingCommentFragment : Fragment() {

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val comments = mutableListOf<Comment>()
    private var replies = mutableListOf<Reply>()
    private val ploggingId = 1L

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plogging_comment, container, false)

        commentsRecyclerView = view.findViewById(R.id.rv_comment)
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 예시 댓글 데이터 추가
        replies = mutableListOf(
            Reply(1, "YU", "2024.10.08 12:30 PM", "답변 내용", "", 1, 1, true, true),
            Reply(2, "PARK", "2024.10.08 12:31 PM", "또 다른 답변 내용", "", 1, 1, true, true)
        )

        comments.add(Comment(1, "LEE", "2024.10.08 12:00 PM", "같이 플로깅 할래?", "", 0, 0, true, true, replies))
        comments.add(Comment(2, "OH", "2024.10.08 12:00 PM", "또 다른 댓글", "", 0, 0, true, true, mutableListOf()))

        loadInfo(view)

        commentAdapter = CommentAdapter(comments) { comment ->
            // 댓글에 답글 작성
            uploadReply(view, comment)
        }

        commentsRecyclerView.adapter = commentAdapter

        makeCommentRequest(view)

        return view
    }

    private fun uploadReply(
        view: View,
        comment: Comment
    ) {
        view.findViewById<LinearLayout>(R.id.ll_upload_reply)
            .setOnClickListener(View.OnClickListener {
                view.findViewById<LinearLayout>(R.id.ll_parent_comment).setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.transparent_main)
                )

                Toast.makeText(requireContext(), "대댓글을 작성해주세요!", Toast.LENGTH_SHORT).show()

                view.findViewById<ImageButton>(R.id.btn_send)
                    .setOnClickListener(View.OnClickListener {

                        val content = view.findViewById<EditText>(R.id.et_comment).text.toString()
                        val parentCommentId = comment.id
                        val depth = 1L
                        var isCommentPublic = true

                        if (view.findViewById<CheckBox>(R.id.btn_comment).isChecked) {
                            isCommentPublic = false
                        }

                        val uploadRequest =
                            CommentUploadRequest(content, parentCommentId, depth, isCommentPublic)

                        // 입력 필드 초기화
                        view.findViewById<EditText>(R.id.et_comment).text.clear()

                        uploadComment(uploadRequest)

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                        // 댓글 화면에 바로 추가
                        // 새 댓글 생성
                        val newReply = Reply(
                            id = replies.size.toLong() + 1,
                            username = "슝슝이",
                            timestamp = LocalDateTime.now().format(formatter),
                            content = content,
                            profileImageUrl = "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png",
                            depth = depth,
                            parentCommentId = parentCommentId,
                            isCommentPublic = isCommentPublic,
                            isWriter = true
                        )

                        // 해당 댓글의 replies 리스트에 newReply 추가
                        comment.replies.add(newReply)

                        // 입력 필드 초기화
                        view.findViewById<EditText>(R.id.et_comment).text.clear()

                        // 어댑터에 데이터 변경 알림
                        commentAdapter.notifyItemChanged(comments.indexOf(comment))
                    })

                // 어댑터에 데이터 변경 알림
                view.findViewById<ImageView>(R.id.iv_option)
                    .setOnClickListener(View.OnClickListener {
                        showOptionsDialog(comment, view)
                    })
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeCommentRequest(view: View) {
        view.findViewById<ImageButton>(R.id.btn_send).setOnClickListener {
            // 댓글 작성
            val content = view.findViewById<EditText>(R.id.et_comment).text.toString()
            val parentCommentId = 0L
            val depth = 0L
            var isCommentPublic = true

            if (view.findViewById<CheckBox>(R.id.btn_comment).isChecked) {
                isCommentPublic = false
            }

            val uploadRequest =
                CommentUploadRequest(content, parentCommentId, depth, isCommentPublic)

            // 입력 필드 초기화
            view.findViewById<EditText>(R.id.et_comment).text.clear()

            uploadComment(uploadRequest)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            // 댓글 화면에 바로 추가
            // 새 댓글 생성
            val newComment = Comment(
                id = comments.size.toLong() + 1,
                username = "슝슝이",
                timestamp = LocalDateTime.now().format(formatter),
                content = content,
                profileImageUrl = "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png",
                depth = 0L,
                parentCommentId = 0L,
                isCommentPublic = isCommentPublic,
                isWriter = true,
                replies = mutableListOf()
            )

            // 댓글 목록에 추가
            comments.add(newComment)

            // 어댑터에 데이터 변경 알림
            commentAdapter.notifyItemInserted(comments.size - 1)

            // 입력 필드 초기화
            view.findViewById<EditText>(R.id.et_comment).text.clear()
        }
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
                                comment.commentId,
                                comment.nickname,
                                comment.createdDate,
                                comment.content,
                                comment.profileImageUrl,
                                comment.depth,
                                comment.parentCommentId,
                                comment.isCommentPublic,
                                comment.isWriter,
                                comment.childComments.map { childComment ->
                                    Reply(
                                        childComment.commentId,
                                        childComment.nickname,
                                        childComment.createdDate,
                                        childComment.content,
                                        childComment.profileImageUrl,
                                        childComment.depth,
                                        childComment.parentCommentId,
                                        childComment.isCommentPublic,
                                        childComment.isWriter
                                    )
                                }.toMutableList()
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

    private fun uploadComment(uploadRequest: CommentUploadRequest) {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.uploadComment(ploggingId, 1, uploadRequest).enqueue(object :
            Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>,
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공")
                } else {
                    // 에러 메시지 로깅
                    val errorBody = response.errorBody()?.string()
                    Log.d("post", "onResponse 실패 + ${response.code()}, 에러: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<Void>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun showOptionsDialog(comment: Comment, view: View) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_comment_options, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.tv_edit).setOnClickListener {
            dialog.dismiss()
            editText(view, comment)
        }

        dialogView.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
            dialog.dismiss()
            deleteText(view, comment)
        }

        dialog.show()
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    private fun editText(view: View, comment: Comment) {
        view.findViewById<TextView>(R.id.tv_content).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.ll_modify).visibility = View.VISIBLE

        Log.d("post", "comment.content: ${comment.content}")

        view.findViewById<EditText>(R.id.et_content).hint = comment.content

        view.findViewById<TextView>(R.id.btn_modify).setOnClickListener {
            val updateContent = view.findViewById<EditText>(R.id.et_content).text.toString()
            val commentId = comment.id
            val updateRequest = CommentUpdateRequest(updateContent, comment.isCommentPublic)
            updateComment(commentId, updateRequest)

            val content = view.findViewById<TextView>(R.id.tv_content)

            content.text = updateContent
            content.visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.ll_modify).visibility = View.GONE
        }
    }

    private fun updateComment(commentId: Long, updateRequest: CommentUpdateRequest) {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.updateComment(commentId, updateRequest).enqueue(object :
            Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>,
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공")
                } else {
                    // 에러 메시지 로깅
                    val errorBody = response.errorBody()?.string()
                    Log.d("post", "onResponse 실패 + ${response.code()}, 에러: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<Void>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun deleteText(view: View, comment: Comment) {
        view.findViewById<TextView>(R.id.tv_nickname).text = "(삭제)"
        view.findViewById<TextView>(R.id.tv_content).text = "삭제된 댓글입니다."
        view.findViewById<TextView>(R.id.tv_date).visibility = View.GONE
        view.findViewById<ImageView>(R.id.iv_option).visibility = View.GONE
        view.findViewById<ImageView>(R.id.profileImage).setImageResource(R.drawable.ic_flower)

        deleteComment(comment)
    }

    private fun deleteComment(comment: Comment) {
        val commentId = comment.id
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.deleteComment(commentId).enqueue(object :
            Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>,
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공")
                } else {
                    // 에러 메시지 로깅
                    val errorBody = response.errorBody()?.string()
                    Log.d("post", "onResponse 실패 + ${response.code()}, 에러: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<Void>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }
}