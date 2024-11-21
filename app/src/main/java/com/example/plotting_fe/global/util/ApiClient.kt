package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.NCPApiService
import com.example.plotting_fe.plogging.presentation.PloggingController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    // 기존의 것
    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 네이버
    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }

    // 로그 확인 하기 위한 임의 확인용 메서드
    fun getApiClient2(interceptor: AppInterceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPloggingController(interceptor: AppInterceptor): PloggingController {
        return getApiClient2(interceptor).create(PloggingController::class.java)
    }

    // 로그 확인하기 위한 메서드
    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }
}
