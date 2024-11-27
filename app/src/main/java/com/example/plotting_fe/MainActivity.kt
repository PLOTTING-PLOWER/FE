package com.example.plotting_fe

import android.media.session.MediaSession.Token
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.plotting_fe.databinding.AActivityMainBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.application.TokenApplication
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.FcmTokenUtil
import com.example.plotting_fe.user.presentation.AuthController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var _binding: AActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 시스템 바 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).post {
            val navController = findNavController(R.id.nav_host_fragment)
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        }

        // FCM 토큰 요청 및 서버 전송
        fetchFcmToken()
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // FCM 토큰
            val token = task.result
            Log.d("FCM", "FCM Token: $token")

            val savedToken = TokenApplication.getFcmToken()
            if(token!=savedToken){
                // 변경된 경우 서버로 토큰 전송
                FcmTokenUtil.sendTokenToServer(token)
                TokenApplication.saveFcmToken(token) // 새토큰 저장
            }else{
                Log.d("FCM", "Token has not changed")
            }
        }
    }


}