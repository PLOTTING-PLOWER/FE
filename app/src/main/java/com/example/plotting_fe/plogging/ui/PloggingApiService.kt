package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.AppInterceptor
import com.example.plotting_fe.plogging.dto.request.PloggingRequest
import com.example.plotting_fe.plogging.dto.response.PloggingListResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PloggingApiService {

    val interceptor = AppInterceptor()
    private val apiService: PloggingController =
        ApiClient.getApiClient().create(PloggingController::class.java)

    // TODO : 플로깅 만들기
    fun createPlogging(
        request: PloggingRequest,
        userId: Long,
        context: Context
    ) {
        Log.d("gogogo", "hi Plogging")

        val call: Call<ResponseTemplate<PloggingRequest>> = apiService.createPlogging(1L, request)
        call.enqueue(object : Callback<ResponseTemplate<PloggingRequest>> {

            override fun onResponse(
                call: Call<ResponseTemplate<PloggingRequest>>,
                response: Response<ResponseTemplate<PloggingRequest>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == true) {
                        Toast.makeText(context, "Plogging이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("gogogo", "Plogging_is_success! ");
                    } else {
                        Toast.makeText(
                            context,
                            "Plogging 생성에 실패했습니다: ${responseBody?.message ?: "알 수 없는 오류"}",
                            Toast.LENGTH_SHORT
                        ).show()
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

    // TODO : 플로깅 필터링
    fun filterPlogging(
        region: String,
        startDate: LocalDate,
        endDate: LocalDate,
        type: String,   // PloggingType을 String으로 변경
        spendTime: Long,
        startTime: LocalTime,   // PloggingType에서 String으로 바꿈
        maxPeople: Long,
        context: Context,
    ) {
        val call: Call<ResponseTemplate<PloggingListResponse>> = apiService.findListByFilter(
            region, startDate, endDate, type, spendTime, startTime, maxPeople
        )
        call.enqueue(object : Callback<ResponseTemplate<PloggingListResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<PloggingListResponse>>,
                response: Response<ResponseTemplate<PloggingListResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == true) {
                        val ploggingList = responseBody.results
//                        ploggingFilterAdapter.notifyDataSetChanged()  // 데이터를 갱신

                        Log.d("filterPlogging", "PloggingList_is_success: ${ploggingList}")
                        Toast.makeText(context, "Plogging 목록이 성공적으로 조회되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.d("filterPlogging", "failed! ${responseBody?.message}")
                    }
                } else {
                    Log.d("filterPlogging", "failed! server die ")
                    Toast.makeText(context, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<PloggingListResponse>>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("filterPlogging", "filterPlogging_onFailure: ${t.message}")
            }
        })
    }
}
