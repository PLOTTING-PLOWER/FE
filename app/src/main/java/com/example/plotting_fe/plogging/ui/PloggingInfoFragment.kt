package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.plogging.dto.Participant
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PloggingInfoFragment : Fragment() {
    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var adapter: PloggingUserAdapter
    private lateinit var participantList: ArrayList<Participant>

    private var ploggingId: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plogging_info, container, false)

        // R.id.ll_people 클릭 리스너 설정
        val llPeople: LinearLayout = view.findViewById(R.id.ll_people)
        llPeople.setOnClickListener {
            showParticipantsDialog()
        }

        loadInfo(view)

        return view
    }

    private fun showParticipantsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_participants, null)

        participantsRecyclerView = dialogView.findViewById(R.id.participantsRecyclerView)
        participantList = ArrayList()

        // 예시 데이터 추가
        participantList.add(Participant("가가가", "신상 정보 뭐 넘겨야 하냐?"))
        participantList.add(Participant("거거거", "뭐가 필요하니?"))
        participantList.add(Participant("APPLE", "BCA 2468 3545 ****"))
        participantList.add(Participant("ANT", "BCA 2468 3545 ****"))
        participantList.add(Participant("ARIANA GRANDE", "BCA 2468 3545 ****"))
        participantList.add(Participant("A+", "BCA 2468 3545 ****"))
        participantList.add(Participant("B급인생", "BCA 2468 3545 ****"))

        adapter = PloggingUserAdapter(participantList)
        participantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        participantsRecyclerView.adapter = adapter

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        dialog.show()
        dialog.window?.setLayout(
            800,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setGravity(Gravity.END)
    }

    private fun loadInfo(view: View) {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.getPloggingDetail(ploggingId).enqueue(object :
            Callback<ResponseTemplate<PloggingDetailResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<PloggingDetailResponse>>,
                response: Response<ResponseTemplate<PloggingDetailResponse>>,
            ) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    Log.d("post", "onResponse 성공: " + response.body().toString())

                    val body = response.body()?.results

                    // body에 있는 데이터를 화면에 표시
                    body?.let {
                        // TextView에 body.title을 설정
                        view.findViewById<TextView>(R.id.tv_title).text = it.title
                        view.findViewById<TextView>(R.id.tv_host).text = "플라워"
                        view.findViewById<TextView>(R.id.tv_current_people).text =
                            it.currentPeople.toString()
                        view.findViewById<TextView>(R.id.tv_max_people).text =
                            it.maxPeople.toString()
                        view.findViewById<TextView>(R.id.tv_time).text =
                            (it.spendTime / 60).toString()
                        view.findViewById<TextView>(R.id.tv_recruit_start).text =
                            it.recruitStartDate.toString()
                        view.findViewById<TextView>(R.id.tv_recruit_end).text =
                            it.recruitEndDate.toString()
                        view.findViewById<TextView>(R.id.tv_plogging_type).text =
                            it.ploggingType.toString()
                        view.findViewById<TextView>(R.id.tv_description).text = it.content
                        view.findViewById<TextView>(R.id.tv_start_location).text = it.startLocation
                        view.findViewById<TextView>(R.id.tv_end_location).text = it.endLocation
                    }


                    view.findViewById<LinearLayout>(R.id.ll_people).setOnClickListener {
                        showParticipantsDialog()
                    }

                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<PloggingDetailResponse>>,
                t: Throwable
            ) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }
}
