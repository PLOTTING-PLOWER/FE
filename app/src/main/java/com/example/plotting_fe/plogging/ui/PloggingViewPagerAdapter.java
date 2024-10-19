package com.example.plotting_fe.plogging.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PloggingViewPagerAdapter extends FragmentStateAdapter {
        public PloggingViewPagerAdapter(FragmentActivity fa) {
                super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
                switch (position) {
                    case 1:
                                return new PloggingCommentFragment();
                        default:
                                return new PloggingInfoFragment();
                }
        }

        @Override
        public int getItemCount() {
                return 2; // 탭 수
        }
}