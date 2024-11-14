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

class MyPloggingWaitingAdapter(private val items: List<WaitingPeople>) : RecyclerView.Adapter<MyPloggingWaitingAdapter.PloggingViewHolder>() {

    class PloggingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_profile_image)

        fun bind(item: WaitingPeople) {
            name.text = item.name

            val profileImageUrl = "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"

            // Glide를 사용하여 이미지 로드
            Glide.with(profileImage)
                .load(profileImageUrl) // participant.imageUrl에 실제 이미지 URL이 있어야 함
                .placeholder(R.drawable.ic_flower) // 로딩 중에 표시할 이미지
                .error(R.drawable.ic_flower) // 오류 발생 시 표시할 이미지
                .into(profileImage)
        }
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_waiting_people, parent, false)
        return PloggingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}