package com.example.plotting_fe.home.ui

import android.util.Log
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.home.dto.response.CardResponse
import com.example.plotting_fe.home.dto.response.CardnewsResponseList
import com.example.plotting_fe.home.dto.response.HomeResponse
import com.example.plotting_fe.home.presentation.HomeController
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
                        // nullPointerException 터져서 임시 처리.
                        val plowerList =
                            homeResponse.plowerResponseList.plowerResponseList ?: emptyList()

                        listener.onHomeDataReceived(homeResponse)
//                        listener.onPloggingDataReceived(homeResponse.ploggingGetStarResponseList.ploggingGetStarListResponse)
                        if (response != null) {
                            listener.onPloggingDataReceived(homeResponse.ploggingGetStarResponseList.ploggingGetStarResponseList)
                        } else {
                            Log.d(
                                "onPloggingDataReceived",
                                "ploggingResponseList: " + homeResponse.ploggingGetStarResponseList.ploggingGetStarResponseList.toString()
                            );
                        }

                        listener.onPlowerDataReceived(plowerList)
                        listener.onUserDataReceiver(homeResponse.userNickname)

                        Log.d("HomeData", "homeResponse: " + homeResponse)

                        Log.d("onPlowerDataReceived", "plower" + plowerList)
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

    // 카드뉴스를 로드하는 메서드 추가
    fun loadCardnews(listener: CardnewsListener) {
        apiService.getCardnews().enqueue(object : Callback<ResponseTemplate<CardnewsResponseList>> {
            override fun onResponse(
                call: Call<ResponseTemplate<CardnewsResponseList>>,
                response: Response<ResponseTemplate<CardnewsResponseList>>
            ) {
                if (response.isSuccessful) {
                    val cardnewsResponseList = response.body()?.results
                    if (cardnewsResponseList != null) {
                        listener.onCardnewsReceived(cardnewsResponseList)
                    } else {
                        Log.d("HomeApiService", "Cardnews 데이터가 없습니다.")
                    }
                } else {
                    Log.d("HomeApiService", "Cardnews API 호출 실패: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<CardnewsResponseList>>,
                t: Throwable
            ) {
                Log.d("HomeApiService", "Cardnews API 호출 실패: ${t.message}")
            }
        })
    }

    fun loadCardnews(cardnewsId: Long, listener: CardsListener) {
        apiService.getCard(cardnewsId).enqueue(object : Callback<ResponseTemplate<CardResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<CardResponse>>,
                response: Response<ResponseTemplate<CardResponse>>
            ) {
                if (response.isSuccessful) {
                    val cardResponse = response.body()?.results
                    Log.d("cardnews", "API Response: ${response.body()}") // 전체 응답 본문 로깅

                    cardResponse?.let {
                        listener.onCardsReceived(listOf(it))
                    } ?: run {
                        Log.d("HomeApiService", "No card data received.")
                        listener.onCardsReceived(emptyList())
                    }
                } else {
                    // 응답 실패 시 로그 출력
                    Log.d(
                        "HomeApiService",
                        "Card API 호출 실패: ${response.code()} ${response.message()}"
                    )
                    listener.onCardsReceived(emptyList())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<CardResponse>>, t: Throwable) {
                Log.d("HomeApiService", "Cardnews API 호출 실패: ${t.message}")
                listener.onCardsReceived(emptyList())
            }
        })
    }
}