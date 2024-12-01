package com.example.plotting_fe.mypage.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.mypage.dto.response.DetailProfileResponse
import com.example.plotting_fe.mypage.presentation.MypageController
import com.example.plotting_fe.mypage.presentation.StarController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetailFragment : Fragment() {

    private val myPageController : MypageController by lazy{
        ApiClient.getApiClient().create(MypageController::class.java)
    }

    private val starController : StarController by lazy{
        ApiClient.getApiClient().create(StarController::class.java)
    }
    private var userId: Long? = null // userId를 저장하는 멤버 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        // 전달된 userId를 arguments에서 가져옴
        userId = arguments?.getLong("userId")
        if (userId != null) {
            fetchUserProfile(userId!!) // 프로필 정보 가져오기
        } else {
            // userId가 null인 경우 처리
            Log.d("userId=!= null", "사용자 정보를 가져올 수 없습니다.")
            Toast.makeText(requireContext(), "사용자 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        // 레이아웃을 인플레이트하고 반환
        return view
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
                        Log.d("get", "onResponse 성공:"+ profileData.toString())
                        updateUI(profileData) // UI 업데이트
                    } else {
                        Log.d("get", "onResponse 성공: profileData==null")
                    }
                } else {
                    activity?.onBackPressed()
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<DetailProfileResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

    private fun updateUI(profileData: DetailProfileResponse)  {
        val nicknameTextView: TextView = requireView().findViewById(R.id.profileName)
        nicknameTextView.text = profileData.nickname

        val profileImageView: AppCompatImageView = requireView().findViewById(R.id.iv_image)
        Glide.with(this)
            .load(profileData.profileImageUrl)
            .apply(RequestOptions().circleCrop()) // 이미지를 원형으로 변환
            .placeholder(R.drawable.ic_flower)
            .skipMemoryCache(true) // 메모리 캐시 비활성화
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 비활성화
            .into(profileImageView)

        val profileMessage : TextView = requireView().findViewById(R.id.profileDescription)
        profileMessage.text = profileData.profileMessage

        val totalCount : TextView = requireView().findViewById(R.id.tv_totalCount)
        totalCount.text = profileData.totalPloggingNumber.toString()
        val totalTime : TextView = requireView().findViewById(R.id.tv_totalTime)
        totalTime.text = profileData.totalPloggingTime.toString()
        val ranking : TextView = requireView().findViewById(R.id.tv_ranking)
        if(profileData.ranking != null){
            ranking.text = profileData.ranking.toString()
        }
        val starIcon : ImageView = requireView().findViewById(R.id.starIcon)
        updateStarIcon(profileData.isStar, starIcon)

        // 클릭 리스너 추가
        starIcon.setOnClickListener {
            userId?.let {
                toggleUserStar(it, starIcon) // 전달받은 userId 사용
            }
        }
    }

    // 이미지 업데이트 로직 분리
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
                    // 서버에서 반환된 즐겨찾기 상태로 UI 업데이트
                    val isStarred = response.body()?.results ?: false
                    updateStarIcon(isStarred, starIcon)
                    starIcon.tag = isStarred // 상태 업데이트
                    Log.d("post", "즐겨찾기 상태 변경 성공: $isStarred")
                } else {
                    Log.d("post", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                Log.d("post", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

}
