package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.plogging.dto.Reply

class ReplyAdapter(private val replies: List<Reply>) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname: TextView = itemView.findViewById(R.id.tv_nickname)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val content: TextView = itemView.findViewById(R.id.tv_content)
        val option: ImageView = itemView.findViewById(R.id.iv_option)

        fun bind(reply: Reply) {
            nickname.text = reply.username
            date.text = reply.timestamp
            content.text = reply.content
            option.setOnClickListener(View.OnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_comment_options, null)
                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .create()

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.setGravity(Gravity.BOTTOM)
                dialog.show()
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(replies[position])
    }

    override fun getItemCount(): Int {
        return replies.size
    }
}
