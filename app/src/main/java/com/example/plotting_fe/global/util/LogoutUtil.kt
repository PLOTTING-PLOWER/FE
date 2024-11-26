package com.example.plotting_fe.global.util

import android.content.Intent
import com.example.plotting_fe.WelcomeActivity
import com.example.plotting_fe.global.application.TokenApplication

object LogoutUtil {
    fun handleLogout() {
        // 토큰 삭제
        TokenApplication.clearTokens()
        // 로그인 화면으로 이동
        val intent = Intent(TokenApplication.appContext, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        TokenApplication.appContext.startActivity(intent)

    }
}