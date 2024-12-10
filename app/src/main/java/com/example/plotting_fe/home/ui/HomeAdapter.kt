package com.example.plotting_fe.home.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.example.plotting_fe.plogging.dto.response.PloggingGetStarResponse
import com.example.plotting_fe.plogging.ui.PloggingDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeAdapter(
    private val context: Context,
    private var dataList: List<PloggingGetStarResponse> // 생성자에서 데이터를 설정
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ploggingType: TextView = itemView.findViewById(R.id.tvploggingType)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val currentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        val maxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)
        val startLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val spendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)
        val btnJoin: TextView = itemView.findViewById(R.id.btnJoin)
        val statusImage: ImageView = itemView.findViewById(R.id.tvStatus)
        val statusText: TextView = itemView.findViewById(R.id.tvStatusText)

        fun bind(item: PloggingGetStarResponse) {
            title.text = item.title
            startLocation.text = item.startLocation
            startTime.text = formatDate(item.startTime)
            spendTime.text = formatSpendTime(item.spendTime)
            currentPeople.text = item.currentPeople.toString()
            maxPeople.text = item.maxPeople.toString()

            ploggingType.text = if (item.ploggingType == "DIRECT") "선착순" else "승인제"

            // 진행 여부 표시
            val isOngoing = formatStatus(item.recruitEndDate)
            statusText.text = if (isOngoing) "진행" else "마감"
            statusImage.setImageResource(
                if (isOngoing) R.drawable.ic_green_circle else R.drawable.ic_red_circle
            )

            // 스타 아이콘 표시
            if (item.isStar) {
                grayStar.visibility = View.GONE
                colorStar.visibility = View.VISIBLE
            } else {
                grayStar.visibility = View.VISIBLE
                colorStar.visibility = View.GONE
            }

            grayStar.setOnClickListener {
                updateStar(item.ploggingId)
                grayStar.visibility = View.GONE
                colorStar.visibility = View.VISIBLE
            }

            colorStar.setOnClickListener {
                updateStar(item.ploggingId)
                grayStar.visibility = View.VISIBLE
                colorStar.visibility = View.GONE
            }

            btnJoin.setOnClickListener {
                val intent = Intent(context, PloggingDetailActivity::class.java)
                intent.putExtra("ploggingId", item.ploggingId)
                context.startActivity(intent)
            }
        }

        private fun updateStar(ploggingId: Long) {
            val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
            myPloggingController.updateStar(ploggingId).enqueue(object : Callback<ResponseTemplate<Void>> {
                override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                    if (response.isSuccessful) {
                        Log.d("post", "성공: $ploggingId")
                    } else {
                        Log.d("post", "실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                    Log.d("post", "실패: ${t.message}")
                }
            })
        }

        private fun formatDate(dateStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM월 dd일 a hh:mm", Locale.KOREA)
            return try {
                val date = inputFormat.parse(dateStr)
                if (date != null) outputFormat.format(date) else dateStr
            } catch (e: Exception) {
                e.printStackTrace()
                dateStr
            }
        }

        private fun formatSpendTime(spendTime: Long): String {
            val hours = (spendTime / 60).toInt()
            return "$hours 시간"
        }

        private fun formatStatus(recruitEndDate: String): Boolean {
            return try {
                val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDateTime = Date() // 현재 시간
                val endDateTime = dateTimeFormat.parse(recruitEndDate)

                endDateTime != null && endDateTime.after(currentDateTime)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    }

    fun setData(newDataList: List<PloggingGetStarResponse>) {
        this.dataList = newDataList
        notifyDataSetChanged()  // 데이터 변경 후 RecyclerView 업데이트
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_plogging_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}

