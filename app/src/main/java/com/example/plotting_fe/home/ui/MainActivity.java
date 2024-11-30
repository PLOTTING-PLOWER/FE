package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.HomeResponse;
import com.example.plotting_fe.mypage.ui.MypageFragment;
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.dto.response.PlowerResponse;
import com.example.plotting_fe.plogging.ui.GetPloggings;
import com.example.plotting_fe.plogging.ui.PloggingApiService;
import com.example.plotting_fe.plogging.ui.PloggingMapActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // 네비게이션 바 설정
        setupBottomNavigationView();

        // 리사이클러뷰 레이아웃 매니저 설정
        setupRecyclerView();

        // 어댑터 설정
        setupAdapter();

        // 서버에서 홈 데이터 가져오기
        getHome();

        // 더미 데이터 추가 (TODO: 서버 데이터 존재시 지울 예정)
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

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) { // 1.홈
            } else if (itemId == R.id.navigation_plogging) { //2. 플로깅 조회
                startActivity(new Intent(MainActivity.this, GetPloggings.class));
            } else if (itemId == R.id.navigation_map) { //3. 지도
                startActivity(new Intent(MainActivity.this, PloggingMapActivity.class));
            } else if (itemId == R.id.navigation_steps) { //4. 내 걸음
                startActivity(new Intent(MainActivity.this, MyPloggingHomeActivity.class));
            } else if (itemId == R.id.navigation_mypage) {//5. 내 정보
                openFragment(new MypageFragment());
            }

            return true; // 이벤트 처리 완료
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // Fragment를 배치할 FrameLayout의 ID
                .addToBackStack(null) // 뒤로 가기 스택에 추가
                .commit();
    }



    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupAdapter() {
        homeAdapter = new HomeAdapter(dataList); // 어댑터 초기화
        recyclerView.setAdapter(homeAdapter);
    }

    private void setDummyData() {
        List<PloggingResponse> dummyData = new ArrayList<>();
        dummyData.add(new PloggingResponse(1L, "플로깅1", 5L, "ASSIGN", "2024-12-31", "2024-12-15T10:00:00", 120L, "Seoul"));
        dummyData.add(new PloggingResponse(2L, "플로깅2", 10L, "APPROVE", "2024-12-31", "2024-12-16T14:00:00", 150L, "Busan"));
        homeAdapter.updateDataList(dummyData);
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

        setupCategoryButtons();
    }

    private void setupCategoryButtons() {
        // 카테고리 버튼 초기화
        if (ploggingApiService == null) {
            ploggingApiService = new PloggingApiService();
        }

        btnCategotyTodayWillFinish.setOnClickListener(v -> {
            String region = "Seoul";
            String endDate = LocalDate.now().toString();
            ploggingApiService.filterPlogging(region, "2024-01-01", endDate, "DIRECT", 1L, LocalDateTime.now().toString(), 1000L, this);
        });

        btnCategoty15Up.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "DIRECT", 1L, "2024-01-01T01:00:00", 15L, this);
        });

        btnCategoryApprove.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "APPROVE", 1L, "2024-01-01T01:00:00", 1000L, this);
        });

        btnCategoryDirect.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "DIRECT", 1L, "2024-01-01T01:00:00", 1000L, this);
        });
    }

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
                homeAdapter.updateDataList(ploggingResponseList);
            }

            @Override
            public void onPlowerDataReceived(List<PlowerResponse> plowerList) {
                // Plower 데이터를 처리하는 로직
            }

            @Override
            public void onUserDataReceiver(String nickname) {
                userNickname.setText(nickname);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("MainActivity", "Error fetching home data: " + errorMessage);
                Toast.makeText(MainActivity.this, "실패!: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
