package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.plogging.dto.Reply
import com.example.plotting_fe.plogging.dto.request.CommentUpdateRequest
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReplyAdapter(private val replies: List<Reply>) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val nickname: TextView = itemView.findViewById(R.id.tv_nickname)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val content: TextView = itemView.findViewById(R.id.tv_content)
        val option: ImageView = itemView.findViewById(R.id.iv_option)

        fun bind(reply: Reply, view: View) {
            nickname.text = reply.username
            date.text = reply.timestamp
            content.text = reply.content
            val profileImageUrl = reply.profileImageUrl

            // Glide를 사용하여 이미지 로드
            Glide.with(profileImage)
                .load(profileImageUrl) // participant.imageUrl에 실제 이미지 URL이 있어야 함
                .placeholder(R.drawable.ic_flower) // 로딩 중에 표시할 이미지
                .error(R.drawable.ic_flower) // 오류 발생 시 표시할 이미지
                .into(profileImage)

            if(!reply.isWriter) {
                option.visibility = View.GONE
            }

            option.setOnClickListener(View.OnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comment_options, null)
                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .create()

                dialogView.findViewById<TextView>(R.id.tv_edit).setOnClickListener {
                    dialog.dismiss()
                    editText(view, reply)
                }

                dialogView.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
                    dialog.dismiss()
                    deleteText(view, reply)
                }

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setGravity(Gravity.BOTTOM)
                dialog.show()
            })
        }

        private fun editText(view: View, reply: Reply) {
            view.findViewById<TextView>(R.id.tv_content).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.ll_modify).visibility = View.VISIBLE

            Log.d("post", "comment.content: ${reply.content}")

            view.findViewById<EditText>(R.id.et_content).hint = reply.content

            view.findViewById<TextView>(R.id.btn_modify).setOnClickListener {
                val updateContent = view.findViewById<EditText>(R.id.et_content).text.toString()
                val commentId = reply.id
                val updateRequest = CommentUpdateRequest(updateContent, reply.isCommentPublic)
                updateReply(commentId, updateRequest)

                val content = view.findViewById<TextView>(R.id.tv_content)

                content.text = updateContent
                content.visibility = View.VISIBLE
                view.findViewById<LinearLayout>(R.id.ll_modify).visibility = View.GONE
            }
        }

        private fun updateReply(commentId: Long, updateRequest: CommentUpdateRequest) {
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

        private fun deleteText(view: View, reply: Reply) {
            view.findViewById<LinearLayout>(R.id.ll_reply).visibility = View.GONE
            deleteComment(reply)
        }

        private fun deleteComment(reply: Reply) {
            val commentId = reply.id
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(replies[position], holder.itemView)
    }

    override fun getItemCount(): Int {
        return replies.size
    }
}
