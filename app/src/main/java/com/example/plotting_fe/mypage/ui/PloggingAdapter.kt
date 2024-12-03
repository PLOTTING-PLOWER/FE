package com.example.plotting_fe.mypage.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.mypage.dto.Plogging
import com.example.plotting_fe.mypage.presentation.StarController
import com.example.plotting_fe.plogging.ui.PloggingDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PloggingAdapter(
    private val ploggingList: MutableList<Plogging>,
) : RecyclerView.Adapter<PloggingAdapter.PloggingViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PloggingViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_plogging_info, parent, false);
        return PloggingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        holder.bind(ploggingList[position])
    }

    override fun getItemCount(): Int = ploggingList.size

    inner class PloggingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val title :TextView = itemView.findViewById(R.id.tvTitle)
        private val ploggingType :TextView = itemView.findViewById(R.id.tvploggingType)
        private val maxPeople :TextView = itemView.findViewById(R.id.tvMaxPeople)
        private val currentPeople :TextView = itemView.findViewById(R.id.tvCurrentPeople)
        private val startTime :TextView = itemView.findViewById(R.id.tvStartTime)
        private val startLocation :TextView = itemView.findViewById(R.id.tvStartLocation)
        private val spendTime :TextView = itemView.findViewById(R.id.tvSpendTime)
        private val status :TextView = itemView.findViewById(R.id.tvStatusText)
        private val statusImage :ImageView = itemView.findViewById(R.id.tvStatus)
        private val starIcon : ImageView = itemView.findViewById(R.id.iv_gray_star)
        private val btnJoin : TextView = itemView.findViewById(R.id.btnJoin)
        private val background : ConstraintLayout = itemView.findViewById(R.id.background)


        @SuppressLint("ResourceAsColor")
        fun bind(plogging: Plogging){
            title.text = plogging.title
            maxPeople.text = plogging.maxPeople.toString()
            currentPeople.text = plogging.currentPeople.toString()
            startTime.text = plogging.startTime
            startLocation.text = plogging.startLocation
            spendTime.text = (plogging.spendTime/60).toString()+"H"
            starIcon.setImageResource(R.drawable.ic_star_color)

            startLocation.text = if (plogging.startLocation.length > 20) {
                "${plogging.startLocation.take(20)}..." // 앞 25자만 가져오고 "..." 추가
            } else {
                plogging.startLocation
            }

            if(plogging.ploggingType.name == "DIRECT"){
                ploggingType.text = "선착순"
            }else{
                ploggingType.text = "승인제"
            }

            val recruitEndDate = LocalDate.parse(plogging.recruitEndDate, DateTimeFormatter.ISO_DATE)

            // 모집 종료 날짜와 오늘 날짜를 비교
            if (recruitEndDate.isBefore(LocalDate.now())) {
                status.text = "마감"
                statusImage.setImageResource(R.drawable.ic_red_circle)
                // 배경 틴트 색상 설정
                background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.light_gray))
                btnJoin.visibility = View.GONE

                // 마감 상태일 때만 클릭 가능
                background.setOnClickListener {
                    Log.d("PloggingAdapter", "마감된 플로깅 클릭됨")
                    val intent = Intent(itemView.context, PloggingDetailActivity::class.java)
                    intent.putExtra("ploggingId", plogging.ploggingId)
                    itemView.context.startActivity(intent)
                }

            } else {
                background.setOnClickListener(null)
                status.text = "진행"
                statusImage.setImageResource(R.drawable.ic_green_circle) // 녹색 아이콘으로 표시
                btnJoin.text = "참가하기"
            }

            // 즐겨찾기 버튼
            starIcon.setOnClickListener {
                onStarClick(plogging, adapterPosition)
            }

            // 참가하기 버튼
            btnJoin.setOnClickListener {
                val context = itemView.context // `itemView`를 통해 context를 가져옵니다.
                val intent = Intent(context, PloggingDetailActivity::class.java)
                intent.putExtra("ploggingId", plogging.ploggingId) // `ploggingId` 전달
                context.startActivity(intent) // `context`에서 `startActivity` 호출
            }

        }

        private fun onStarClick(plogging: Plogging, position: Int) {
            // 즐겨찾기 API 호출
            togglePloggingStar(plogging.ploggingId, position)
        }

        private fun togglePloggingStar(ploggingId: Long, position: Int) {
            val starController = ApiClient.getApiClient().create(StarController::class.java)
            starController.updatePloggingStar(ploggingId).enqueue(object :
                Callback<ResponseTemplate<Boolean>> {
                override fun onResponse(call: Call<ResponseTemplate<Boolean>>, response: Response<ResponseTemplate<Boolean>>) {
                    if (response.isSuccessful) {
                        // 서버에서 반환된 즐겨찾기 상태로 UI 업데이트
                        val isStarred = response.body()?.results ?: false
                        if(!isStarred){
                            ploggingList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, ploggingList.size) // 나머지 항목 갱신
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