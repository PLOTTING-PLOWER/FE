package com.example.plotting_fe.home.ui

import android.util.Log
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.home.presentation.HomeController
import com.example.plotting_fe.plogging.dto.response.HomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeApiService {

    private val apiService: HomeController =
        ApiClient.getApiClient().create(HomeController::class.java)

    fun getHome(listener: HomeResponseListener) {
        apiService.getHome().enqueue(object : Callback<ResponseTemplate<HomeResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<HomeResponse>>,
                response: Response<ResponseTemplate<HomeResponse>>
            ) {
                if (response.isSuccessful) {
                    val homeResponse = response.body()?.results
                    if (homeResponse != null) {
                        // 각각의 데이터를 별도의 콜백 메서드로 전달
                        listener.onHomeDataReceived(homeResponse)
                        listener.onPloggingDataReceived(homeResponse.ploggingResponseList)
                        listener.onPlowerDataReceived(homeResponse.plowerResponseList)
                    } else {
                        Log.d("HomeApiService", "onResponse 실패: Body가 null입니다.")
                    }
                } else {
                    Log.d("HomeApiService", "onResponse 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<HomeResponse>>, t: Throwable) {
                Log.d("HomeApiService", "onFailure 에러: ${t.message}")
            }
        })
    }
}
