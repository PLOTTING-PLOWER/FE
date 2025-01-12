package com.example.plotting_fe.myplogging.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.plotting_fe.R
import com.example.plotting_fe.databinding.ActivityMyPloggingUpdateBinding
import com.example.plotting_fe.global.util.ClickUtil
import java.util.Calendar

class MyPloggingUpdateActivity : AppCompatActivity() {
    private var _binding: ActivityMyPloggingUpdateBinding? = null
    private val binding get() = _binding!!

    private var ploggingId = 1L
    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyPloggingUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resetTimeButtons()

        ploggingId = intent.getLongExtra("ploggingId", 1L)
        val ploggingType = intent.getStringExtra("ploggingType")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val startTime = intent.getStringExtra("startTime")
        val spendTime = intent.getLongExtra("spendTime", 0)
        val maxPeople = intent.getLongExtra("maxPeople", 0)
        val recruitStartDate = intent.getStringExtra("recruitStartDate")
        val recruitEndDate = intent.getStringExtra("recruitEndDate")

        binding.inputParticipantNum.setText(maxPeople.toString())
        binding.startDate.setText(recruitStartDate)
        binding.endDate.setText(recruitEndDate)

        if (ploggingType == "DIRECT") {
            binding.btnTime.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main))
            binding.nameTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.detailsTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.detailsTextView2.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.btnApproval.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main))
            binding.tvApprove.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tvApproveDetails.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.startDate.setOnClickListener {
            showDatePickerDialog(true)
        }

        binding.endDate.setOnClickListener() {
            showDatePickerDialog(false)
        }

        binding.btnNext.setOnClickListener {
            binding.btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main))

            Log.d("post", "maxPeople: " + binding.inputParticipantNum.text + "recruitStartDate: " + binding.startDate.text + "recruitEndDate: " + binding.endDate.text)

            val intent = Intent(this, MyPloggingUpdate2Activity::class.java)
            intent.putExtra("ploggingId", ploggingId)
            intent.putExtra("maxPeople", binding.inputParticipantNum.text.toString().toLong())
            intent.putExtra("recruitStartDate", binding.startDate.text.toString())
            intent.putExtra("recruitEndDate", binding.endDate.text.toString())
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            intent.putExtra("startTime", startTime)
            intent.putExtra("spendTime", spendTime)
            startActivity(intent)
            finish()
        }

        ClickUtil.onBackButtonClick(this, binding.btnBack)
    }

    private fun showDatePickerDialog(isStart: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            this,
            { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                selectedDate =
                    selectedYear.toString() + "-" + (selectedMonth + 1) + "-" + selectedDay

                if (isStart) {
                    binding.startDate.setText(selectedDate)
                } else {
                    binding.endDate.setText(selectedDate)
                }
            }, year, month, day
        )
        datePickerDialog.show()
    }
    private fun resetTimeButtons() {
        binding.btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray))
        binding.btnTime.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray))
        binding.btnApproval.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray))
    }
}