package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.NCPApiService
import com.example.plotting_fe.plogging.presentation.PloggingController
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    // Retrofit 인스턴스 생성 (HttpLoggingInterceptor 포함)
    fun getApiClient(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // 요청 및 응답 로그 출력
            .build()

        var gson= GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // NCPApiService 생성
    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }

    // PloggingController 생성
    fun getPloggingController(): PloggingController {
        return getApiClient().create(PloggingController::class.java)
    }
}
