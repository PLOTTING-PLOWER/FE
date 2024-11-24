package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.ui.GetPloggings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnRead;
    private ImageView btnCategotyTodayWillFinish, btnCategoty15Up, btnCategoryApprove, btnCategoryDirect
            , btnPlower1, btnPlower2, btnPlower3, btnPlower4;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<String> dummyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        findViewById(R.id.welcome_image);   //1. 사용자 이미지 url
        findViewById(R.id.welcome_nickname); //2. 사용자 닉네임
        btnRead = findViewById(R.id.btn_read_more);   //3. 카드뉴스 읽으러 가기 버튼
        btnCategotyTodayWillFinish = findViewById(R.id.categoty_today_finish);   //4. 카테고리 (1) 오늘마감 버튼
        btnCategoty15Up = findViewById(R.id.categoty_15_up);    //4. 카테고리 (2) 15명 이상
        btnCategoryApprove = findViewById(R.id.category_approve);//4. 카테고리 (3) 승인제 플로깅
        btnCategoryDirect = findViewById(R.id.category_random);//4. 카테고리 (4) 선착순
        recyclerView = findViewById(R.id.rv_popular_plogging);  //5. 인기플로깅

        btnPlower1 = findViewById(R.id.popular_plowers_first);  //6. 인기플로워 1
        btnPlower2 = findViewById(R.id.popular_plowers_second);  //6. 인기플로워 2
        btnPlower3 = findViewById(R.id.popular_plowers_third);  //6. 인기플로워 3
        btnPlower4 = findViewById(R.id.popular_plowers_fourth);  //6. 인기플로워 4

        //recyclerView 가로로 스크롤
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 데이터 리스트 초기화 (여기에 실제 데이터 로딩 코드가 들어갈 수 있습니다)
        dummyData = new ArrayList<>();
        dummyData.add("Plogging 1");
        dummyData.add("Plogging 2");
        dummyData.add("Plogging 3");
        dummyData.add("Plogging 4");

        // 어댑터 설정
        homeAdapter = new HomeAdapter(dummyData);
        recyclerView.setAdapter(homeAdapter);

        //3. 카드뉴스 읽으러 가기
        btnRead.setOnClickListener(v -> {
            v.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.button_color_in_home));
            Intent intent = new Intent(v.getContext(), CardnewsListActivity.class);
            startActivity(intent);
        });

        // 4. 카테고리 버튼 이동 TODO: 조건에 맞는 플로깅 지정해야한다.
        btnCategotyTodayWillFinish.setOnClickListener(v -> {
            // TODO: 여기서 값을 미리 정해서 넘겨줘야할까?
            Intent intent = new Intent(v.getContext(), GetPloggings.class);
            startActivity(intent);
        });

        btnCategoty15Up.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GetPloggings.class);
            startActivity(intent);
        });

        btnCategoryApprove.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GetPloggings.class);
            startActivity(intent);
        });

        btnCategoryDirect.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GetPloggings.class);
            startActivity(intent);
        });

        //TODO: 서버에서 인기 플로워 가져오기
        //TODO: 가져온 값을 하나씩 버튼에 연결하기

        //6. 인기 플로워
        btnPlower1.setOnClickListener(v -> {

        });

        btnPlower2.setOnClickListener(v -> {

        });

        btnPlower3.setOnClickListener(v -> {

        });

        btnPlower4.setOnClickListener(v -> {

        });
    }
}
