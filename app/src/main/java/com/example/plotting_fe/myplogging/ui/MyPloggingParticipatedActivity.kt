package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.response.MyPloggingParticipatedResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingParticipatedActivity : AppCompatActivity() {

    private lateinit var recyclerViewParticipatedPlogging: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var adapter: MyPloggingParticipatedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplogging_participated)

        // UI 요소 초기화
        initViews()

        // 데이터 불러오기
        fetchData()
    }

    private fun initViews() {
        recyclerViewParticipatedPlogging = findViewById(R.id.recyclerView_participated_plogging)
        backButton = findViewById(R.id.iv_back)

        backButton.setOnClickListener {
            navigateToHome()
        }

        recyclerViewParticipatedPlogging.layoutManager = LinearLayoutManager(this)
    }

    private fun navigateToHome() {
        val intent = Intent(this, MyPloggingHomeActivity::class.java)
        startActivity(intent)
        finish() // 현재 Activity 종료
    }

    private fun fetchData() {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)

        myPloggingController.getMyPloggingParticipated()
            .enqueue(object : Callback<ResponseTemplate<List<MyPloggingParticipatedResponse>>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<List<MyPloggingParticipatedResponse>>>,
                    response: Response<ResponseTemplate<List<MyPloggingParticipatedResponse>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.results?.let { data ->
                            Log.d("MyPloggingParticipated", "Received data: $data")
                            setupRecyclerView(data)

                        } ?: run {
                            Log.e("MyPloggingParticipated", "Response body is null")
                            showToast("데이터가 없습니다.")
                        }
                    } else {
                        Log.e("MyPloggingParticipated", "Error code: ${response.code()}")
                        showToast("데이터를 불러오는 데 실패했습니다. (${response.code()})")
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTemplate<List<MyPloggingParticipatedResponse>>>,
                    t: Throwable
                ) {
                    Log.e("MyPloggingParticipated", "API call failed: ${t.message}")
                    showToast("서버 연결에 실패했습니다.")
                }
            })
    }

    private fun setupRecyclerView(dataList: List<MyPloggingParticipatedResponse>) {
        adapter = MyPloggingParticipatedAdapter(this,dataList)
        recyclerViewParticipatedPlogging.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
