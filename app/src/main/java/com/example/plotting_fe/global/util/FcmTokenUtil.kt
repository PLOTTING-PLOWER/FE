package com.example.plotting_fe.global.util

import android.util.Log
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.request.AlarmRequest
import com.example.plotting_fe.home.presentation.AlarmController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FcmTokenUtil {
    fun sendTokenToServer(token: String) {
        val authController: AlarmController = ApiClient.getApiClient().create(AlarmController::class.java)

        authController.sendFcmToken(AlarmRequest(token = token)).enqueue(object : Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>
            ) {
                if(response.isSuccessful){
                    Log.d("FCM", "FCM token updated on server successfully.")
                }else{
                    Log.e("FCM", "Failed to update FCM token: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.e("FCM", "Error sending FCM token: ${t.message}")
            }
        });

        Log.d("FCM", "Sending token to server: $token")
    }
}