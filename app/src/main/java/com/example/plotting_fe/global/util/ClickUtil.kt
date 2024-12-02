package com.example.plotting_fe.global.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient.getApiClient
import com.example.plotting_fe.mypage.presentation.StarController
import com.example.plotting_fe.user.ui.LoginActivity
import com.example.plotting_fe.user.ui.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ClickUtil {

    // 회원가입 화면으로 전환하는 메서드
    fun onJoinClick(activity: Activity) {
        val intent = Intent(activity, SignUpActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    // 로그인 화면(LoginActivity)으로 전환하는 메서드
    fun onLoginClick(activity: Activity) {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    // 액티비티 용 뒤로 가기 버튼 설정 메서드
    fun onBackButtonClick(activity: Activity, backButton: ImageView) {
        backButton.setOnClickListener {
            activity.finish() // 현재 액티비티를 종료하고 이전 화면으로 돌아감
        }
    }

    // 프래그먼트에서 사용하는 뒤로 가기 버튼 설정 메서드
    fun onBackButtonClick(fragment: Fragment, backButton: ImageView) {
        backButton.setOnClickListener {
            val fragmentManager = fragment.parentFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStack() // 백 스택에서 이전 프래그먼트로 돌아가기
            } else {
                fragment.requireActivity().onBackPressed() // 기본 뒤로 가기 동작 수행
            }
        }
    }

    // 플로깅 즐겨찾기
    fun togglePloggingStar(ploggingId: Long): Boolean {
        val starController = ApiClient.getApiClient().create(StarController::class.java)
        var isSuccessful = false
        starController.updatePloggingStar(ploggingId).enqueue(object :
            Callback<ResponseTemplate<Boolean>> {
            override fun onResponse(call: Call<ResponseTemplate<Boolean>>, response: Response<ResponseTemplate<Boolean>>) {
                if (response.isSuccessful) {
                    // 서버에서 반환된 즐겨찾기 상태로 UI 업데이트
                    val isStarred = response.body()?.results ?: false
                    isSuccessful = true
                    if(!isStarred){
                        Log.d("post", "즐겨찾기 해제 성공: $isStarred")
                    }else{
                        Log.d("post", "즐겨찾기 추가 성공: $isStarred")
                    }
                } else {
                    Log.d("post", "onResponse 실패: " + response.code())
                }
            }
            override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                Log.d("post", "onFailure 에러: " +  t.message.toString())
            }
        })
        return isSuccessful
    }

    fun togglePloggingStarWithHome(ploggingId: Long, callback: ToggleCallback) {
        val starController = getApiClient().create(StarController::class.java)
        starController.updatePloggingStar(ploggingId)
            .enqueue(object : Callback<ResponseTemplate<Boolean>> { // Changed Boolean? to Boolean
                override fun onResponse(
                    call: Call<ResponseTemplate<Boolean>>,
                    response: Response<ResponseTemplate<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        val isStarred = response.body()?.results ?: false // Handle null safely here
                        if (!isStarred) {
                            Log.d("post", "즐겨찾기 해제 성공: $isStarred")
                        } else {
                            Log.d("post", "즐겨찾기 추가 성공: $isStarred")
                        }
                        // 콜백으로 결과 반환
                        callback.onComplete(isStarred)
                    } else {
                        Log.d("post", "onResponse 실패: " + response.code())
                        callback.onComplete(false) // 실패 시 false 반환
                    }
                }

                override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                    Log.d("post", "onFailure 에러: " + t.message)
                    callback.onComplete(false) // 실패 시 false 반환
                }
            })
    }

    interface ToggleCallback {
        fun onComplete(isStarred: Boolean)
    }


}
