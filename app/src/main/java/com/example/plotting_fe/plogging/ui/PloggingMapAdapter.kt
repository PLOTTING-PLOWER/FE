package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.plotting.server.plogging.dto.response.PloggingMapResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PloggingMapAdapter(
    private val context: Context,
    private val itemList: List<PloggingMapResponse>
) : RecyclerView.Adapter<PloggingMapAdapter.PloggingMapViewHolder>() {

    inner class PloggingMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val PloggingType: TextView = view.findViewById(R.id.tvploggingType)
        val Title: TextView = view.findViewById(R.id.tvTitle)
        val CurrentPeople: TextView = view.findViewById(R.id.tvCurrentPeople)
        val MaxPeople: TextView = view.findViewById(R.id.tvMaxPeople)
        val StartLocation: TextView = view.findViewById(R.id.tvStartLocation)
        val StartTime: TextView = view.findViewById(R.id.tvStartTime)
        val SpendTime: TextView = view.findViewById(R.id.tvSpendTime)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)
        val btnJoin: TextView = itemView.findViewById(R.id.btnJoin)
        private val statusImage: ImageView = itemView.findViewById(R.id.tvStatus)
        private val statusText: TextView = itemView.findViewById(R.id.tvStatusText)

        fun bind(item: PloggingMapResponse) {

            Title.text = item.title
            StartLocation.text = item.startLocation

            val formattedTime = formatStartTime(item.startTime)
            StartTime.text = formattedTime

            SpendTime.text = "${(item.spendTime / 60)}H"
            CurrentPeople.text = "${item.currentPeople}"
            MaxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                PloggingType.text = "선착순"
            } else {
                PloggingType.text = "승인제"
            }

            // 진행 여부 표시
            val isOngoing = formatStatus(item.recruitEndDate)
            statusText.text = if (isOngoing) "진행" else "마감"
            statusImage.setImageResource(
                if (isOngoing) R.drawable.ic_green_circle else R.drawable.ic_red_circle
            )

            if (item.isStar) {
                grayStar.visibility = View.GONE
                colorStar.visibility = View.VISIBLE
            } else {
                grayStar.visibility = View.VISIBLE
                colorStar.visibility = View.GONE
            }

            grayStar.setOnClickListener {
                updateStar(item.ploggingId)
                grayStar.visibility = View.GONE
                colorStar.visibility = View.VISIBLE
            }

            colorStar.setOnClickListener {
                updateStar(item.ploggingId)
                grayStar.visibility = View.VISIBLE
                colorStar.visibility = View.GONE
            }

            // 참여하기 버튼 클릭 리스너 설정
            btnJoin.setOnClickListener {
                val intent = Intent(context, PloggingDetailActivity::class.java)
                intent.putExtra("ploggingId", item.ploggingId) // 플로깅 ID 전달
                context.startActivity(intent) // Context를 사용해 액티비티 이동
            }
        }
    }

    fun updateStar(ploggingId: Long) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.updateStar(ploggingId).enqueue(object :
            Callback<ResponseTemplate<Void>> {
            override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                if (response.isSuccessful) {
                    Log.d("post", "성공: $ploggingId")
                } else {
                    Log.d("post", "삭제 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.d("post", "실패: ${t.message}")
            }
        })
    }

    private fun formatStatus(recruitEndDate: String): Boolean {
        return try {
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDateTime = Date() // 현재 시간
            val endDateTime = dateTimeFormat.parse(recruitEndDate)

            endDateTime != null && endDateTime.after(currentDateTime)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingMapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plogging_info, parent, false)
        return PloggingMapViewHolder(view)
    }

    override fun onBindViewHolder(holder: PloggingMapViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun formatStartTime(input: String): String {
        // Step 1: 입력 형식에 맞는 SimpleDateFormat 정의
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Step 2: 원하는 출력 형식 정의
        val outputFormat = SimpleDateFormat("MM.dd (E) a hh:mm", Locale.KOREAN)

        // Step 3: 입력 문자열을 Date로 변환
        val date = inputFormat.parse(input)

        // Step 4: Date 객체를 원하는 형식으로 변환
        return outputFormat.format(date)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
