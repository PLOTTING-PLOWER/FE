package com.example.plotting_fe.home.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<String> dummyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.rv_popular_plogging);

        // RecyclerView에 LayoutManager 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 데이터 리스트 초기화 (여기에 실제 데이터 로딩 코드가 들어갈 수 있습니다)
        dummyData = new ArrayList<>();
        dummyData.add("Plogging 1");
        dummyData.add("Plogging 2");
        dummyData.add("Plogging 3");
        dummyData.add("Plogging 4");

        // 어댑터 설정
        homeAdapter = new HomeAdapter(dummyData);
        recyclerView.setAdapter(homeAdapter);
    }
}
