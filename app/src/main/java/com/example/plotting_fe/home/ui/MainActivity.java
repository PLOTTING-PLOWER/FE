package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.HomeResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.dto.response.PlowerListResponse;
import com.example.plotting_fe.plogging.ui.GetPloggings;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnRead;
    private ImageView btnCategotyTodayWillFinish, btnCategoty15Up, btnCategoryApprove, btnCategoryDirect;
    private ImageView btnPlower1, btnPlower2, btnPlower3, btnPlower4;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<PloggingResponse> dummyData;  // List<String>에서 List<PloggingResponse>로 변경
    private TextView btnPlowerName1, btnPlowerName2, btnPlowerName3, btnPlowerName4;

    private HomeApiService homeApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        // 뷰 초기화
        initializeViews();

        // 리사이클러뷰 레이아웃 매니저 설정
        setupRecyclerView();

        // 서버에서 홈 데이터 가져오기
        getHome();

        // 더미 데이터 추가 (TODO: 서버 데이터로 변경 필요)
        setDummyData();

        // 어댑터 설정
        setupAdapter();

        // 버튼 클릭 이벤트 설정
        setupButtonListeners();
    }

    private void initializeViews() {
        btnRead = findViewById(R.id.btn_read_more);
        btnCategotyTodayWillFinish = findViewById(R.id.categoty_today_finish);
        btnCategoty15Up = findViewById(R.id.categoty_15_up);
        btnCategoryApprove = findViewById(R.id.category_approve);
        btnCategoryDirect = findViewById(R.id.category_random);
        recyclerView = findViewById(R.id.rv_popular_plogging);
        btnPlower1 = findViewById(R.id.popular_plowers_first);
        btnPlower2 = findViewById(R.id.popular_plowers_second);
        btnPlower3 = findViewById(R.id.popular_plowers_third);
        btnPlower4 = findViewById(R.id.popular_plowers_fourth);
        btnPlowerName1 = findViewById(R.id.plower_name_first);
        btnPlowerName2 = findViewById(R.id.plower_name_second);
        btnPlowerName3 = findViewById(R.id.plower_name_third);
        btnPlowerName4 = findViewById(R.id.plower_name_fourth);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setDummyData() {
        dummyData = new ArrayList<PloggingResponse>();

        // PloggingResponse 객체를 생성하여 리스트에 추가
        dummyData.add(new PloggingResponse(
                1L, "플로깅1", 5L, 10L, "ASSIGN",
                "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"
        ));

        dummyData.add(new PloggingResponse(
                2L, "플로깅 2", 3L, 8L, "ASSIGN",
                "2024-12-25", "2024-12-20T09:30:00", 90L, "Busan"
        ));

        dummyData.add(new PloggingResponse(
                3L, "플로깅 3", 7L, 12L, "ASSIGN",
                "2024-12-10", "2024-12-05T08:00:00", 150L, "Incheon"
        ));

        dummyData.add(new PloggingResponse(
                4L, "플로깅 4", 2L, 5L, "ASSIGN",
                "2024-11-30", "2024-11-25T11:00:00", 60L, "Gyeongju"
        ));
    }

    private void setupAdapter() {
        // 더미 데이터 설정
        setDummyData();

        // HomeAdapter에 더미 데이터를 전달하고 RecyclerView에 설정
        homeAdapter = new HomeAdapter(dummyData);
        recyclerView.setAdapter(homeAdapter);
    }

    private void setupButtonListeners() {
        btnRead.setOnClickListener(v -> {
            v.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_color_in_home));
            startActivity(new Intent(this, CardnewsListActivity.class));
        });

        // 카테고리 버튼 연결 모아놓은 메서드
        setupCategoryButtons();
    }

    private void setupCategoryButtons() {
        btnCategotyTodayWillFinish.setOnClickListener(v -> startCategoryActivity());
        btnCategoty15Up.setOnClickListener(v -> startCategoryActivity());
        btnCategoryApprove.setOnClickListener(v -> startCategoryActivity());
        btnCategoryDirect.setOnClickListener(v -> startCategoryActivity());
    }

    private void startCategoryActivity() {
        Intent intent = new Intent(this, GetPloggings.class);
        startActivity(intent);
    }



    // 홈에 각 데이터 맵핑함
    private void getHome() {
        homeApiService = new HomeApiService();
        homeApiService.getHome(new HomeResponseListener() {
            @Override
            public void onHomeDataReceived(HomeResponse homeResponse) {
                Toast.makeText(MainActivity.this, "반갑습니다!", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Home data received: " + homeResponse.toString());
            }

            @Override
            public void onPloggingDataReceived(List<PloggingResponse> ploggingResponseList) {
                Log.d("MainActivity", "Plogging data received: " + ploggingResponseList);

                if (homeAdapter != null) {
                    homeAdapter.updateDataList(ploggingResponseList);  // 데이터 업데이트
                }
            }

            @Override
            public void onPlowerDataReceived(PlowerListResponse plowerListResponse) {
                Log.d("MainActivity", "Plower data received: " + plowerListResponse);

                // Plower 목록 가져오기
                List<PlowerListResponse.PloggingUser> plowerList = plowerListResponse.getPloggingUserList();

                // 각 플로워에 대해 처리
                for (int index = 0; index < plowerList.size(); index++) {
                    PlowerListResponse.PloggingUser plower = plowerList.get(index);

                    switch (index) {
                        case 0:
                            // 첫 번째 플로워 설정
                            btnPlowerName1.setText(plower.getNickname());
                            Glide.with(getApplicationContext())
                                    .load(plower.getProfileImageUrl())
                                    .placeholder(R.drawable.ic_icon_round)
                                    .into(btnPlower1);
                            break;
                        case 1:
                            // 두 번째 플로워 설정
                            btnPlowerName2.setText(plower.getNickname());
                            Glide.with(getApplicationContext())
                                    .load(plower.getProfileImageUrl())
                                    .placeholder(R.drawable.ic_icon_round)
                                    .into(btnPlower2);
                            break;
                        case 2:
                            // 세 번째 플로워 설정
                            btnPlowerName3.setText(plower.getNickname());
                            Glide.with(getApplicationContext())
                                    .load(plower.getProfileImageUrl())
                                    .placeholder(R.drawable.ic_icon_round)
                                    .into(btnPlower3);
                            break;
                        case 3:
                            // 네 번째 플로워 설정
                            btnPlowerName4.setText(plower.getNickname());
                            Glide.with(getApplicationContext())
                                    .load(plower.getProfileImageUrl())
                                    .placeholder(R.drawable.ic_icon_round)
                                    .into(btnPlower4);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                // 오류 발생 시 처리
                Log.e("MainActivity", "Error fetching home data: " + errorMessage);
                Toast.makeText(MainActivity.this, "실패!: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
