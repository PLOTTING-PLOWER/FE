package com.example.plotting_fe.plogging.ui;

import static java.util.Collections.emptyList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GetPloggings extends AppCompatActivity {

    private ImageView addBtn, searchBtn, filterBtn;
    private EditText searchInput;
    private PloggingAdapter ploggingAdapter;
    private List<PloggingResponse> ploggingList;
    private RecyclerView recyclerView;

    private String region, startDateStr, endDateStr, meetingType, timeStr, startTimeStr, participantsStr;
    private Long spendTime, participants;
    private LocalDate startDate, endDate;
    private LocalTime startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_fragment_filterplogging);

        addBtn = findViewById(R.id.btn_add_plogging);
        searchInput = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        filterBtn = findViewById(R.id.filter);

        //초기화 작업
        ploggingList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ploggingAdapter = new PloggingAdapter(this, ploggingList);
        recyclerView.setAdapter(ploggingAdapter);

        //  filterPlogging 에서 넘겨받은 값
        region = getIntent().getStringExtra("region");
        startDateStr = getIntent().getStringExtra("startDate");
        endDateStr = getIntent().getStringExtra("endDate");
        meetingType = getIntent().getStringExtra("meetingType");
        timeStr = getIntent().getStringExtra("time");
        startTimeStr = getIntent().getStringExtra("startTime");
        participantsStr = getIntent().getStringExtra("participants");

//  파라미터 : region: String, startDate: LocalDate, endDate: LocalDate, type: String, spendTime: Long,
//  타입 바꾸기 startTime: LocalDateTime,  maxPeople: Long
        try {
            DateTimeFormatter dateFormatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            }

            // 날짜 타입 변경
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = LocalDate.parse(startDateStr, dateFormatter);
                Log.d("Debug", "startDate: " + startDate);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                endDate = LocalDate.parse(endDateStr, dateFormatter);
                Log.d("Debug", "endDate: " + endDate);
            }

            DateTimeFormatter dateTimeFormatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");   // FIXME : LocalTime으로 변경
            }

            if (startTimeStr != null && !startTimeStr.contains(":")) {
                startTimeStr = startTimeStr + ":00";
                Log.d("Debug", "{Before}-> Formatted startTimeStr: " + startTimeStr);
            }
            // 시간 타입 변경
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startTime = LocalTime.parse(startTimeStr, dateTimeFormatter);
                Log.d("Debug", "{After}-> Formatted startTime: " + startTime);
            }

            spendTime = Long.parseLong(timeStr);
            Log.d("Debug", "spendTime: " + spendTime);
            participants = Long.parseLong(participantsStr);
            Log.d("Debug", "participants: " + participants);


        } catch (Exception e) {
            Log.e("Debug", "in {GetPloggings + FilterPloggings} Error : ", e);
            Toast.makeText(this, "필터 값 변환 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }

        //추가 버튼
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetPloggings.this, PloggingMakeActivity1.class);
            startActivity(intent);
        });

        //필터링 버튼
        filterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetPloggings.this, PloggingFilter.class);
            startActivity(intent);
        });

        // 검색 버튼
        searchBtn.setOnClickListener(v -> {
            String searchQuery = searchInput.getText().toString().trim();
            if (searchQuery.isEmpty()) {
                Toast.makeText(GetPloggings.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
//                searchPloggings(searchQuery);
            }
        });
    }

    // TODO: 플로깅 필터링
    private void filterPlogging() {
        //필터 연결
        PloggingApiService ploggingApiService = new PloggingApiService();

// 파라미터 순서 : region, startDate, endDate, type, spendTime, startTime, maxPeople
        ploggingApiService.filterPlogging(region, startDate, endDate, meetingType, spendTime, startTime, participants, GetPloggings.this);
    }

//    // TODO: 플로깅 검색
//    private void searchPloggings(String query) {
//        // 기존 목록 초기화
//        ploggingList.clear();
//
//        // 필터링 조건을 null로 설정하여 모든 데이터를 가져옴
//        LocalDate startDate = null; // 3일 전 날짜
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            startDate = LocalDate.now().minusDays(3);
//        }
//        LocalDate endDate = null; // 현재 날짜
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            endDate = LocalDate.now();
//        }
//        LocalDateTime startTime = null; // 시작 시간 없음
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            startTime = LocalDateTime.now();
//        }
//        String region = "Seoul"; // 모든 지역
//        String typeString = "DIRECT"; // 특정 타입
//        long spendTime = 5; // 최소 소요 시간
//        long maxPeople = 10; // 최대 인원 수
//
//        // 변수 값 로그 출력
//        Log.d("FilterInputs", "region: " + region);
//        Log.d("FilterInputs", "startDate: " + startDate);
//        Log.d("FilterInputs", "endDate: " + endDate);
//        Log.d("FilterInputs", "typeString: " + typeString);
//        Log.d("FilterInputs", "spendTime: " + spendTime);
//        Log.d("FilterInputs", "startTime: " + startTime);
//        Log.d("FilterInputs", "maxPeople: " + maxPeople);
//
//        // request를 위한 타입 변환 과정
//        PloggingType type = null;
//        if (typeString != null) {
//            try {
//                type = PloggingType.valueOf(typeString.toUpperCase());
//                Log.d("FilterInputs22", type.toString());
//            } catch (IllegalArgumentException e) {
//                Toast.makeText(this, "잘못된 유형 값입니다.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }

//        // TODO : 서버 연결하기
//        assert startTime != null;
//
//        ApiClient.INSTANCE.getApiClient().create(PloggingController.class)
//                .findListByFilter(region, startDate, endDate, type, spendTime, startTime, maxPeople)
//                .enqueue(new Callback<ResponseTemplate<PloggingListResponse>>() {
//
//                    @Override
//                    public void onResponse(Call<ResponseTemplate<PloggingListResponse>> call, Response<ResponseTemplate<PloggingListResponse>> response) {
//                        try {
//                            if (response.isSuccessful() && response.body() != null) {
//                                ResponseTemplate<PloggingListResponse> responseTemplate = response.body();
//
//                                if (responseTemplate.isSuccess() != null && responseTemplate.isSuccess()) {
//                                    if (responseTemplate.getResults() != null) {
//                                        ploggingList.addAll(responseTemplate.getResults());
//                                    }
//                                    ploggingAdapter.notifyDataSetChanged();
//                                } else {
//                                    Toast.makeText(GetPloggings.this, responseTemplate.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(GetPloggings.this, "서버 오류 발생", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            Log.d("fail", "GetPloggings " + e.getMessage(), e);
//                            Toast.makeText(GetPloggings.this, "오류 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseTemplate<PloggingListResponse>> call, Throwable t) {
//                        Log.d("fail", "GetPloggings " + t.getMessage(), t);
//                        Toast.makeText(GetPloggings.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

}
