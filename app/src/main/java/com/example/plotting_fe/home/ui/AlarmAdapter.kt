package com.example.plotting_fe.home.ui

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.home.dto.Alarm
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AlarmAdapter(
    private val alarmList: List<Alarm>
) :RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }


    override fun getItemCount(): Int = alarmList.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int,) {
        holder.bind(alarmList[position])
    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contetntTextView: TextView = itemView.findViewById(R.id.tv_content)

        fun bind(alarm: Alarm){

            // 텍스트 날짜와 결합
            val contentText = alarm.content+"  "
            val createdDate = alarm.createdDate

            // 날짜 형식 변환
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val alarmDateTime = LocalDateTime.parse(createdDate, formatter)
            val now = LocalDateTime.now()
            Log.d("AlarmAdapter", "createdDate: "+ createdDate + "/ now: " + now)

            // 시간 차이 계산
            val duration = Duration.between(alarmDateTime, now).abs()
            val minutesBetween = duration.toMinutes()
            val hoursBetween = duration.toHours()
            val daysBetween = ChronoUnit.DAYS.between(alarmDateTime.toLocalDate(), now.toLocalDate())
            val monthsBetween = ChronoUnit.MONTHS.between(alarmDateTime.toLocalDate(), now.toLocalDate())

            // 표시할 시간 텍스트 결정
            val dateText = when {
                minutesBetween < 60 -> "${minutesBetween}분 전" // 1시간 미만
                hoursBetween < 24 && daysBetween == 0L -> "${hoursBetween}시간 전" // 오늘
                daysBetween < 7 -> "${daysBetween}일 전" // 이번 주
                daysBetween < 30 -> "${daysBetween / 7}주 전" // 이번 달
                monthsBetween < 12 -> "${monthsBetween}달 전" // 이번 년도
                else -> alarmDateTime.toLocalDate().toString() // 연도를 넘으면 날짜 그대로 표시
            }

            val spannablestring = SpannableString(contentText + dateText)

            // 날짜 부분 색상 변경
            spannablestring.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.gray) ), // 회색
                contentText.length,
                contentText.length + dateText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            contetntTextView.text = spannablestring

        }
    }
}