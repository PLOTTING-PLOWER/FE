package com.example.plotting_fe.myplogging.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.RetrofitImpl
import com.example.plotting_fe.myplogging.dto.response.MyPloggingParticipatedResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.dto.response.MyPloggingSummaryResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPloggingHomeActivity : AppCompatActivity() {

    private lateinit var ivProfileImageUrl: ImageView  // 프로필 이미지
    private lateinit var tvNickname: TextView          // 유저 닉네임
    private lateinit var ploggingCount: TextView       // 플로깅 횟수
    private lateinit var ploggingTime: TextView        // 플로깅 시간
    private lateinit var tvBtnShowMore2: TextView      // 'Show More' 버튼 2
    private lateinit var tvBtnShowMore3: TextView      // 'Show More' 버튼 3
    private lateinit var tvBtnShowMoreAdd: TextView
    private lateinit var trophy : ImageView
    private lateinit var edit : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplogging_home)

        // UI 요소 초기화
        initViews()

        // 데이터 불러오기
        fetchData()

        fetchPloggingData_2()

        fetchPloggingData_4()
    }

    private fun initViews() {
        // UI 요소 연결
        ivProfileImageUrl = findViewById(R.id.IvProfileImageUrl)
        tvNickname = findViewById(R.id.tvNickname)
        ploggingCount = findViewById(R.id.ploggingCount)
        ploggingTime = findViewById(R.id.ploggingTime)
        tvBtnShowMore2 = findViewById(R.id.tv_btn_show_more2)
        tvBtnShowMore3 = findViewById(R.id.tv_btn_show_more3)
        tvBtnShowMoreAdd = findViewById(R.id.tv_btn_show_more_add)
        trophy = findViewById(R.id.trophy)
        edit = findViewById(R.id.edit)

        // 버튼 클릭 이벤트 설정
        edit.setOnClickListener {
            val intent = Intent(this, MyPloggingCreatedActivity::class.java)
            startActivity(intent)
        }

        tvBtnShowMore2.setOnClickListener {
            val intent = Intent(this, MyPloggingScheduledActivity::class.java)
            startActivity(intent)
        }

        tvBtnShowMore3.setOnClickListener {
            val intent = Intent(this, MyPloggingParticipatedActivity::class.java)
            startActivity(intent)
        }

        tvBtnShowMoreAdd.setOnClickListener{
            val intent = Intent(this, MyMonthlyPloggingActivity::class.java)
            startActivity(intent)
        }

        trophy.setOnClickListener {
            val intent = Intent(this, MyPloggingParticipatedActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchData() {
        val myPloggingController = RetrofitImpl.retrofit.create(MyPloggingController::class.java)
        val userId = 2L // 테스트용으로 사용자 ID 설정

        myPloggingController.getPloggingSummary(userId)
            .enqueue(object : Callback<ResponseTemplate<MyPloggingSummaryResponse>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<MyPloggingSummaryResponse>>,
                    response: Response<ResponseTemplate<MyPloggingSummaryResponse>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.results?.let { data ->
                            Log.d("MyPloggingHomeActivity", "Received data: $data")

                            // UI 업데이트
                            updateUI(data)

                        } ?: run {
                            Log.e("MyPloggingHomeActivity", "Response body is null")
                            showToast("데이터가 없습니다.")
                        }
                    } else {
                        Log.e("MyPloggingHomeActivity", "Error code: ${response.code()}")
                        showToast("데이터를 불러오는 데 실패했습니다. (${response.code()})")
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTemplate<MyPloggingSummaryResponse>>,
                    t: Throwable
                ) {
                    Log.e("MyPloggingHomeActivity", "API call failed: ${t.message}")
                    showToast("서버 연결에 실패했습니다.")
                }
            })
    }

    private fun fetchPloggingData_2() {
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
                            Log.d("MyPloggingScheduled", "Received data: $data")

                            // 리스트 중 첫 번째 항목 가져오기
                            val firstItem = data.firstOrNull()
                            if (firstItem != null) {
                                // UI 업데이트
                                updatePloggingView_2(firstItem)
                            }

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

    private fun fetchPloggingData_4() {
        val myPloggingController = RetrofitImpl.retrofit.create(MyPloggingController::class.java)
        val userId = 1L // 테스트용으로 사용자 ID 설정

        myPloggingController.getMyPloggingParticipated(userId)
            .enqueue(object : Callback<ResponseTemplate<List<MyPloggingParticipatedResponse>>> {
                override fun onResponse(
                    call: Call<ResponseTemplate<List<MyPloggingParticipatedResponse>>>,
                    response: Response<ResponseTemplate<List<MyPloggingParticipatedResponse>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.results?.let { data ->
                            Log.d("MyPloggingParticipated", "Received data: $data")

                            // 리스트 중 첫 번째 항목 가져오기
                            val firstItem = data.firstOrNull()
                            if (firstItem != null) {
                                // UI 업데이트
                                updatePloggingView_4(firstItem)
                            }

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

    private fun updatePloggingView_2(data: MyPloggingScheduledResponse) {
        val includeView = findViewById<View>(R.id.include_2)
        val viewBinder = ScheduledPloggingViewBinder(includeView)
        viewBinder.bind(data) // 데이터를 바인딩
    }

    private fun updatePloggingView_4(data: MyPloggingParticipatedResponse) {
        val includeView = findViewById<View>(R.id.include_4)
        val viewBinder = PloggingViewBinder(includeView)
        viewBinder.bind(data) // 데이터를 바인딩
    }

    private fun updateUI(response: MyPloggingSummaryResponse) {
        // 프로필 이미지 로드 (Glide 라이브러리 사용)
        Glide.with(this)
            .load(response.profileImageUrl)
            .into(ivProfileImageUrl)

        // 텍스트 뷰에 값 설정
        tvNickname.text = response.nickname
        ploggingCount.text = "${response.totalPloggingCount}회"
        ploggingTime.text = "${response.totalSpendTime}시간"
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}