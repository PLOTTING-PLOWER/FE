package com.example.plotting_fe.myplogging.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.myplogging.dto.response.MyPloggingScheduledResponse
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class MyPloggingScheduledAdapter(
    private val context: Context,
    private var dataList: MutableList<MyPloggingScheduledResponse>,
    private val onCancelClick: (ploggingId: Long) -> Unit
) : RecyclerView.Adapter<MyPloggingScheduledAdapter.ViewHolder>() {

    // 데이터를 MutableList로 변환하여 내부에서 관리
  //  private var dataList: MutableList<MyPloggingScheduledResponse> = dataList.toMutableList()

    // ViewHolder 클래스 정의
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val PloggingType: TextView = itemView.findViewById(R.id.tvploggingType)
        val Title: TextView = itemView.findViewById(R.id.tvTitle)
        val StartLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        val StartTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val SpendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        val CurrentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        val MaxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)
        val IsAssigned: TextView = itemView.findViewById(R.id.tvIsAssigned)
        val cancelButton: TextView = itemView.findViewById(R.id.btnCancel)
        val grayStar: ImageView = itemView.findViewById(R.id.iv_gray_star)
        val colorStar: ImageView = itemView.findViewById(R.id.iv_color_star)

        // 데이터를 뷰에 바인딩하는 메서드
        fun bind(item: MyPloggingScheduledResponse) {
            Title.text = item.title
            StartLocation.text = item.startLocation

            val formattedTime = formatStartTime(item.startTime)
            StartTime.text = formattedTime

            SpendTime.text = "${(item.spendTime / 60)}H"
            CurrentPeople.text = "${item.currentPeople}/"
            MaxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                PloggingType.text = "선착순"
            } else {
                PloggingType.text = "승인제"
            }

            if (item.isAssigned) {
                IsAssigned.text = "승인완료"
            } else {
                IsAssigned.text = "승인대기"
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

            cancelButton.setOnClickListener {
                cancelPlogging(item.ploggingId, adapterPosition)
            }
        }
    }

    fun cancelPlogging(ploggingId: Long, position: Int) {
        val myPloggingController = ApiClient.getApiClient().create(MyPloggingController::class.java)
        myPloggingController.reqeustCancel(ploggingId).enqueue(object :
            Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "플로깅 취소 완료.", Toast.LENGTH_SHORT).show()

                    // 데이터 리스트에서 해당 아이템 삭제
                    dataList.removeAt(position)

                    // RecyclerView에서 아이템 삭제
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, dataList.size) // 다른 아이템 위치 갱신
                } else {
                    Toast.makeText(context, "플로깅 취소 실패.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류로 취소 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
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

    // RecyclerView에서 사용할 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scheduled_plogging, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun formatStartTime(input: String): String {
        // Step 1: 입력 형식에 맞는 SimpleDateFormat 정의
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Step 2: 원하는 출력 형식 정의
        val outputFormat = SimpleDateFormat("MM.dd (E) a hh:mm", Locale.KOREAN)

        // Step 3: 입력 문자열을 Date로 변환
        val date = inputFormat.parse(input)

        // Step 4: Date 객체를 원하는 형식으로 변환
        return outputFormat.format(date)
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = dataList.size

}
