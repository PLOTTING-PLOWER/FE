package com.example.plotting_fe.mypage.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.mypage.dto.Person
import com.example.plotting_fe.mypage.presentation.StarController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleAdapter(
    private val peopleList: MutableList<Person>,
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
        private val nicknameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        private val profileImage: AppCompatImageView = itemView.findViewById(R.id.profileImage)
        private val starIcon: ImageView = itemView.findViewById(R.id.starIcon)

        // 데이터를 뷰에 바인딩
        fun bind(person: Person) {
            nicknameTextView.text = person.nickname

            // 프로필 메시지가 길면 잘라서 표시
            val maxLength = 50
            detailsTextView.text = if (person.profileMessage.length > maxLength) {
                "${person.profileMessage.take(maxLength)}..." // 앞 50자만 가져오고 "..." 추가
            } else {
                person.profileMessage
            }

            Glide.with(itemView.context)
                .load(person.profileImageUrl)
                .apply(RequestOptions().circleCrop()) // 이미지를 원형으로 변환
                .placeholder(R.drawable.ic_flower)
                .into(profileImage)

            // 별 아이콘 클릭 이벤트
            starIcon.setOnClickListener {
                onStarClick(person, adapterPosition)
            }

            // 아이템 클릭 리스너 설정
            itemView.setOnClickListener { onPersonClickListener.onPersonClick(person) }
        }

        private fun onStarClick(person: Person, position: Int) {
            // 즐겨찾기 API 호출
            toggleUserStar(person.userId, position)
        }

        private fun toggleUserStar(starId: Long, position: Int) {
            val starController = ApiClient.getApiClient().create(StarController::class.java)
            starController.updateUserStar(starId).enqueue(object :
                Callback<ResponseTemplate<Boolean>> {
                override fun onResponse(call: Call<ResponseTemplate<Boolean>>, response: Response<ResponseTemplate<Boolean>>) {
                    if (response.isSuccessful) {
                        // 서버에서 반환된 즐겨찾기 상태로 UI 업데이트
                        val isStarred = response.body()?.results ?: false
                        if(!isStarred){
                            peopleList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, peopleList.size) // 나머지 항목 갱신
                            Log.d("post", "즐겨찾기 해제 성공: $isStarred")
                        }
                    } else {
                        Log.d("post", "onResponse 실패: " + response.code())
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                    Log.d("post", "onFailure 에러: " +  t.message.toString())
                }
            })
        }


    }
}

