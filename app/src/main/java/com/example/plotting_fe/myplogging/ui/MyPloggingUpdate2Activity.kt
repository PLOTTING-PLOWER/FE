package com.example.plotting_fe.myplogging.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.plotting_fe.R
import com.example.plotting_fe.databinding.ActivityMyPloggingUpdate2Binding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.request.MyPloggingUpdateRequest
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MyPloggingUpdate2Activity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingUpdate2Binding? = null
    private val binding get() = _binding!!

    var ploggingId : Long = 1L
    var spendTime : Long = 0

    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingUpdate2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editStartDate.setOnClickListener() {
            showDatePickerDialog()
        }

        binding.editStartTime.setOnClickListener {
            showTimePickerDialog()
        }

        // 예상 소요 시간 입력 { hh:mm 형태로 입력 }
        binding.buttonDuringTime.setOnClickListener(View.OnClickListener {
            binding.inputDuringTime.setVisibility(View.VISIBLE)
            binding.buttonDuringTime.setBackgroundResource(R.drawable.shape_edittext_main_round)
            binding.buttonDuringTime.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.editDuringTimeFree.setBackgroundResource(R.drawable.shape_edittext_light_gray)
            binding.editDuringTimeFree.setTextColor(resources.getColor(R.color.gray))
        })

        binding.editDuringTimeFree.setOnClickListener(View.OnClickListener {
            binding.inputDuringTime.setVisibility(View.GONE)
            binding.editDuringTimeFree.setBackgroundResource(R.drawable.shape_edittext_main_round)
            binding.editDuringTimeFree.setTextColor(resources.getColor(R.color.white))

            binding.buttonDuringTime.setBackgroundResource(R.drawable.shape_edittext_light_gray)
            binding.buttonDuringTime.setTextColor(resources.getColor(R.color.gray))
        })

        ploggingId = intent.getLongExtra("ploggingId", 1L)
        val maxPeople = intent.getStringExtra("maxPeople")
        var recruitStartTime = intent.getStringExtra("recruitStartTime")
        var recruitEndTime = intent.getStringExtra("recruitEndTime")

        var request = MyPloggingUpdateRequest(
            title = binding.editName.text.toString(),
            content = binding.editIntro.text.toString(),
            maxPeople = maxPeople!!.toLong(),
            recruitStartTime = recruitStartTime.toString(),
            recruitEndTime = recruitEndTime.toString(),
            startTime = binding.editStartDate.toString() + " " + binding.editStartTime.toString(),
            spendTime = spendTime
        )

        binding.btnNext.setOnClickListener {
            if (binding.inputDuringTime.visibility == View.VISIBLE) {
                spendTime = binding.inputDuringTime.text.toString().toLong()
            }

            request.spendTime = spendTime
            postData(request)
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun postData(request: MyPloggingUpdateRequest) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.updateMyPlogging(ploggingId, request).enqueue(object :
            Callback<ResponseTemplate<MyPloggingUpdateRequest>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPloggingUpdateRequest>>,
                response: Response<ResponseTemplate<MyPloggingUpdateRequest>>,
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공: " + response.body().toString())
                } else {
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }
            override fun onFailure(
                call: Call<ResponseTemplate<MyPloggingUpdateRequest>>,
                t: Throwable
            ) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            this,
            { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate =
                    selectedYear.toString() + "-" + (selectedMonth + 1) + "-" + selectedDay
                binding.editStartDate.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // 선택한 날짜와 시간을 EditText에 설정합니다.
                val selectedDate = String.format("%02d:%02d:00", hourOfDay, minute)
                binding.editStartTime.setText(selectedDate)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}