package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotting_fe.databinding.ActivityMyPloggingWaitingBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.WaitingPeople
import com.example.plotting_fe.myplogging.dto.response.MyPloggingWaitingResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingWaitingActivity : AppCompatActivity() {
    private lateinit var adapter: MyPloggingWaitingAdapter
    private var waitingUsers = mutableListOf<WaitingPeople>()

    private var _binding: ActivityMyPloggingWaitingBinding? = null
    private val binding get() = _binding!!

    var ploggingId = 4L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 ploggingId를 가져오기
        ploggingId = intent.getLongExtra("ploggingId", 4L)

        // RecyclerView 설정
        val recyclerView = binding.rvPlogging
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MyPloggingWaitingAdapter(
            items = waitingUsers,
            onAcceptClick = { waitingPeople -> approveRequest(waitingPeople) },
            onRejectClick = { waitingPeople -> rejectRequest(waitingPeople) }
        )

        recyclerView.adapter = adapter

        loadInfo(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MyPloggingCreatedActivity::class.java)
            startActivityForResult(intent, 0) // REQUEST_CODE는 임의의 정수 상수
        }
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
                        waitingUsers.clear()
                        waitingUsers.addAll(it.responses)
                        adapter.notifyDataSetChanged()
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

    private fun approveRequest(waitingPeople: WaitingPeople) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.reqeustApprove(ploggingId, waitingPeople.ploggingUserId)
            .enqueue(object : Callback<ResponseTemplate<String>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<String>>,
                    response: Response<ResponseTemplate<String>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()?.results
                        body?.let {
                            Toast.makeText(this@MyPloggingWaitingActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                        Log.d("post", "onResponse 성공: ${response.body().toString()}")

                        adapter.removeItem(waitingPeople)
                    } else {
                        Log.d("post", "onResponse 실패 + ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<String>>, t: Throwable) {
                    Log.d("post", "onFailure 에러: ${t.message.toString()}")
                }
            })
    }

    private fun rejectRequest(waitingPeople: WaitingPeople) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.requestReject(waitingPeople.ploggingUserId)
            .enqueue(object : Callback<ResponseTemplate<Void>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<Void>>,
                    response: Response<ResponseTemplate<Void>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MyPloggingWaitingActivity, "거절이 완료 되었습니다", Toast.LENGTH_SHORT).show()
                        Log.d("post", "onResponse 성공: ${response.body().toString()}")
                        adapter.removeItem(waitingPeople)
                    } else {
                        Log.d("post", "onResponse 실패 + ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                    Log.d("post", "onFailure 에러: ${t.message.toString()}")
                }
            })
    }
}