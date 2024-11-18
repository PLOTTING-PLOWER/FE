package com.example.plotting_fe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.plotting_fe.user.ui.SignUpActivity;

public class Utils {

    // 회원가입 화면으로 전환하는 메서드
    public static void onJoinClick(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    // 로그인 화면(LoginActivity)으로 전환하는 메서드
    public static void onLoginClick(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    // 액티비티 용 뒤로 가기 버튼 설정 메서드
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
                // 백 스택이 비어있지 않으면 popBackStack으로 이전 프래그먼트로 돌아가기
                if (fragment.getParentFragmentManager().getBackStackEntryCount() > 0) {
                    fragment.getParentFragmentManager().popBackStack();
                } else {
                    // 백 스택이 비어있으면 기본 뒤로 가기 동작 수행
                    fragment.requireActivity().onBackPressed();
                }
            }
        });
    }


}
