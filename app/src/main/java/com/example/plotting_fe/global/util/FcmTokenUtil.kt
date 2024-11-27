package com.example.plotting_fe.global.util

import android.util.Log
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.user.presentation.AuthController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FcmTokenUtil {
    fun sendTokenToServer(token: String) {
        val authController: AuthController = ApiClient.getApiClient().create(AuthController::class.java)
        val tokenData = mapOf("token" to token)

        authController.sendFcmToken(tokenData).enqueue(object : Callback<ResponseTemplate<Void>> {
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