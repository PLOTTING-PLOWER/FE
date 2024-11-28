package com.example.plotting_fe.global.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.plotting_fe.R
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

//        remoteMessage.notification?.let {
//            Log.d("FCM", "Notification Title: ${it.title}, Body: ${it.body}")
//        }

        // Notification Payload 확인
        remoteMessage.notification?.let {
            val title = it.title ?: "Default Title"
            val body = it.body ?: "Default Body"
            showNotification(title, body)
        }

    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 채널 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "FCM_CHANNEL",
                "FCM Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "FCM_CHANNEL")
            .setSmallIcon(R.drawable.ic_notification) // 알림 아이콘? 아니면 ,,, 로고?
            .setContentTitle(title) // 알림 제목
            .setContentText(body) // 알림 본문
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}