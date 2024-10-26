package com.example.plotting_fe.plogging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.plogging.dto.Comment

class CommentAdapter(
    private val comments: List<Comment>,
    private val onCommentClick: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname: TextView = itemView.findViewById(R.id.tv_nickname)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val content: TextView = itemView.findViewById(R.id.tv_content)
        val repliesRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_reply)

        fun bind(comment: Comment) {
            nickname.text = comment.username
            date.text = comment.timestamp
            content.text = comment.content

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
