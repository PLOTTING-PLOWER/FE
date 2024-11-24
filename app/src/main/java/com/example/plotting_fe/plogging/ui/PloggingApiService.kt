package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.AppInterceptor
import com.example.plotting_fe.plogging.dto.request.PloggingRequest
import com.example.plotting_fe.plogging.dto.response.PloggingListResponse
import com.example.plotting_fe.plogging.dto.response.PloggingResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PloggingApiService {

//    val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmlja25hbWUiOiLsip3sip3snbQiLCJpYXQiOjE3MzI0Mzg4NzQsImV4cCI6MTczMjQ0NjA3NCwiaXNzIjoicGxvdHRpbmcifQ.B54hskmXVpcQam5qSWG8Fx4A3B17XWlqGv0HSWGskJM"
//    val userId = extractUserIdFromToken(token)

    val interceptor = AppInterceptor()
    private val apiService: PloggingController =
        ApiClient.getApiClient().create(PloggingController::class.java)

    // TODO : 플로깅 만들기
    fun createPlogging(
        request: PloggingRequest,
        context: Context
    ) {
        Log.d("gogogo", "hi Plogging")

        val call: Call<ResponseTemplate<PloggingRequest>> =
            apiService.createPlogging(request)
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
                        Log.d("gogogo", "Plogging_is_success! ")
                    } else {
                        Toast.makeText(
                            context,
                            "Plogging 생성에 실패했습니다: ${responseBody?.message ?: "알 수 없는 오류"}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("gogogo", "Plogging_is_failed! ")
                    }
                } else {
                    Toast.makeText(context, "Plogging 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("gogogo", "Plogging_is_failed! server die ")
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
        startTime: LocalDateTime,   // PloggingType에서 String으로 바꿈
        maxPeople: Long,
        context: Context,
        recyclerView: RecyclerView,
        adapter: PloggingAdapter
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

                        val ploggingList: List<PloggingResponse> = responseBody.results?.ploggingResponseList ?: emptyList()
                        if (ploggingList.isNotEmpty()) {
                            adapter.updatePloggingList(ploggingList)
                            recyclerView.adapter = adapter

                            Log.d("filterPlogging", "PloggingList_is_success: ${ploggingList}")

                            Toast.makeText(
                                context,
                                "Plogging 목록이 성공적으로 조회되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(context, "조건에 맞는 플로깅이 없습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("filterPlogging", "No matching plogging found.")
                        }
                    } else {
                        Log.d("filterPlogging", "failed! ${responseBody?.message}")
                        Toast.makeText(context, "Plogging 목록 조회 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("filterPlogging", "failed! server die ")
                    Toast.makeText(context, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<PloggingListResponse>>,
                t: Throwable
            ) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("filterPlogging", "filterPlogging_onFailure: ${t.message}")
            }
        })
    }

//    fun extractUserIdFromToken(token: String): Long? {
//        try {
//            // JWT 토큰을 디코딩합니다.
//            val decodedJWT: DecodedJWT = JWT.decode(token)
//
//            // 'sub' 클레임에서 userId를 추출합니다. (JWT에서 일반적으로 'sub' 필드는 사용자 ID를 의미)
//            val userId = decodedJWT.getClaim("sub").asLong()
//            return userId
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//    }
}
