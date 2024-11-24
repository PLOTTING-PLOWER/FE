package com.example.plotting_fe.home.presentation

import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.home.dto.response.CardResponse
import com.example.plotting_fe.home.dto.response.CardResponseList
import com.example.plotting_fe.home.dto.response.CardnewsResponseList
import com.example.plotting_fe.plogging.dto.response.HomeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeController {

    // 카드뉴스 리스트 조회
    @GET("/cardnews")
    fun getCardnews(): Call<ResponseTemplate<CardnewsResponseList>>

    // 카드뉴스 상세 조회
    @GET("/cardnews/{cardnewsId}")
    fun getCard(
        @Path("cardnewsId") cardnewsId: Long
    ): Call<ResponseTemplate<CardResponseList>>

    // 홈 조회
    @GET("/ploggings/home")
    fun getHome(
    ): Call<ResponseTemplate<HomeResponse>>
}