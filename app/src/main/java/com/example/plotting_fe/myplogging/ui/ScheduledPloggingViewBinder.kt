package com.example.plotting_fe.myplogging.ui

import android.view.View
import android.widget.TextView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse

class ScheduledPloggingViewBinder (private val includeView: View) {

    private val ploggingType: TextView = includeView.findViewById(R.id.tvploggingType)
    private val title: TextView = includeView.findViewById(R.id.tvTitle)
    private val startLocation: TextView = includeView.findViewById(R.id.tvStartLocation)
    private val startTime: TextView = includeView.findViewById(R.id.tvStartTime)
    private val spendTime: TextView = includeView.findViewById(R.id.tvSpendTime)
    private val currentPeople: TextView = includeView.findViewById(R.id.tvCurrentPeople)
    private val maxPeople: TextView = includeView.findViewById(R.id.tvMaxPeople)
    private val IsAssigned: TextView = includeView.findViewById(R.id.tvIsAssigned)

    fun bind(data: MyPloggingScheduledResponse) {
        title.text = data.title
        startLocation.text = data.startLocation
        startTime.text = data.startTime
        spendTime.text = "${(data.spendTime / 60)}분"
        currentPeople.text = "${data.currentPeople}/"
        maxPeople.text = "${data.maxPeople}"

        if (data.ploggingType.name == "DIRECT") {
            ploggingType.text = "선착순"
        } else {
            ploggingType.text = "승인제"
        }

        if (data.isAssigned) {
            IsAssigned.text = "승인완료"
        } else {
            IsAssigned.text = "승인대기"
        }
    }
}