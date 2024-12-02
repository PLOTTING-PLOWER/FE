package com.example.plotting_fe.myplogging.ui

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
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.example.plotting_fe.global.util.ClickUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingScheduledActivity : AppCompatActivity() {

    private lateinit var recyclerViewScheduledPlogging: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var adapter: MyPloggingScheduledAdapter

    // 데이터를 MutableList로 유지
    private val dataList: MutableList<MyPloggingScheduledResponse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplogging_scheduled)

        // UI 요소 초기화
        recyclerViewScheduledPlogging = findViewById(R.id.recyclerView_scheduled_plogging)
        backButton = findViewById(R.id.iv_back)

        // 뒤로 가기 버튼 이벤트 연결
        ClickUtil.onBackButtonClick(this, backButton)

        // RecyclerView 초기화
        recyclerViewScheduledPlogging.layoutManager = LinearLayoutManager(this)
        adapter = MyPloggingScheduledAdapter(this, dataList) { ploggingId ->
            cancelPlogging(ploggingId)
        }
        recyclerViewScheduledPlogging.adapter = adapter

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
                        Log.d("MyPloggingScheduled", "Received data: $data")

                        dataList.clear() // 이전 데이터 삭제
                        dataList.addAll(data)
                        adapter.notifyDataSetChanged()
                    } ?: run {
                        Log.e("MyPloggingScheduled", "Response body is null")
                        showToast("데이터가 없습니다.")
                    }
                } else {
                    Log.e("MyPloggingScheduled", "Error code: ${response.code()}")
                    showToast("데이터를 불러오는 데 실패했습니다. (${response.code()})")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<List<MyPloggingScheduledResponse>>>,
                t: Throwable
            ) {
                Log.e("MyPloggingScheduled", "API call failed: ${t.message}")
                showToast("서버 연결에 실패했습니다.")
            }
        })
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

                    // MutableList에서 데이터 제거
                    val position = dataList.indexOfFirst { it.ploggingId == ploggingId }
                    if (position != -1) {
                        dataList.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        adapter.notifyItemRangeChanged(position, dataList.size)
                    }
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
