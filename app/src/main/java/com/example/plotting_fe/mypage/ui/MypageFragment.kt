package com.example.plotting_fe.mypage.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.plotting_fe.R
import com.example.plotting_fe.WelcomeActivity
import com.example.plotting_fe.global.TokenApplication

class MypageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_mypage, container, false)

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
            TokenApplication.clearTokens()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // 현재 프래그먼트 및 관련 액티비티 종료
            activity?.finish()
        }

        // 탈퇴
        val withdraw = rootView.findViewById<LinearLayout>(R.id.ll_menu).findViewById<LinearLayout>(R.id.ll_withdraw)
        withdraw.setOnClickListener{

        }

        return rootView
    }
}