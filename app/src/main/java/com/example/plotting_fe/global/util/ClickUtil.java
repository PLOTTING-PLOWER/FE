package com.example.plotting_fe.global.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.plotting_fe.global.ResponseTemplate;
import com.example.plotting_fe.user.ui.LoginActivity;
import com.example.plotting_fe.user.ui.SignUpActivity;

public class ClickUtil {

    // 회원가입 화면으로 전환하는 메서드
    public static void onJoinClick(Activity activity) {
        Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    // 로그인 화면(LoginActivity)으로 전환하는 메서드
    public static void onLoginClick(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
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

    private fun togglePloggingStar(starId: Long, position: Int) {
        val starController = ApiClient.getApiClient().create(StarController::class.java)
        starController.updateUserStar(starId).enqueue(object :
        Callback<ResponseTemplate<Boolean>> {
            override fun onResponse(call: Call<ResponseTemplate<Boolean>>, response: Response<ResponseTemplate<Boolean>>) {
                if (response.isSuccessful) {
                    // 서버에서 반환된 즐겨찾기 상태로 UI 업데이트
                    val isStarred = response.body()?.results ?: false
                    if(!isStarred){
                        ploggingList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, ploggingList.size) // 나머지 항목 갱신
                        Log.d("post", "즐겨찾기 해제 성공: $isStarred")
                    }
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
