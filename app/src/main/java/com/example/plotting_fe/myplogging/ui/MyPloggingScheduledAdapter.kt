package com.example.plotting_fe.myplogging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse

class MyPloggingScheduledAdapter(
    private val dataList: List<MyPloggingScheduledResponse>
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
        }
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
