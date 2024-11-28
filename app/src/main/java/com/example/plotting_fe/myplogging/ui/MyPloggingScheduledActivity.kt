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
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)

        myPloggingController.getMyPloggingScheduled().enqueue(object :
            Callback<ResponseTemplate<List<MyPloggingScheduledResponse>>> {
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
        adapter = MyPloggingScheduledAdapter(this,dataList) { ploggingId, userId ->
            // 취소 버튼 클릭 시 호출되는 메서드
            cancelPlogging(ploggingId)
        }
        recyclerViewScheduledPlogging.adapter = adapter
    }

    private fun cancelPlogging(ploggingId: Long) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)

        myPloggingController.reqeustCancel(ploggingId).enqueue(object :
            Callback<ResponseTemplate<Void>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<Void>>,
                    response: Response<ResponseTemplate<Void>>
                ) {
                    if (response.isSuccessful) {
                        showToast("플로깅이 취소되었습니다.")
                        // 취소 후 데이터를 갱신
                        fetchData()
                    } else {
                        showToast("취소에 실패했습니다. 다시 시도해 주세요.")
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTemplate<Void>>,
                    t: Throwable
                ) {
                    showToast("서버 연결에 실패했습니다.")
                }
            })
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

