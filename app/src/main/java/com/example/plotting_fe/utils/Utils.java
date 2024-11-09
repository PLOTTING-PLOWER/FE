package com.example.plotting_fe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.plotting_fe.user.ui.JoinActivity;
import com.example.plotting_fe.user.ui.LoginActivity;

public class Utils {

    // 회원가입 화면으로 전환하는 메서드
    public static void onJoinClick(Context context) {
        Intent intent = new Intent(context, JoinActivity.class);
        context.startActivity(intent);
    }

    // 로그인 화면(LoginActivity)으로 전환하는 메서드
    public static void onLoginClick(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    // 액티비티용 뒤로 가기 버튼 설정 메서드
    public static void onBackButtonClick(final Activity activity, ImageView backButton) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish(); // 현재 액티비티를 종료하고 이전 화면으로 돌아감
            }
        });
    }

    // 프래그먼트에서 사용하는 뒤로 가기 버튼 설정 메서드
    public static void onBackButtonClick(final Fragment fragment, ImageView backButton) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.getParentFragmentManager().getBackStackEntryCount() > 0) {
                    fragment.getParentFragmentManager().popBackStack(); // 백 스택에서 이전 프래그먼트로 돌아감
                } else {
                    fragment.requireActivity().finish(); // 백 스택이 비어있으면 액티비티 종료
                }
            }
        });
    }


}
