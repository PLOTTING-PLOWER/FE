package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.plogging.dto.request.PloggingRequest
import com.example.plotting_fe.plogging.presentation.PloggingController
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.AppInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PloggingApiService {

    val interceptor = AppInterceptor()
    private val apiService: PloggingController = ApiClient.getPloggingController(interceptor);

    fun createPlogging(
        request: PloggingRequest,
        userId: Long,
        context: Context
    ) {
        Log.d("gogogo", "hi Plogging")

        //TODO 여기서 터짐
        val call: Call<ResponseTemplate<PloggingRequest>> = apiService.createPlogging(userId, request)
        call.enqueue(object : Callback<ResponseTemplate<PloggingRequest>> {

            override fun onResponse(
                call: Call<ResponseTemplate<PloggingRequest>>,
                response: Response<ResponseTemplate<PloggingRequest>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == true) {
                        Toast.makeText(context, "Plogging이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("gogogo", "Plogging_is_success! ");
                    } else {
                        Toast.makeText(context, "Plogging 생성에 실패했습니다: ${responseBody?.message ?: "알 수 없는 오류"}", Toast.LENGTH_SHORT).show()
                        Log.d("gogogo", "Plogging_is_failed! ");
                    }
                } else {
                    Toast.makeText(context, "Plogging 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("gogogo", "Plogging_is_filed! server die ");
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<PloggingRequest>>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
