package com.example.plotting_fe.home.ui

import android.util.Log
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.home.dto.response.CardResponseList
import com.example.plotting_fe.home.dto.response.CardnewsResponseList
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

    fun loadCards(cardId: Long, listener: CardsListener) {
        apiService.getCard(cardId).enqueue(object : Callback<ResponseTemplate<CardResponseList>> {
            override fun onResponse(
                call: Call<ResponseTemplate<CardResponseList>>,
                response: Response<ResponseTemplate<CardResponseList>>
            ) {
                if (response.isSuccessful) {
                    val cardResponseList = response.body()?.results?.cardResponseList
                    if (cardResponseList != null) {
                        // 카드 리스트 디버깅 로그
                        Log.d("cardnews", "cardnews result: $cardResponseList")

                        // 카드 ID로 필터링
                        val selectedCard = cardResponseList.find { it.cardnewsId == cardId }

                        if (selectedCard != null) {
                            // 카드 ID로 필터링된 데이터를 onCardsReceived로 전달
                            listener.onCardsReceived(listOf(selectedCard))  // 리스트로 전달
                        } else {
                            Log.d("HomeApiService", "해당 카드 뉴스를 찾을 수 없습니다.")
                            listener.onCardsReceived(emptyList())  // 빈 리스트 전달
                        }
                    } else {
                        Log.d("HomeApiService", "Card 데이터가 없습니다.")
                        listener.onCardsReceived(emptyList())  // 빈 리스트 전달
                    }
                } else {
                    Log.d("HomeApiService", "Card API 호출 실패: ${response.code()}")
                    listener.onCardsReceived(emptyList())  // 빈 리스트 전달
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<CardResponseList>>, t: Throwable) {
                Log.d("HomeApiService", "Cardnews API 호출 실패: ${t.message}")
                listener.onCardsReceived(emptyList())
            }
        })
    }
}
