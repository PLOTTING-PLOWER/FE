package com.example.plotting_fe.user.dto.request

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("accessToken")
    val accessToken: String
)