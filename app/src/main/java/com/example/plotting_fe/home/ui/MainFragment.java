package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.plotting_fe.R;
import com.example.plotting_fe.utils.UIUtils;

public class MainFragment extends Fragment {

    private Button btnCardnews;
    private ImageView btnCatagotyToday, btnCatagoty15Up, btnCatagotyApprove, btnCatagotyRandom;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment의 레이아웃을 인플레이트
        View rootView = inflater.inflate(R.layout.a_activity_home, container, false);

        // Edge-to-Edge 모드 활성화
        UIUtils.enableEdgeToEdgeMode(getActivity());

        // 시스템 바 인셋 처리
        UIUtils.handleSystemBarInsets(rootView.findViewById(R.id.main));

        // 버튼 초기화
        btnCardnews = rootView.findViewById(R.id.btn_read_more);
        btnCatagotyToday = rootView.findViewById(R.id.categoty_today_finish);
        btnCatagoty15Up = rootView.findViewById(R.id.categoty_15_up);
        btnCatagotyApprove = rootView.findViewById(R.id.category_approve);
        btnCatagotyRandom = rootView.findViewById(R.id.category_random);

        // 버튼 클릭 리스너 설정
        btnCardnews.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CardnewsListActivity.class);
            startActivity(intent);
        });

        // 네비게이션 셋업
        new Handler(Looper.getMainLooper()).post(() -> {
            UIUtils.setupBottomNavigation(getActivity());
        });

        return rootView;
    }
}
