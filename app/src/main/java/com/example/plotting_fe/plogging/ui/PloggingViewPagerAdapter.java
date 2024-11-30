package com.example.plotting_fe.plogging.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PloggingViewPagerAdapter extends FragmentStateAdapter {
        private final Long ploggingId;

        public PloggingViewPagerAdapter(FragmentActivity fa, Long ploggingId) {
                super(fa);
                this.ploggingId = ploggingId;
        }

        @Override
        public Fragment createFragment(int position) {
                switch (position) {
                    case 1:
                                return PloggingCommentFragment.Companion.newInstance(ploggingId);
                        default:
                                return PloggingInfoFragment.Companion.newInstance(ploggingId); // ploggingId 전달
                }
        }

        @Override
        public int getItemCount() {
                return 2; // 탭 수
        }
}