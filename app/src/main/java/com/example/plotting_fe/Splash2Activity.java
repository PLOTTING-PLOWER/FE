package com.example.plotting_fe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.plotting_fe.utils.Utils;

public class Splash2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Splash2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 회원가입 버튼에 클릭 리스너 설정
        findViewById(R.id.btn_join).setOnClickListener(v -> Utils.onJoinClick(this));

        // 로그인 버튼에 클릭 리스너 설정
        findViewById(R.id.btn_login).setOnClickListener(v -> Utils.onLoginClick(this));
    }
}