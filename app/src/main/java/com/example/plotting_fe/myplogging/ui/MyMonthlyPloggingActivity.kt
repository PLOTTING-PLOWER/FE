package com.example.plotting_fe.myplogging.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.plotting_fe.R
import com.example.plotting_fe.databinding.ActivityMyMonthlyPloggingBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.myplogging.dto.response.MonthResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MyMonthlyPloggingActivity : AppCompatActivity() {

    private var _binding: ActivityMyMonthlyPloggingBinding? = null
    private val binding get() = _binding!!
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyMonthlyPloggingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.barChart

        // CustomRenderer 설정
        barChart.renderer = RoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )

        setupBarChart()
        updateMonthLabels()

        loadInfo()

        ClickUtil.onBackButtonClick(this, binding.btnBack)
    }

    private fun loadInfo() {
        // API 호출
        val api = ApiClient.getApiClient().create(MyPloggingController::class.java)
        api.getMyMonthlyPlogging().enqueue(object : Callback<ResponseTemplate<MonthResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MonthResponse>>,
                response: Response<ResponseTemplate<MonthResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공: " + response.body().toString())
                    val monthResponses = response.body()?.results?.responses ?: emptyList()
                    setChartData(monthResponses)
                } else {
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<MonthResponse>>, t: Throwable) {
                Toast.makeText(
                    this@MyMonthlyPloggingActivity,
                    "데이터를 불러오는 데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupBarChart() {
        barChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawBorders(false)
            setDrawGridBackground(false)

            // X축 설정
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            // Y축 설정
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                gridColor = ContextCompat.getColor(context, R.color.light_gray)
                gridLineWidth = 0.5f
                axisMinimum = 0f
                setDrawLabels(false)
            }

            axisRight.isEnabled = false

            // 터치 비활성화
            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)

            // 여백 설정
            setExtraOffsets(20f, 0f, 20f, 0f)
        }
    }

    private fun setChartData(monthResponses: List<MonthResponse.MonthData>) {
        val entries = ArrayList<BarEntry>()

        // 현재 날짜 기준으로 현재 월과 그에 따른 월 계산
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // 현재 월 (0부터 시작)

        // 2개월 전, 현재, 2개월 후 월 계산
        val monthOffsets = listOf(-2, -1, 0, 1, 2) // -2개월, -1개월, 현재, +1개월, +2개월

        var currentMonthData: MonthResponse.MonthData? = null // 이번 달 데이터 저장

        for (offset in monthOffsets) {
            val monthToCheck = (currentMonth + offset + 12) % 12 // 월 계산 (12로 나눈 나머지로 보정)

            // 해당 월 데이터 찾기
            val response = monthResponses.find {
                it.month == monthToCheck + 1 // monthResponses의 월은 1부터 시작하므로 +1
            }

            // 데이터가 없으면 기본값 설정
            if (response != null) {
                // X축에 월, Y축에 총 시간 (totalHour) 설정
                entries.add(BarEntry(offset.toFloat(), (response.totalHour / 60).toFloat())) // offset을 X값으로 사용

                // 이번 달 데이터 저장
                if (offset == 0) {
                    currentMonthData = response
                }
            } else {
                // 데이터가 없으면 기본값으로 0시간 설정
                entries.add(BarEntry(offset.toFloat(), 0f)) // offset을 X값으로 사용
            }
        }

        // 이번 달 데이터가 있을 때 TextView 설정
        currentMonthData?.let {
            // 참여 횟수와 시간을 TextView에 설정
            binding.tvCount.text = it.participationCount.toString() // 참여 횟수
            binding.tvMonthTime.text = (it.totalHour / 60).toString() // 시간을 시간 단위로 변환하여 설정
        } ?: run {
            // 이번 달 데이터가 없을 경우 기본값 설정
            binding.tvCount.text = "0"
            binding.tvMonthTime.text = "0"
        }

        val dataSet = BarDataSet(entries, "플로깅 시간").apply {
            // 색상 설정 (값이 0인 경우 색상을 투명하게 설정)
            val colors = entries.map { entry ->
                when {
                    entry.y == 0f -> Color.WHITE // Y값이 0일 때 색상
                    entry.x == 0f -> ContextCompat.getColor(this@MyMonthlyPloggingActivity, R.color.main) // 이번 달 색상
                    entry.y > 0 -> ContextCompat.getColor(this@MyMonthlyPloggingActivity, R.color.light_orange) // Y값이 0보다 클 때 색상
                    else -> Color.WHITE // Y값이 0보다 작을 때 색상 (필요시 추가)
                }

            }
            setColors(colors)

            setDrawValues(true) // 막대 위에 값을 표시하도록 설정
            valueTextSize = 12f // 값의 텍스트 크기 설정
            valueTextColor = ContextCompat.getColor(this@MyMonthlyPloggingActivity, R.color.gray) // 값의 색상 설정
            valueTypeface = Typeface.DEFAULT // 값의 글꼴 설정

            // 커스텀 ValueFormatter 설정
            valueFormatter = CustomValueFormatter()
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.4f  // 막대 너비 설정
        }

        barChart.data = barData
        barChart.apply {
            setFitBars(true)  // 막대 간격 자동 조절
            invalidate()
        }
    }

    private fun updateMonthLabels() {
        // 현재 월 계산
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // 0부터 시작 (0 = 1월)

        // TextView ID 리스트
        val monthTextViews = listOf(
            binding.tvMonth1,
            binding.tvMonth2,
            binding.tvMonth3,
            binding.tvMonth4,
            binding.tvMonth5
        )

        // TextView에 월 이름 설정
        val months = listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        for ((index, textView) in monthTextViews.withIndex()) {
            val monthToDisplay = (currentMonth - 2 + index + 12) % 12 // 이전 2개월부터 현재와 이후 2개월까지
            textView.text = months[monthToDisplay]
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null // 메모리 누수 방지
    }
}