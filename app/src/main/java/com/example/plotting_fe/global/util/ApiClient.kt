package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.TokenApplication
import com.example.plotting_fe.global.NCPApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL
//    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getApiClient(): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()

                // 토큰 가져오기
            //    val token = TokenApplication.getAccessToken()
                val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmlja25hbWUiOiLsip3sip3snbQiLCJpYXQiOjE3MzI3NjMxNzYsImV4cCI6MTczMjc3MDM3NiwiaXNzIjoicGxvdHRpbmcifQ.hCuWL8w5e9HRiaQcF_a3i4lbXUXBmP42p0F3OLIbT0M"


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