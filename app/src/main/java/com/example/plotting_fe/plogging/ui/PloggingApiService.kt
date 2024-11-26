package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.content.Intent
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

                        //GetPlogging으로 이동
                        val intent = Intent(context, GetPloggings::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Plogging 생성에 실패했습니다: ${responseBody?.message ?: "알 수 없는 오류"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Plogging 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
        startDate: String,
        endDate: String,
        type: String,   // PloggingType을 String으로 변경
        spendTime: Long,
        startTime: String,   // PloggingType에서 String으로 바꿈
        maxPeople: Long,
        context: Context,
//    recyclerView: RecyclerView,
//    adapter: PloggingAdapter
    ) {
        val call: Call<ResponseTemplate<List<PloggingResponse>>> = apiService.findListByFilter(
            region, startDate, endDate, type, spendTime, startTime, maxPeople
        )
        call.enqueue(object : Callback<ResponseTemplate<List<PloggingResponse>>> {
            override fun onResponse(
                call: Call<ResponseTemplate<List<PloggingResponse>>>,
                response: Response<ResponseTemplate<List<PloggingResponse>>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == true) {

                        val ploggingList: List<PloggingResponse> =
                            responseBody.results ?: emptyList()
                        if (ploggingList.isNotEmpty()) {

                            val intent = Intent(context, GetPloggings::class.java)
                            for ((index, plogging) in ploggingList.withIndex()) {
                                intent.putExtra("ploggingId_$index", plogging.ploggingId)
                                intent.putExtra("ploggingTitle_$index", plogging.title)
                                intent.putExtra("ploggingCurrentPeople_$index", plogging.currentPeople)
                                intent.putExtra("ploggingMaxPeople_$index", plogging.maxPeople)
                                intent.putExtra("ploggingType_$index", plogging.ploggingType)
                                intent.putExtra("ploggingRecruitEndDate_$index", plogging.recruitEndDate)
                                intent.putExtra("ploggingStartTime_$index", plogging.startTime)
                                intent.putExtra("ploggingSpendTime_$index", plogging.spendTime)
                                intent.putExtra("ploggingStartLocation_$index", plogging.startLocation)
                            }
                            context.startActivity(intent)

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
                call: Call<ResponseTemplate<List<PloggingResponse>>>,
                t: Throwable
            ) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("filterPlogging", "filterPlogging_onFailure: ${t.message}")
            }
        })
    }

    fun getPloggingWithTitle(
        title: String,
        context: Context
    ) {
        val call: Call<ResponseTemplate<PloggingResponse>> =
            apiService.getPloggingWithTitle(title) // title 매개변수 사용

        call.enqueue(object : Callback<ResponseTemplate<PloggingResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<PloggingResponse>>,
                response: Response<ResponseTemplate<PloggingResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == true) {
                        Toast.makeText(context, "Plogging이 성공적으로 검색되었습니다.", Toast.LENGTH_SHORT)
                            .show()

                        // GetPlogging으로 이동
                        val intent = Intent(context, GetPloggings::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "Plogging 검색에 실패했습니다: ${responseBody?.message ?: "알 수 없는 오류"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Plogging 검색에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<PloggingResponse>>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}