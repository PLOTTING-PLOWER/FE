package com.example.plotting_fe.myplogging.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingScheduledAdapter(
    private val context: Context,
    private val dataList: List<MyPloggingScheduledResponse>,
    private val onCancelClick: (ploggingId: Long, UserId: Long) -> Unit
) : RecyclerView.Adapter<MyPloggingScheduledAdapter.ViewHolder>() {

    // ViewHolder 클래스 정의
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val PloggingType: TextView = itemView.findViewById(R.id.tvploggingType)
        val Title: TextView = itemView.findViewById(R.id.tvTitle)
        val StartLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        val StartTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val SpendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        val CurrentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        val MaxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)
        val IsAssigned: TextView = itemView.findViewById(R.id.tvIsAssigned)
        val cancelButton: Button = itemView.findViewById(R.id.btnCancel)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)

        // 데이터를 뷰에 바인딩하는 메서드
        fun bind(item: MyPloggingScheduledResponse) {
            Title.text = item.title
            StartLocation.text = item.startLocation
            StartTime.text = item.startTime
            SpendTime.text = (item.spendTime / 60).toString()
            CurrentPeople.text = item.currentPeople.toString()
            MaxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                PloggingType.text = "선착순"
            } else {
                PloggingType.text = "승인제"
            }

            if (item.isAssigned) {
                IsAssigned.text = "승인완료"
            } else {
                IsAssigned.text = "승인대기"
            }

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

            cancelButton.setOnClickListener {
                // Call the onCancelClick function to trigger the API request
                onCancelClick(item.ploggingId, 1L)
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

    // RecyclerView에서 사용할 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scheduled_plogging, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = dataList.size
}
