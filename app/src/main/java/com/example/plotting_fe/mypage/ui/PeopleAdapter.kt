package com.example.plotting_fe.mypage.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.mypage.dto.Person

class PeopleAdapter(
    private val peopleList: List<Person>,
    private val onPersonClickListener: OnPersonClickListener
) : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    interface OnPersonClickListener {
        fun onPersonClick(person: Person)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(peopleList[position])
    }

    override fun getItemCount(): Int = peopleList.size

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.starIcon)

        // 데이터를 뷰에 바인딩
        fun bind(person: Person) {
            nameTextView.text = person.name
            detailsTextView.text = person.details

            // 아이템 클릭 리스너 설정
            itemView.setOnClickListener { onPersonClickListener.onPersonClick(person) }
        }
    }
}

