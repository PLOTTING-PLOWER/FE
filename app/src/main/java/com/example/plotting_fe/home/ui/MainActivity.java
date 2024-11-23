package com.example.plotting_fe.home.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.plotting_fe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        // Fragment가 이미 추가되지 않은 경우에만 추가
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();  // Fragment 인스턴스 생성
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainFragment);  // Fragment를 지정된 컨테이너에 추가
            transaction.commit();
        }
    }
}
