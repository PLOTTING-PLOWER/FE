package com.example.plotting_fe.global.util

import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.global.application.TokenApplication
import com.example.plotting_fe.global.NCPApiService
import com.example.plotting_fe.user.presentation.AuthController
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Route
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.BASE_URL
//    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getApiClient(): Retrofit {

        val client = OkHttpClient.Builder()
            // Access Token 처리
            .addInterceptor(AuthInterceptor())
            // Refresh Token 처리
            .authenticator(TokenAuthenticator())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()

            // Access Token 추가
            val accessToken = TokenApplication.getAccessToken()
            if (!accessToken.isNullOrEmpty()) {
                request.addHeader("Authorization", "Bearer $accessToken")
            }

            val response = chain.proceed(request.build())

            // 401 Unauthorized 감지
            if (response.code == 401) {
                LogoutUtil.handleLogout()
            }
            return response
        }
    }

    private class TokenAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            // Refresh Token 가져오기
            val refreshToken = TokenApplication.getRefreshToken()

            if (!refreshToken.isNullOrEmpty()) {
                // 새 Access Token 요청
                val newAccessToken = getNewAccessToken(refreshToken)

                if (newAccessToken != null) {
                    // 새 Access Token 저장
                    TokenApplication.saveTokens(newAccessToken, refreshToken)

                    // 원래 요청 재실행
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    LogoutUtil.handleLogout() // Refresh Token도 만료
                }
            }else{
                LogoutUtil.handleLogout() // Refresh Token 없을 때
            }

            return null
        }

        private fun getNewAccessToken(refreshToken: String): String? {
            return try {
                // AuthController 생성
                val authService = getApiClient().create(AuthController::class.java)

                // Refresh Token 요청
                val refreshResponse = authService.refreshAccessToken(refreshToken).execute()

                if (refreshResponse.isSuccessful) {
                    refreshResponse.body()?.results // 새 Access Token 반환
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun getNCPApiService(): NCPApiService {
        return getApiClient().create(NCPApiService::class.java)
    }
}