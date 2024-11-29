package com.example.plotting_fe.global.util

import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            // 인증이 필요 없는 경우 헤더를 추가하지 않음
            .build()

        return chain.proceed(request)
    }
}