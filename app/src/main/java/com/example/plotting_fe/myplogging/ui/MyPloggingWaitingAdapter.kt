package com.example.plotting_fe.myplogging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.WaitingPeople

class MyPloggingWaitingAdapter(
    private var items: MutableList<WaitingPeople>,
    private val onAcceptClick: (WaitingPeople) -> Unit,
    private val onRejectClick: (WaitingPeople) -> Unit
) : RecyclerView.Adapter<MyPloggingWaitingAdapter.PloggingViewHolder>() {

    class PloggingViewHolder(
        itemView: View,
        private val onAcceptClick: (WaitingPeople) -> Unit,
        private val onRejectClick: (WaitingPeople) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_profile_image)
        val btnAccept: TextView = itemView.findViewById(R.id.btn_accept)
        val btnReject: TextView = itemView.findViewById(R.id.btn_reject)

        fun bind(item: WaitingPeople) {
            name.text = item.nickname
            val profileImageUrl = item.profileImageUrl

            // Glide를 사용하여 이미지 로드
            Glide.with(profileImage)
                .load(profileImageUrl)
                .placeholder(R.drawable.ic_flower)
                .error(R.drawable.ic_flower)
                .into(profileImage)

            // 버튼 클릭 리스너 설정
            btnAccept.setOnClickListener { onAcceptClick(item) }
            btnReject.setOnClickListener { onRejectClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_waiting_people, parent, false)
        return PloggingViewHolder(view, onAcceptClick, onRejectClick)
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(waitingPeople: WaitingPeople) {
        val position = items.indexOf(waitingPeople)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}