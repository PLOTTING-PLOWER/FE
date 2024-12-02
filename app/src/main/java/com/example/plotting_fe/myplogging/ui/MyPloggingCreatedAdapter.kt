package com.example.plotting_fe.myplogging.ui

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
import com.example.plotting_fe.myplogging.dto.PloggingData
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPloggingCreatedAdapter(
    private val items: MutableList<PloggingData>,
    private val onDeleteClick: (Long) -> Unit,
    private val activity: MyPloggingCreatedActivity)
    : RecyclerView.Adapter<MyPloggingCreatedAdapter.PloggingViewHolder>() {

    class PloggingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val startLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val spendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        val ploggintType: TextView = itemView.findViewById(R.id.tv_plogging_type)
        val currentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        val maxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)
        val btnWaiting: TextView = itemView.findViewById(R.id.btn_waiting)
        val btnDelete: TextView = itemView.findViewById(R.id.btn_delete)
        val btnUpdate: TextView = itemView.findViewById(R.id.btn_update)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)

        fun bind(item: PloggingData, onDeleteClick: (Long) -> Unit, activity: MyPloggingCreatedActivity) {
            title.text = item.title
            startLocation.text = item.startLocation
            startTime.text = item.startTime
            spendTime.text = (item.spendTime / 60).toString()
            currentPeople.text = item.currentPeople.toString()
            maxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                ploggintType.text = "선착순"
                btnWaiting.visibility = View.INVISIBLE
            } else {
                ploggintType.text = "승인제"
            }

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

            btnWaiting.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, MyPloggingWaitingActivity::class.java)
                intent.putExtra("ploggingId", item.ploggingId) // ploggingId 전달
                context.startActivity(intent)
            }

            btnUpdate.setOnClickListener() {
                val context = itemView.context
                val intent = Intent(context, MyPloggingUpdateActivity::class.java)
                intent.putExtra("ploggingId", item.ploggingId) // ploggingId 전달
                intent.putExtra("ploggingType", item.ploggingType)
                intent.putExtra("title", item.title)
                intent.putExtra("content", item.content)
                intent.putExtra("startTime", item.startTime)
                intent.putExtra("spendTime", item.spendTime)
                intent.putExtra("maxPeople", item.maxPeople)
                intent.putExtra("recruitStartDate", item.recruitStartDate)
                intent.putExtra("recruitEndDate", item.recruitEndDate)
                context.startActivity(intent)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(item.ploggingId) // ploggingId 전달
            }
        }

        fun updateStar(ploggingId: Long) {
            val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
            myPloggingController.updateStar(ploggingId).enqueue(object :
                Callback<ResponseTemplate<Void>> {
                override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                    if (response.isSuccessful) {
                        Log.d("post", "성공: $ploggingId")
                    } else {
                        Log.d("post", "삭제 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                    Log.d("post", "실패: ${t.message}")
                }
            })
        }
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onDeleteClick, activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_plogging, parent, false)
        return PloggingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}