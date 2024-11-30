package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.HomeResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.dto.response.PlowerResponse;
import com.example.plotting_fe.plogging.ui.GetPloggings;
import com.example.plotting_fe.plogging.ui.PloggingApiService;
import com.example.plotting_fe.utils.UIUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private Button btnCardnews;
    private ImageView btnCatagotyToday, btnCatagoty15Up, btnCatagotyApprove, btnCatagotyRandom;

    private Button btnRead;
    private ImageView btnCategotyTodayWillFinish, btnCategoty15Up, btnCategoryApprove, btnCategoryDirect;
    private ImageView btnPlower1, btnPlower2, btnPlower3, btnPlower4, userProfile, btnRanking, btnAlarm, btnSearch;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private TextView btnPlowerName1, btnPlowerName2, btnPlowerName3, btnPlowerName4, userNickname;
    private List<PloggingResponse> dataList = new ArrayList<>();

    private HomeApiService homeApiService;
    private PloggingApiService ploggingApiService;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment의 레이아웃을 인플레이트
        View view = inflater.inflate(R.layout.a_activity_home, container, false);

        // 버튼 초기화
        btnCardnews = view.findViewById(R.id.btn_read_more);
        btnCatagotyToday = view.findViewById(R.id.categoty_today_finish);
        btnCatagoty15Up = view.findViewById(R.id.categoty_15_up);
        btnCatagotyApprove = view.findViewById(R.id.category_approve);
        btnCatagotyRandom = view.findViewById(R.id.category_random);
        btnRanking = view.findViewById(R.id.welcome_rank);
        btnAlarm = view.findViewById(R.id.welcome_alarm);

        // 버튼 클릭 리스너 설정
        btnCardnews.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CardnewsListActivity.class);
            startActivity(intent);
        });

        //
        btnRanking.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_home_to_ranking);
        });

        // 알람 리스트 이동
        btnAlarm.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), AlarmActivity.class);
            startActivity(intent);
        });

        // 뷰 초기화
        initializeViews(view);

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Edge-to-Edge 모드 활성화
        if (getActivity() instanceof ComponentActivity) {
            UIUtils.enableEdgeToEdgeMode((ComponentActivity) getActivity());
        }

        // 네비게이션 셋업 (Handler로 지연 호출)
        new Handler(Looper.getMainLooper()).post(() -> {
            if (getActivity() instanceof ComponentActivity) {
                UIUtils.setupBottomNavigation((ComponentActivity) getActivity());
            }
        });
    }

    private void initializeViews(View view) {
        userProfile = view.findViewById(R.id.welcome_image);
        userNickname = view.findViewById(R.id.welcome_nickname);
        btnRead = view.findViewById(R.id.btn_read_more);
        btnCategotyTodayWillFinish = view.findViewById(R.id.categoty_today_finish);
        btnCategoty15Up = view.findViewById(R.id.categoty_15_up);
        btnCategoryApprove = view.findViewById(R.id.category_approve);
        btnCategoryDirect = view.findViewById(R.id.category_random);
        recyclerView = view.findViewById(R.id.rv_popular_plogging);
        btnPlower1 = view.findViewById(R.id.popular_plowers_first);
        btnPlower2 = view.findViewById(R.id.popular_plowers_second);
        btnPlower3 = view.findViewById(R.id.popular_plowers_third);
        btnPlower4 = view.findViewById(R.id.popular_plowers_fourth);
        btnPlowerName1 = view.findViewById(R.id.plower_name_first);
        btnPlowerName2 = view.findViewById(R.id.plower_name_second);
        btnPlowerName3 = view.findViewById(R.id.plower_name_third);
        btnPlowerName4 = view.findViewById(R.id.plower_name_fourth);
        btnRanking = view.findViewById(R.id.welcome_rank);
        btnAlarm = view.findViewById(R.id.welcome_alarm);
        btnSearch = view.findViewById(R.id.welcome_search);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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
            v.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.button_color_in_home));
            startActivity(new Intent(getActivity(), CardnewsListActivity.class));
        });

        btnRanking.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RankingFragment.class));
        });

        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), GetPloggings.class));
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
            ploggingApiService.filterPlogging(region, "2024-01-01", endDate, "DIRECT", 1L, LocalDateTime.now().toString(), 1000L, getActivity());
        });

        btnCategoty15Up.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "DIRECT", 1L, "2024-01-01T01:00:00", 15L, getActivity());
        });

        btnCategoryApprove.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "APPROVE", 1L, "2024-01-01T01:00:00", 1000L, getActivity());
        });

        btnCategoryDirect.setOnClickListener(v -> {
            String region = "Seoul";
            ploggingApiService.filterPlogging(region, "2024-01-01", "2025-01-01", "DIRECT", 1L, "2024-01-01T01:00:00", 1000L, getActivity());
        });
    }

    private void getHome() {
        homeApiService = new HomeApiService();
        homeApiService.getHome(new HomeResponseListener() {
            @Override
            public void onHomeDataReceived(HomeResponse homeResponse) {
                Toast.makeText(getActivity(), "반갑습니다!", Toast.LENGTH_SHORT).show();
                Log.d("MainFragment", "Home data received: " + homeResponse.toString());
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
                Log.e("MainFragment", "Error fetching home data: " + errorMessage);
                Toast.makeText(getActivity(), "실패!: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}