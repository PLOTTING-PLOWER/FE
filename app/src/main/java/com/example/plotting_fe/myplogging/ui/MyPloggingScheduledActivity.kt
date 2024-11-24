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
import com.example.plotting_fe.global.util.RetrofitImpl
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingScheduledActivity : AppCompatActivity() {

    private lateinit var recyclerViewScheduledPlogging: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var adapter: MyPloggingScheduledAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplogging_scheduled)

        // UI 요소 초기화
        recyclerViewScheduledPlogging = findViewById(R.id.recyclerView_scheduled_plogging)
        backButton = findViewById(R.id.iv_back)

        // 뒤로가기 버튼 클릭 이벤트 설정
        backButton.setOnClickListener {
            val intent = Intent(this, MyPloggingHomeActivity::class.java)
            startActivity(intent) // MyPloggingHomeActivity로 이동
            finish() // 현재 Activity 종료
        }

        // RecyclerView 초기화
        recyclerViewScheduledPlogging.layoutManager = LinearLayoutManager(this)

        // 데이터 불러오기
        fetchData()
    }

    private fun fetchData() {
        val myPloggingController = RetrofitImpl.retrofit.create(MyPloggingController::class.java)
        val userId = 1L // 테스트용으로 사용자 ID 설정

        myPloggingController.getMyPloggingScheduled(userId)
            .enqueue(object : Callback<ResponseTemplate<List<MyPloggingScheduledResponse>>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<List<MyPloggingScheduledResponse>>>,
                    response: Response<ResponseTemplate<List<MyPloggingScheduledResponse>>>
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
                    call: Call<ResponseTemplate<List<MyPloggingScheduledResponse>>>,
                    t: Throwable
                ) {
                    Log.e("MyPloggingParticipated", "API call failed: ${t.message}")
                    showToast("서버 연결에 실패했습니다.")
                }
            })
    }

    private fun setupRecyclerView(dataList: List<MyPloggingScheduledResponse>) {
        adapter = MyPloggingScheduledAdapter(dataList)
        recyclerViewScheduledPlogging.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

