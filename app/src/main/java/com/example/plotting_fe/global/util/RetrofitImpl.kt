package com.example.plotting_fe.global.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.plotting_fe.BuildConfig

object RetrofitImpl {
    private const val URL = BuildConfig.BASE_URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}