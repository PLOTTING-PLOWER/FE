package com.example.plotting_fe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 초기화 작업 시작
        initializeApp();
    }

    private void initializeApp() {
        // 여기에 초기화 작업을 수행 (예: 데이터베이스 초기화, 설정 로드 등)

        // 예시로 2초 딜레이를 주면서 초기화 작업을 시뮬레이션
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // 초기화 작업 완료 후 메인 화면으로 전환
            navigateToMain();
        }, 2000); // 실제 초기화 작업 시간에 따라 조정
    }

    private void navigateToMain() {
        // 초기화 작업이 끝나면 메인 화면으로 전환
        Intent intent = new Intent(SplashActivity.this, Splash2Activity.class);
        startActivity(intent);
        finish(); // 스플래시 화면 종료
    }
}