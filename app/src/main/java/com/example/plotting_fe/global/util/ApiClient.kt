package com.example.plotting_fe.global.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.NCPApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL
    private var preferences: SharedPreferences? = null

    fun init(context: Context) {
        preferences = context.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
    }

    fun getApiClient(): Retrofit {
//        val accessToken = preferences?.getString("accessToken", "")
        val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmlja25hbWUiOiLsip3sip3snbQiLCJpYXQiOjE3MzI4MDMwMDIsImV4cCI6MTczMjgxMDIwMiwiaXNzIjoicGxvdHRpbmcifQ.GqlO-tPuFyTt6YL9WI1aBXsqCNvkwgaqGJ66epBVtmQ"

        Log.d("accessToken", accessToken.toString())

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(AppInterceptor(accessToken.toString())))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    class AppInterceptor(private val accessToken: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {

            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            proceed(newRequest)
            // proceed에서 응답이 나오는데 여기서 인증이 안됐으면 refresh token을 이용해서 access token 재발급 로직 등을 넣기
        }
    }
}