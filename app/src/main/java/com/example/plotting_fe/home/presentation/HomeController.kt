package com.example.plotting_fe.home.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.response.CardnewsResponseList
import retrofit2.Call
import retrofit2.http.GET

interface HomeController {

    // 카드뉴스 리스트 조회
    @GET("/cardnews")
    fun getCardnews(): Call<ResponseTemplate<CardnewsResponseList>>

//    // 카드뉴스 상세 조회
//    @GET("/cardnews/{cardnewsId}")
//    fun getCard(
//        @Path("cardnewsId") cardnewsId: Long
//    ): Call<ResponseTemplate<CardResponse>>
}