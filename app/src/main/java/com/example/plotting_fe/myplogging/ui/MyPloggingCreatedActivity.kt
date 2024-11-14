package com.example.plotting_fe.myplogging.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingCreatedBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.PloggingData
import com.example.plotting_fe.myplogging.dto.response.MyPloggingCreatedResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingCreatedActivity : AppCompatActivity() {

    private var _binding: ActivityMyPloggingCreatedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingCreatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInfo(binding.root)
    }

    private fun loadInfo(view: View) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.getMyPloggingCreated(1).enqueue(object :
            Callback<ResponseTemplate<MyPloggingCreatedResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPloggingCreatedResponse>>,
                response: Response<ResponseTemplate<MyPloggingCreatedResponse>>,
            ) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    Log.d("post", "onResponse 성공: " + response.body().toString())

                    val body = response.body()?.results?.myPloggings

                    // body에 있는 데이터를 화면에 표시
                    body?.let { ploggings ->
                        val recrutingPlogging = mutableListOf<PloggingData>()
                        val completePlogging = mutableListOf<PloggingData>()

                        for (plogging in ploggings) {
                            if (plogging.isRecruiting) {
                                recrutingPlogging.add(plogging)
                            } else {
                                completePlogging.add(plogging)
                            }
                        }

                        // RecyclerView 설정
                        val recyclerView = binding.rvPlogging
                        recyclerView.layoutManager = LinearLayoutManager(view.context)

                        val adapter = MyPloggingCreatedAdapter(recrutingPlogging)
                        recyclerView.adapter = adapter

                        // RecyclerView 설정
                        val completeRecyclerView = binding.rvCompletePlogging
                        completeRecyclerView.layoutManager = LinearLayoutManager(view.context)

                        val completeAdapter = MyCompletePloggingCreatedAdapter(completePlogging)
                        completeRecyclerView.adapter = completeAdapter
                    }
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<MyPloggingCreatedResponse>>,
                t: Throwable
            ) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }
}