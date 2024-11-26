package com.example.plotting_fe;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.plotting_fe.global.application.TokenApplication;
import com.example.plotting_fe.global.util.ClickUtil;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 로그인 상태 확인
        if (isLoggedIn()) {
            // 이미 로그인된 상태면 바로 MainActivity로 이동
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Splash2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 회원가입 버튼에 클릭 리스너 설정
        findViewById(R.id.btn_join).setOnClickListener(v -> ClickUtil.onJoinClick(this));

        // 로그인 버튼에 클릭 리스너 설정
        findViewById(R.id.btn_login).setOnClickListener(v -> ClickUtil.onLoginClick(this));
    }

    private boolean isLoggedIn() {
        String accessToken = TokenApplication.Companion.getAccessToken();
        return accessToken != null && !accessToken.isEmpty();
    }
}