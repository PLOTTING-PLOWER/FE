package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingCreatedBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.PloggingData
import com.example.plotting_fe.myplogging.dto.response.MyPloggingCreatedResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.example.plotting_fe.plogging.ui.PloggingMakeActivity1
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

        binding.ivAdd.setOnClickListener() {
            val intent = Intent(this, PloggingMakeActivity1::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadInfo(view: View) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.getMyPloggingCreated().enqueue(object :
            Callback<ResponseTemplate<MyPloggingCreatedResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPloggingCreatedResponse>>,
                response: Response<ResponseTemplate<MyPloggingCreatedResponse>>,
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공: " + response.body().toString())

                    val body = response.body()?.results?.myPloggings

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

                        val adapter = MyPloggingCreatedAdapter(recrutingPlogging, { ploggingId ->
                            deletePlogging(ploggingId) // 삭제 요청 메서드 호출
                        }, this@MyPloggingCreatedActivity)
                        recyclerView.adapter = adapter

                        // RecyclerView 설정
                        val completeRecyclerView = binding.rvCompletePlogging
                        completeRecyclerView.layoutManager = LinearLayoutManager(view.context)

                        val completeAdapter = MyCompletePloggingCreatedAdapter(completePlogging)
                        completeRecyclerView.adapter = completeAdapter
                    }
                } else {
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<MyPloggingCreatedResponse>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun deletePlogging(ploggingId: Long) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.deleteMyPlogging(ploggingId).enqueue(object : Callback<ResponseTemplate<Void>> {
            override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                if (response.isSuccessful) {
                    Log.d("post", "삭제 성공: $ploggingId")
                    Toast.makeText(this@MyPloggingCreatedActivity, "해당 플로깅이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    loadInfo(binding.root) // 필요에 따라 다시 로드
                } else {
                    Log.d("post", "삭제 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.d("post", "삭제 실패: ${t.message}")
            }
        })
    }
}
