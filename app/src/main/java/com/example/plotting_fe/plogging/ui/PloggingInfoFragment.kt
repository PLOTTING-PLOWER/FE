package com.example.plotting_fe.plogging.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.databinding.FragmentPloggingInfoBinding
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.plogging.dto.Participant
import com.example.plotting_fe.plogging.dto.response.PloggingDetailResponse
import com.example.plotting_fe.plogging.dto.response.PloggingUserListResponse
import com.example.plotting_fe.plogging.presentation.PloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PloggingInfoFragment : Fragment() {
    private var _binding: FragmentPloggingInfoBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentPloggingInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        // R.id.ll_people 클릭 리스너 설정
        binding.llPeople.setOnClickListener {
            showParticipantsDialog()
        }

        loadInfo(view)

        // 참여하기 버튼 클릭 리스너 설정
        binding.btnJoin.setOnClickListener {
            joinPlogging()
        }

        return view
    }

    private fun joinPlogging() {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.joinPlogging(ploggingId, 1).enqueue(object :
            Callback<ResponseTemplate<String>> {
            override fun onResponse(
                call: Call<ResponseTemplate<String>>,
                response: Response<ResponseTemplate<String>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()?.results

                    // body가 null이 아닐 경우에만 토스트 메시지를 띄움
                    body?.let {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }

                    Log.d("post", "onResponse 성공: " + response.body().toString())
                } else {
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<String>>, t: Throwable) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }


    private fun showParticipantsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_participants, null)

        participantsRecyclerView = dialogView.findViewById(R.id.participantsRecyclerView)
        participantList = ArrayList()

        // 다이얼로그의 TextView에 currentPeople 및 maxPeople 값을 설정할 변수
        val tvCurrentPeople: TextView = dialogView.findViewById(R.id.tv_current_people)
        val tvMaxPeople: TextView = dialogView.findViewById(R.id.tv_max_people)

        // API 호출
        fetchParticipants { participants, currentPeople, maxPeople ->
            participantList.clear()  // 기존 참여자 목록을 초기화
            participantList.addAll(participants)  // API에서 받은 참여자 목록 추가

            // currentPeople 및 maxPeople 값을 TextView에 설정
            tvCurrentPeople.text = currentPeople.toString()
            tvMaxPeople.text = maxPeople.toString()

            adapter = PloggingUserAdapter(participantList)
            participantsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            participantsRecyclerView.adapter = adapter
        }

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

    // API 호출 함수
    private fun fetchParticipants(callback: (List<Participant>, Int, Int) -> Unit) {
        val ploggingController = ApiClient.getApiClient().create(PloggingController::class.java)
        ploggingController.getPloggingUsers(ploggingId).enqueue(object :
            Callback<ResponseTemplate<PloggingUserListResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<PloggingUserListResponse>>,
                response: Response<ResponseTemplate<PloggingUserListResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("post", "onResponse 성공: " + response.body().toString())

                    val body = response.body()?.results

                    body?.let { it ->
                        // 참여자 목록 생성
                        val participants = it.ploggingUserList.map {
                            Participant("https://plower.s3.ap-northeast-2.amazonaws.com/file1/80110b36-21f8-433a-ab33-6d3daf641904_pg.png", it.nickname, "내용 추가 부분")
                        }

                        // currentPeople 및 maxPeople 반환
                        callback(participants, it.currentPeople.toInt(), it.maxPeople.toInt())
                    } ?: callback(emptyList(), 0, 0) // 결과가 없을 경우 빈 목록과 0 반환
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("post", "onResponse 실패 + ${response.code()}")
                    // 오류 처리
                    callback(emptyList(), 0, 0)
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<PloggingUserListResponse>>, t: Throwable) {
                // 오류 처리
                Log.d("post", "onFailure 에러: " + t.message.toString())
                callback(emptyList(), 0, 0)
            }
        })
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
                        binding.tvTitle.text = it.title
                        binding.tvHost.text = "플라워"
                        binding.tvCurrentPeople.text = it.currentPeople.toString()
                        binding.tvMaxPeople.text = it.maxPeople.toString()
                        binding.tvTime.text = (it.spendTime / 60).toString()
                        binding.tvRecruitStart.text = it.recruitStartDate.toString()
                        binding.tvRecruitEnd.text = it.recruitEndDate.toString()
                        binding.tvPloggingType.text = it.ploggingType.toString()
                        binding.tvDescription.text = it.content
                        binding.tvStartLocation.text = it.startLocation
                        binding.tvEndLocation.text = it.endLocation
                    }

                    binding.llPeople.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지하기 위해 binding을 null로 설정
    }
}
