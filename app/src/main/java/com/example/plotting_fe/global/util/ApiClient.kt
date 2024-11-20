package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.NCPApiService
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }

    fun getPloggingController(): PloggingController {
        return getApiClient().create(PloggingController::class.java)
    }
}