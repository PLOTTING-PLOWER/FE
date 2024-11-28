package com.example.plotting_fe.plogging.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button
import android.widget.ImageView

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import com.plotting.server.plogging.dto.response.PloggingMapResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PloggingMapAdapter(
    private val context: Context,
    private val itemList: List<PloggingMapResponse>
) : RecyclerView.Adapter<PloggingMapAdapter.PloggingMapViewHolder>() {

    inner class PloggingMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val PloggingType: TextView = view.findViewById(R.id.tvploggingType)
        val Title: TextView = view.findViewById(R.id.tvTitle)
        val CurrentPeople: TextView = view.findViewById(R.id.tvCurrentPeople)
        val MaxPeople: TextView = view.findViewById(R.id.tvMaxPeople)
        val StartLocation: TextView = view.findViewById(R.id.tvStartLocation)
        val StartTime: TextView = view.findViewById(R.id.tvStartTime)
        val SpendTime: TextView = view.findViewById(R.id.tvSpendTime)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)
        val btnJoin: Button = itemView.findViewById(R.id.btnJoin)

        fun bind(item: PloggingMapResponse) {

            Title.text = item.title
            StartLocation.text = item.startLocation
            StartTime.text = item.startTime
            SpendTime.text = (item.spendTime / 60).toString()
            CurrentPeople.text = item.currentPeople.toString()
            MaxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                PloggingType.text = "선착순"
            } else {
                PloggingType.text = "승인제"
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

            // 버튼 클릭 리스너 설정
            btnJoin.setOnClickListener {
                val intent = Intent(context, PloggingDetailActivity::class.java)
                context.startActivity(intent) // Context를 사용해 액티비티 이동
            }
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingMapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plogging_info, parent, false)
        return PloggingMapViewHolder(view)
    }

    override fun onBindViewHolder(holder: PloggingMapViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
