package com.example.plotting_fe.global.application

import android.app.Application
import android.content.Context

class TokenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object{
        lateinit var appContext:Context
            private set

        private const val PREFS_NAME = "AuthPrefs"
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
        private const val FCM_TOKEN_KEY = "FCM_TOKEN"

        // 토큰 저장
        fun saveTokens(accessToken: String, refreshToken: String) {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(ACCESS_TOKEN_KEY, accessToken)
                putString(REFRESH_TOKEN_KEY, refreshToken)
                apply() // 최종적으로 저장
            }
        }

        // ACCESS_TOKEN 가져오기
        fun getAccessToken(): String? {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        }

        // REFRESH_TOKEN 가져오기
        fun getRefreshToken(): String? {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        }

        // FCM 토큰 저장
        fun saveFcmToken(fcmToken: String) {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(FCM_TOKEN_KEY, fcmToken)
                apply()
            }
        }

        // FCM 토큰 가져오기
        fun getFcmToken(): String? {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(FCM_TOKEN_KEY, null)
        }

        // 토큰 삭제 (로그아웃)
        fun clearTokens() {
            val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                remove(ACCESS_TOKEN_KEY)
                remove(REFRESH_TOKEN_KEY)
                remove(FCM_TOKEN_KEY)
                apply()
            }
        }

    }
}