package com.example.plotting_fe.mypage.dto.response

import com.example.plotting_fe.mypage.dto.Person
import com.google.gson.annotations.SerializedName

data class MyUserStarListResponse (
    @SerializedName("responses")
    val responses: List<Person>
)