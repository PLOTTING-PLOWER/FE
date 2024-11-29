package com.example.plotting_fe.home.ui

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R

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
            val contentText = alarm.content
            val date = alarm.date
            val spannablestring = SpannableString(contentText + date)

            // 날짜 부분 색상 변경
            spannablestring.setSpan(
                ForegroundColorSpan(0xFF888888.toInt()), // 회색
                contentText.length,
                contentText.length + date.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            contetntTextView.text = spannablestring

        }
    }
}