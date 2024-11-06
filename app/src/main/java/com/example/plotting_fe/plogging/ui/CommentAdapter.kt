package com.example.plotting_fe.plogging.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.plogging.dto.Comment

class CommentAdapter(
    private val comments: List<Comment>,
    private val onCommentClick: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val nickname: TextView = itemView.findViewById(R.id.tv_nickname)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val content: TextView = itemView.findViewById(R.id.tv_content)
        val repliesRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_reply)

        fun bind(comment: Comment) {
            nickname.text = comment.username
            date.text = comment.timestamp
            content.text = comment.content

            val profileImageUrl = "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"

            // Glide를 사용하여 이미지 로드
            Glide.with(profileImage)
                .load(profileImageUrl) // participant.imageUrl에 실제 이미지 URL이 있어야 함
                .placeholder(R.drawable.ic_flower) // 로딩 중에 표시할 이미지
                .error(R.drawable.ic_flower) // 오류 발생 시 표시할 이미지
                .into(profileImage)

            Log.d("post", "comment.isWriter: ${comment.isWriter}")

            if (!comment.isWriter) {
                itemView.findViewById<ImageView>(R.id.iv_option).visibility = View.GONE
            }

            // 답변 RecyclerView 설정
            repliesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            repliesRecyclerView.adapter = ReplyAdapter(comment.replies)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
        holder.itemView.setOnClickListener { onCommentClick(comment) } // 클릭 리스너 설정
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
