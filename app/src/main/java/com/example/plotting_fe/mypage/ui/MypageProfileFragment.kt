package com.example.plotting_fe.mypage.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.Utils
import com.example.plotting_fe.mypage.dto.response.MyProfileResponse
import com.example.plotting_fe.mypage.presentation.MypageController
import com.example.plotting_fe.myplogging.presentation.MyPloggingController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageProfileFragment: Fragment() {
    private lateinit var profileImageView : AppCompatImageView
    private lateinit var nicknameTextView : TextView
    private lateinit var emailTextView : TextView
    private lateinit var profileMessageTextView : TextView
    private lateinit var publicTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_profile, container, false)

        // UI 요소 초기화
        profileImageView = view.findViewById(R.id.iv_image)
        nicknameTextView = view.findViewById(R.id.tv_nickname_value)
        emailTextView = view.findViewById(R.id.tv_email_value)
        profileMessageTextView = view.findViewById(R.id.tv_intro_value)
        publicTextView = view.findViewById(R.id.visibility_spinner)

        // 뒤로 가기 버튼 설정
        val backButton: ImageView = view.findViewById(R.id.iv_back)
        Utils.onBackButtonClick(this, backButton)

        // 수정 버튼 클릭 리스너 설정
        val editButton: ImageView = view.findViewById(R.id.iv_edit)
        editButton.setOnClickListener {
            Log.d("ProfileActivity", "Edit button clicked")
            // SampleActivity로 전환하는 Intent 생성
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent) // 액티비티 시작
        }

        loadProfile()

        return view
    }

    private fun loadProfile(){
        val myPageController = ApiClient.getApiClient().create(MypageController::class.java)
        myPageController.getMyProfile().enqueue(object : Callback<ResponseTemplate<MyProfileResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyProfileResponse>>,
                response: Response<ResponseTemplate<MyProfileResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val profile = response.body()?.results
                    if(profile!=null){
                        updateUI(profile)
                    }else{
                        Log.d("get", "onResponse 성공: profile == null")
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }
            override fun onFailure(call: Call<ResponseTemplate<MyProfileResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())

            }
        })
    }

    private fun updateUI(profile: MyProfileResponse){
        nicknameTextView.text = profile.nickname
        emailTextView.text = profile.email
        profileMessageTextView.text = profile.profileMessage
        Glide.with(this)
            .load(profile.profileImageUrl)
            .placeholder(R.drawable.ic_flower)
            .error(R.drawable.ic_flower)
            .into(profileImageView)
        publicTextView.text = if(profile.isProfileResponse) "공개" else "비공개"
    }

}