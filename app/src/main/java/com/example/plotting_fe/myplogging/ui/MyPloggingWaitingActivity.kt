package com.example.plotting_fe.myplogging.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingWaitingBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.MyPloggingWaitingResponse
import com.example.plotting_fe.myplogging.dto.WaitingPeople
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingWaitingActivity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingWaitingBinding? = null
    private val binding get() = _binding!!

    val ploggingId = 3L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        val recyclerView = binding.rvPlogging
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 더미 데이터 설정
        val dummyData = listOf(
            WaitingPeople(1, "슝슝이", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
            WaitingPeople(2, "피치", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
            WaitingPeople(3, "버즈", "https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png"),
        )

        val adapter = MyPloggingWaitingAdapter(dummyData)
        recyclerView.adapter = adapter

        loadInfo(binding.root)
    }

    private fun loadInfo(view: View) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.getWaitingUsers(ploggingId).enqueue(object :
            Callback<ResponseTemplate<MyPloggingWaitingResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPloggingWaitingResponse>>,
                response: Response<ResponseTemplate<MyPloggingWaitingResponse>>,
            ) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    Log.d("post", "onResponse 성공: " + response.body().toString())

                    val body = response.body()?.results

                    // body에 있는 데이터를 화면에 표시
                    body?.let {
                        // RecyclerView 설정
                        val recyclerView = binding.rvPlogging
                        recyclerView.layoutManager = LinearLayoutManager(view.context)

                        // body에 있는 데이터를 RecyclerView에 표시
                        val adapter = MyPloggingWaitingAdapter(it.responses)
                        recyclerView.adapter = adapter
                    }

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<MyPloggingWaitingResponse>>,
                t: Throwable
            ) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }
}