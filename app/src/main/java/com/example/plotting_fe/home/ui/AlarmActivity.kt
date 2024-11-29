package com.example.plotting_fe.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.home.dto.Alarm
import com.example.plotting_fe.home.dto.response.AlarmListResponse
import com.example.plotting_fe.home.presentation.AlarmController
import com.example.plotting_fe.mypage.dto.Person
import com.example.plotting_fe.mypage.ui.PeopleAdapter
import com.example.plotting_fe.user.presentation.AuthController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlarmAdapter
    private val alarmList: MutableList<Alarm> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alarm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_alarm)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 뒤로가기 버튼 설정
        val backButton: ImageView = findViewById(R.id.iv_back)
        ClickUtil.onBackButtonClick(this, backButton)

        fetchAlarmList()

        adapter = AlarmAdapter(alarmList)
        recyclerView.adapter = adapter
    }

    private fun fetchAlarmList(){
        val alarmController = ApiClient.getApiClient().create(AlarmController::class.java)
        alarmController.getAlarmList().enqueue(object :
            Callback<ResponseTemplate<AlarmListResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<AlarmListResponse>>,
                response: Response<ResponseTemplate<AlarmListResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val responseBody = response.body()?.results
                    if(responseBody?.alarmList  != null){
                        updateRecyclerView(responseBody.alarmList )
                    }else {
                        Log.d("get", "onResponse 성공: responseBody  == null")
                        updateRecyclerView(emptyList())
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<AlarmListResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        });
    }

    private fun updateRecyclerView(newList: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(newList)
        adapter.notifyDataSetChanged()
    }
}