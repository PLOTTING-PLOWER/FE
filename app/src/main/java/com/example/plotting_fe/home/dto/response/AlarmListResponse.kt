package com.example.plotting_fe.home.dto.response

import com.example.plotting_fe.home.dto.Alarm
import com.google.gson.annotations.SerializedName

data class AlarmListResponse (
    val alarmList: List<Alarm>
){
}