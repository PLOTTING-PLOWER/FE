package com.example.plotting_fe.global.util

import android.content.Context
import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.MainApplication
import com.example.plotting_fe.global.NCPApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    fun getApiClient(): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()

                // SharedPreferences에서 토큰 가져오기
                val prefs = MainApplication.appContext.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("ACCESS_TOKEN", null)

                // 토큰이 존재하면 Authorization 헤더 추가
                if (!token.isNullOrEmpty()) {
                    request.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(request.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }
}