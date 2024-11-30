package com.example.plotting_fe

import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.plotting_fe.databinding.AActivityMainBinding
import com.example.plotting_fe.global.application.TokenApplication
import com.example.plotting_fe.global.util.FcmTokenUtil
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity
import com.example.plotting_fe.plogging.ui.PloggingMapActivity
import com.example.plotting_fe.utils.UIUtils.setupBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging


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

//            // FIXME
//            // TODO: 메인 화면은 여기가 아니라, home 도메인-> MainActivity 로 설정함.
//            // BottomNavigationView 클릭 이벤트 추가
//            
        // FCM 토큰 요청 및 서버 전송
        fetchFcmToken()

        // 안드로이드 13 이상일 경우 알림 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // 사용자가 권한을 허용했을 때
                println("알림 권한 허용됨")
            } else {
                // 사용자가 권한을 거부했을 때
                println("알림 권한 거부됨")
            }
        }

        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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
            if (token != savedToken) {
                // 변경된 경우 서버로 토큰 전송
                FcmTokenUtil.sendTokenToServer(token)
                TokenApplication.saveFcmToken(token) // 새토큰 저장
            } else {
                Log.d("FCM", "Token has not changed")
            }
        }
    }
}


