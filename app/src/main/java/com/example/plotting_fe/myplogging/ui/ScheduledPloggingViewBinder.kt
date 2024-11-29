package com.example.plotting_fe.myplogging.ui

import android.view.View
import android.widget.TextView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import java.text.SimpleDateFormat
import java.util.Locale

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

        val formattedTime = formatStartTime(data.startTime)
        startTime.text = formattedTime

        spendTime.text = "${(data.spendTime / 60)}H"
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
}