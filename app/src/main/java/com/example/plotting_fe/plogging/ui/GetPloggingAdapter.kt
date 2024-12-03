package com.example.plotting_fe.plogging.ui

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class GetPloggingAdapter(
    private val context: Context,
    private var dataList: List<PloggingGetStarResponse>
) : RecyclerView.Adapter<GetPloggingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ploggingType: TextView = itemView.findViewById(R.id.tvploggingType)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val currentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        private val maxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)
        private val startLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        private val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
        private val spendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        private val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        private val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)
        private val btnJoin: TextView = itemView.findViewById(R.id.btnJoin)

        fun bind(item: PloggingGetStarResponse) {
            title.text = item.title
            startLocation.text = item.startLocation
            startTime.text = formatDate(item.startTime)
            spendTime.text = formatSpendTime(item.spendTime)
            currentPeople.text = item.currentPeople.toString()
            maxPeople.text = item.maxPeople.toString()
            ploggingType.text = if (item.ploggingType == "DIRECT") "선착순" else "승인제"

            updateStarIcon(item.isStar)

            grayStar.setOnClickListener { toggleStar(item.ploggingId, true) }
            colorStar.setOnClickListener { toggleStar(item.ploggingId, false) }
            btnJoin.setOnClickListener { navigateToDetail(item.ploggingId) }
        }

        private fun updateStarIcon(isStarred: Boolean) {
            grayStar.visibility = if (isStarred) View.GONE else View.VISIBLE
            colorStar.visibility = if (isStarred) View.VISIBLE else View.GONE
        }

        private fun toggleStar(ploggingId: Long, star: Boolean) {
            updateStar(ploggingId)
            updateStarIcon(star)
        }

        private fun updateStar(ploggingId: Long) {
            ApiClient.getApiClient().create(MyPloggingController::class.java)
                .updateStar(ploggingId)
                .enqueue(object : Callback<ResponseTemplate<Void>> {
                    override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                        Log.d("UpdateStar", if (response.isSuccessful) "Success: $ploggingId" else "Failure: ${response.code()}")
                    }

                    override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                        Log.e("UpdateStar", "Error: ${t.message}")
                    }
                })
        }

        private fun navigateToDetail(ploggingId: Long) {
            val intent = Intent(context, PloggingDetailActivity::class.java).apply {
                putExtra("ploggingId", ploggingId)
            }
            context.startActivity(intent)
        }

        private fun formatDate(dateStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM월 dd일 a hh:mm", Locale.KOREA)
            return try {
                inputFormat.parse(dateStr)?.let { outputFormat.format(it) } ?: dateStr
            } catch (e: Exception) {
                Log.e("FormatDate", "Error parsing date: ${e.message}")
                dateStr
            }
        }

        private fun formatSpendTime(spendTime: Long): String {
            val hours = (spendTime / 60).toInt()
            return "$hours 시간"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_plogging_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(newDataList: List<PloggingGetStarResponse>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}
