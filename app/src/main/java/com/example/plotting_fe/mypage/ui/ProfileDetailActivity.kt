package com.example.plotting_fe.mypage.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.mypage.dto.response.DetailProfileResponse
import com.example.plotting_fe.mypage.presentation.MypageController
import com.example.plotting_fe.mypage.presentation.StarController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetailActivity : AppCompatActivity() {

    private val myPageController: MypageController by lazy {
        ApiClient.getApiClient().create(MypageController::class.java)
    }

    private val starController: StarController by lazy {
        ApiClient.getApiClient().create(StarController::class.java)
    }

    private var userId: Long? = null // userId를 저장하는 멤버 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)

        // 전달된 userId를 intent에서 가져옴
        userId = intent.getLongExtra("userId", -1L).takeIf { it != -1L }
        if (userId != null) {
            fetchUserProfile(userId!!) // 프로필 정보 가져오기
        } else {
            Log.d("userId=!= null", "사용자 정보를 가져올 수 없습니다.")
            Toast.makeText(this, "사용자 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        val backButton: ImageView = findViewById(R.id.iv_back)
        ClickUtil.onBackButtonClick(this, backButton)
    }

    private fun fetchUserProfile(userId: Long) {
        myPageController.getDetailProfile(userId).enqueue(object :
            Callback<ResponseTemplate<DetailProfileResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<DetailProfileResponse>>,
                response: Response<ResponseTemplate<DetailProfileResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val profileData = response.body()?.results
                    if (profileData != null) {
                        Log.d("get", "onResponse 성공:" + profileData.toString())
                        updateUI(profileData) // UI 업데이트
                    } else {
                        Log.d("get", "onResponse 성공: profileData==null")
                    }
                } else {
                    finish()
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<DetailProfileResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun updateUI(profileData: DetailProfileResponse) {
        val nicknameTextView: TextView = findViewById(R.id.profileName)
        nicknameTextView.text = profileData.nickname

        val profileImageView: ImageView = findViewById(R.id.iv_image)
        Glide.with(this)
            .load(profileData.profileImageUrl)
            .apply(RequestOptions().circleCrop()) // 이미지를 원형으로 변환
            .placeholder(R.drawable.ic_flower)
            .skipMemoryCache(true) // 메모리 캐시 비활성화
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 비활성화
            .into(profileImageView)

        val profileMessage: TextView = findViewById(R.id.profileDescription)
        profileMessage.text = profileData.profileMessage

        val totalCount: TextView = findViewById(R.id.tv_totalCount)
        totalCount.text = profileData.totalPloggingNumber.toString()
        val totalTime: TextView = findViewById(R.id.tv_totalTime)
        totalTime.text = profileData.totalPloggingTime.toString()
        val ranking: TextView = findViewById(R.id.tv_ranking)
        if (profileData.ranking != null) {
            ranking.text = profileData.ranking.toString()
        }
        val starIcon: ImageView = findViewById(R.id.starIcon)
        updateStarIcon(profileData.isStar, starIcon)

        // 클릭 리스너 추가
        starIcon.setOnClickListener {
            userId?.let {
                toggleUserStar(it, starIcon) // 전달받은 userId 사용
            }
        }
    }

    private fun updateStarIcon(isStar: Boolean, starIcon: ImageView) {
        if (isStar) {
            starIcon.setImageResource(R.drawable.ic_star_color) // 즐겨찾기 상태
        } else {
            starIcon.setImageResource(R.drawable.ic_star_gray) // 즐겨찾기 해제 상태
        }
    }

    private fun toggleUserStar(starId: Long, starIcon: ImageView) {
        starController.updateUserStar(starId).enqueue(object : Callback<ResponseTemplate<Boolean>> {
            override fun onResponse(call: Call<ResponseTemplate<Boolean>>, response: Response<ResponseTemplate<Boolean>>) {
                if (response.isSuccessful) {
                    val isStarred = response.body()?.results ?: false
                    updateStarIcon(isStarred, starIcon)
                    starIcon.tag = isStarred
                    Log.d("post", "즐겨찾기 상태 변경 성공: $isStarred")
                } else {
                    Log.d("post", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                Log.d("post", "onFailure 에러: " + t.message.toString())
            }
        })
    }
}
