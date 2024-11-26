package com.example.plotting_fe.mypage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plotting_fe.R

class ProfileDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 레이아웃을 인플레이트하고 반환
        return inflater.inflate(R.layout.fragment_profile_detail, container, false)
    }
}
