package com.example.plotting_fe.mypage.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StarViewPagerAdapter extends FragmentStateAdapter {
    public StarViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StarPeopleFragment(); // 첫 번째 탭에 사람 목록 프래그먼트
        } else {
            return new StarPloggingFragment(); // 두 번째 탭에 플로깅 목록 프래그먼트
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 탭의 개수
    }
}
