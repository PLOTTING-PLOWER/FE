package com.example.plotting_fe.global.application

import android.util.Log
import com.example.plotting_fe.global.util.FcmTokenUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String){
        super.onNewToken(token)
        Log.d("FCM","New token: $token")

        val savedToken = TokenApplication.getFcmToken()
        if(token!=savedToken){
            // 변경된 경우 서버로 토큰 전송
            FcmTokenUtil.sendTokenToServer(token)
            TokenApplication.saveFcmToken(token) // 새토큰 저장
        }else{
            Log.d("FCM", "Token has not changed")
        }
    }

    // 알림 메시지 수신 시 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // 필요하면 알림 처리 코드 추가
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Title: ${it.title}, Body: ${it.body}")
        }
    }
}