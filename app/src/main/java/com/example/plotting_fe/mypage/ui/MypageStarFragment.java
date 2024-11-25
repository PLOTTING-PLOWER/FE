package com.example.plotting_fe.mypage.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.util.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MypageStarFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StarViewPagerAdapter pagerAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MypageStarFragment() {
    }

    public static MypageStarFragment newInstance(String param1, String param2) {
        MypageStarFragment fragment = new MypageStarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_star, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // ViewPager2에 어댑터 설정
        pagerAdapter = new StarViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // TabLayout과 ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("사람");
            } else if (position == 1) {
                tab.setText("플로깅");
            }
        }).attach();

        // 뒤로 가기 버튼 설정
        ImageView backButton = view.findViewById(R.id.iv_back);
        Utils.onBackButtonClick(this, backButton);

        return view;
    }
}