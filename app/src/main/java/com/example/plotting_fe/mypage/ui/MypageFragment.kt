package com.example.plotting_fe.mypage.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.WelcomeActivity
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.application.TokenApplication
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.LogoutUtil
import com.example.plotting_fe.mypage.dto.response.MyPageResponse
import com.example.plotting_fe.mypage.presentation.MypageController
import com.example.plotting_fe.user.presentation.AuthController
import retrofit2.Call
import retrofit2.Callback

class MypageFragment: Fragment() {

    private lateinit var profileImage: AppCompatImageView
    private lateinit var nickname :TextView
    private lateinit var isAlarm: Switch

    private val myPageController : MypageController by lazy{
        ApiClient.getApiClient().create(MypageController::class.java)
    }

    private val authController :AuthController by lazy{
        ApiClient.getApiClient().create(AuthController::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 뷰 초기화
        profileImage = rootView.findViewById(R.id.iv_image)
        nickname = rootView.findViewById(R.id.tv_username)
        isAlarm = rootView.findViewById(R.id.switch1)

        // 프로필 메뉴 클릭 리스너 설정
        val profile = rootView.findViewById<LinearLayout>(R.id.ll_menu).findViewById<LinearLayout>(R.id.ll_profile)
        profile.setOnClickListener { v ->
            val navController = Navigation.findNavController(v)
            navController.navigate(R.id.action_mypage_to_profile)
        }

        // 스타 메뉴 클릭 리스너 설정
        val star = rootView.findViewById<LinearLayout>(R.id.ll_menu).findViewById<LinearLayout>(R.id.ll_star)
        star.setOnClickListener { v ->
            val navController = Navigation.findNavController(v)
            navController.navigate(R.id.action_mypage_to_star)
        }

        // 로그 아웃
        val logout = rootView.findViewById<LinearLayout>(R.id.ll_menu).findViewById<LinearLayout>(R.id.ll_logout)
        logout.setOnClickListener{
            LogoutUtil.handleLogout()
        }

        // 탈퇴
        val withdraw = rootView.findViewById<LinearLayout>(R.id.ll_menu).findViewById<LinearLayout>(R.id.ll_withdraw)
        withdraw.setOnClickListener{
            // AlertDialog 생성
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("탈퇴 확인")
            builder.setMessage("정말 탈퇴하시겠습니까?")

            // "확인" 버튼 설정
            builder.setPositiveButton("확인") { dialog, _ ->
                // 여기서 탈퇴 API 호출 등 탈퇴 작업 수행
                handleWithdraw()
                dialog.dismiss()
            }

            // "취소" 버튼 설정
            builder.setNegativeButton("취소") { dialog, _ ->
                // 다이얼로그 닫기
                dialog.dismiss()
            }

            // 다이얼로그 보여주기
            builder.create().show()
        }

        loadMypage()

        return rootView
    }

    private fun loadMypage(){
        myPageController.getMyPage().enqueue(object : Callback<ResponseTemplate<MyPageResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPageResponse>>,
                response: retrofit2.Response<ResponseTemplate<MyPageResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val myPageResponse = response.body()?.results
                    if(myPageResponse!=null){
                        setupUI(myPageResponse)
                    }else{
                        Log.d("get", "onResponse 성공: mypage == null")
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }
            override fun onFailure(call: Call<ResponseTemplate<MyPageResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

    private fun setupUI(myPageResponse : MyPageResponse){
        // 프로필 이미지 설정
        Glide.with(this)
            .load(myPageResponse.profileImageUrl)
            .placeholder(R.drawable.ic_flower)
            .error(R.drawable.ic_flower)
            .into(profileImage)

        nickname.text= myPageResponse.nickname
        // 알림 설정 스위치 초기화
        isAlarm.isChecked = myPageResponse.isAlarmAllowed
        // 스위치 상태 변경 이벤트 리스너 추가
        isAlarm.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchChange(isChecked)
        }
    }

    private fun handleSwitchChange(isAlarmAllowed: Boolean) {
        myPageController.updateAlarm(isAlarmAllowed).enqueue(object : Callback<ResponseTemplate<Void>> {
            override fun onResponse(call: Call<ResponseTemplate<Void>>, response: retrofit2.Response<ResponseTemplate<Void>>) {
                if (response.isSuccessful) {
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                } else {
                    Log.d("get", "onResponse 실패: " + response.code() + response.body().toString())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

    private fun handleWithdraw() {
        authController.withdrawUser().enqueue(object : Callback<ResponseTemplate<Void>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: retrofit2.Response<ResponseTemplate<Void>>
            ) {
                if (response.isSuccessful) {
                    Log.d("delete", "onResponse 성공: " + response.body().toString())

                    // 로그아웃 및 초기화
                    LogoutUtil.handleLogout()
                } else {
                    Log.d("get", "onResponse 실패: " + response.code() + response.body().toString())

                    // 실패: 사용자에게 실패 메시지 표시
                    Toast.makeText(requireContext(), "탈퇴 처리에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

}