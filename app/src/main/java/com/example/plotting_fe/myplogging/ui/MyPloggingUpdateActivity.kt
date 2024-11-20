package com.example.plotting_fe.myplogging.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.plotting_fe.databinding.ActivityMyPloggingUpdateBinding
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

        binding.startDate.setOnClickListener {
            showDatePickerDialog(true)
        }

        binding.endDate.setOnClickListener() {
            showDatePickerDialog(false)
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, MyPloggingUpdate2Activity::class.java)
            intent.putExtra("ploggingId", ploggingId)
            intent.putExtra("maxPeople", binding.inputParticipantNum.text.toString())
            intent.putExtra("recruitStartTime", binding.startDate.text.toString())
            intent.putExtra("recruitEndTime", binding.endDate.text.toString())
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
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
}