package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.NCPApiService
import com.example.plotting_fe.plogging.presentation.PloggingController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder

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

        // Gson 인스턴스 생성 (lenient 모드 활성화)
        val gson: Gson = GsonBuilder()
            .setLenient() // lenient 모드 활성화
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // OkHttpClient 적용
            .addConverterFactory(GsonConverterFactory.create(gson)) // lenient Gson 사용
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
