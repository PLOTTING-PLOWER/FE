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
import com.example.plotting_fe.home.dto.response.HomeResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.dto.response.PlowerResponse;
import com.example.plotting_fe.plogging.ui.GetPloggings;
import com.example.plotting_fe.plogging.ui.PloggingApiService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnRead;
    private ImageView btnCategotyTodayWillFinish, btnCategoty15Up, btnCategoryApprove, btnCategoryDirect;
    private ImageView btnPlower1, btnPlower2, btnPlower3, btnPlower4, userProfile, btnRanking, btnAlarm, btnSearch;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private TextView btnPlowerName1, btnPlowerName2, btnPlowerName3, btnPlowerName4, userNickname;
    private List<PloggingResponse> dataList = new ArrayList<>();

    private HomeApiService homeApiService;
    private PloggingApiService ploggingApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        // 뷰 초기화
        initializeViews();

        // 리사이클러뷰 레이아웃 매니저 설정
        setupRecyclerView();

        // 어댑터 설정
        setupAdapter();

        // 서버에서 홈 데이터 가져오기
        getHome();

        // 더미 데이터 추가 (TODO: 서버 데이터 존재시 지울예정 변경 필요)
        setDummyData();


        // 버튼 클릭 이벤트 설정
        setupButtonListeners();
    }

    private void initializeViews() {
        userProfile = findViewById(R.id.welcome_image);
        userNickname = findViewById(R.id.welcome_nickname);
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
        btnRanking = findViewById(R.id.welcome_rank);
        btnAlarm = findViewById(R.id.welcome_alarm);
        btnSearch = findViewById(R.id.welcome_search);

    }


    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setDummyData() {

        List<PloggingResponse> dummyData = new ArrayList<>();


        dummyData = new ArrayList<PloggingResponse>();

        // PloggingResponse 객체를 생성하여 리스트에 추가
        dummyData.add(new PloggingResponse(
                1L, "플로깅1", 5L, "ASSIGN",
                "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"
        ));

        dummyData.add(new PloggingResponse(
                1L, "플로깅1", 5L, "ASSIGN",
                "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"
        ));

        dummyData.add(new PloggingResponse(
                1L, "플로깅1", 5L, "ASSIGN",
                "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"
        ));

        dummyData.add(new PloggingResponse(
                1L, "플로깅1", 5L, "ASSIGN",
                "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"
        ));
        homeAdapter.updateDataList(dummyData);
    }

    private void setupAdapter() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(homeAdapter);
        homeAdapter = new HomeAdapter(dataList);  // 어댑터 초기화
        recyclerView.setAdapter(homeAdapter);
    }


    private void setupButtonListeners() {
        btnRead.setOnClickListener(v -> {
            v.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_color_in_home));
            startActivity(new Intent(this, CardnewsListActivity.class));
        });

        btnRanking.setOnClickListener(v -> {
            startActivity(new Intent(this, RankingFragment.class));
        });

        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, GetPloggings.class));
        });

//        // TODO : 알림 화면으로 intent
//        btnAlarm.setOnClickListener(v -> {
//            startActivity(new Intent(this, 알림 화면.class));
//        });

        // 카테고리 버튼 연결 모아놓은 메서드
        setupCategoryButtons();
    }


    //  파라미터 : region: String, startDate: LocalDate, endDate: LocalDate, type: String, spendTime: Long,
//  타입 바꾸기 startTime: LocalDateTime,  maxPeople: Long
    private void setupCategoryButtons() {
        // 한번만 초기화
        if (ploggingApiService == null) {
            ploggingApiService = new PloggingApiService();
        }

        btnCategotyTodayWillFinish.setOnClickListener(v -> {

            // 현재 날짜와 시간을 String으로 변환
            String endDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                endDate = LocalDate.now().toString();
            }
            String startTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startTime = LocalDateTime.now().toString();
            }
            String region = "Seoul";
            String startDate = "2024-01-01";
            String type = "DIRECT";
            Long spendTime = 1L;
            Long maxPeople = 1000L;
            startActivity(new Intent(MainActivity.this, GetPloggings.class));

            // 서버 호출하기
            ploggingApiService.filterPlogging(region, startDate, endDate, type, spendTime, startTime, maxPeople,
                    this);
        });

        btnCategoty15Up.setOnClickListener(v -> {
            String region = "Seoul";
            String startDate = "2024-01-01";
            String endDate = "2025-01-01";
            String type = "DIRECT";
            Long spendTime = 1L;
            String startTime = "2024-01-01T01:00:00";
            Long maxPeople = 15L;
            startActivity(new Intent(MainActivity.this, GetPloggings.class));

            // 서버 호출하기
            ploggingApiService.filterPlogging(region, startDate, endDate, type, spendTime, startTime, maxPeople,
                    this);

        });

        btnCategoryApprove.setOnClickListener(v -> {
            String region = "Seoul";
            String startDate = "2024-01-01";
            String endDate = "2025-01-01";
            String type = "APPROVE";
            Long spendTime = 1L;
            String startTime = "2024-01-01T01:00:00";
            Long maxPeople = 1000L;
            startActivity(new Intent(MainActivity.this, GetPloggings.class));

            // 서버 호출하기
            ploggingApiService.filterPlogging(region, startDate, endDate, type, spendTime, startTime, maxPeople,
                    this);
        });

        btnCategoryDirect.setOnClickListener(v -> {
            String region = "Seoul";
            String startDate = "2024-01-01";
            String endDate = "2025-01-01";
            String type = "DIRECT";
            Long spendTime = 1L;
            String startTime = "2024-01-01T01:00:00";
            Long maxPeople = 1000L;

            startActivity(new Intent(MainActivity.this, GetPloggings.class));

            // 서버 호출하기
            ploggingApiService.filterPlogging(region, startDate, endDate, type, spendTime, startTime, maxPeople,
                    this);
        });
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

            // 인기 플로깅
            @Override
            public void onPloggingDataReceived(List<PloggingResponse> ploggingResponseList) {
                Log.d("MainActivity", "Plogging data received: " + ploggingResponseList);

                if (homeAdapter != null) {
                    homeAdapter.updateDataList(ploggingResponseList);  // 데이터 업데이트
                }
            }

            // 인기 플로워
            @Override
            public void onPlowerDataReceived(List<PlowerResponse> PlowerResponse) {
                // Plower 목록 가져오기
                List<PlowerResponse> plowerList = PlowerResponse;
                Log.d("Plower", plowerList.toString());

                // 각 플로워에 대해 처리
                for (int index = 0; index < plowerList.size(); index++) {
                    PlowerResponse plower = plowerList.get(index);

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
            // 유저 이름
            @Override
            public void onUserDataReceiver(String nickname) {
                userNickname.setText(nickname);
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
