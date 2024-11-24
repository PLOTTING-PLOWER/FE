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
    private LocalDateTime startDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_fragment_filterplogging);

        addBtn = findViewById(R.id.btn_add_plogging);
        searchInput = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        filterBtn = findViewById(R.id.filter);

        recyclerView = findViewById(R.id.plogging_list);

        //초기화 작업
        ploggingList = new ArrayList<>();

        //recyclerview에 adapter 연결
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

//  파라미터 : region: String, startDat
//  e: LocalDate, endDate: LocalDate, type: String, spendTime: Long,
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String startDateTimeStr = startDateStr + " " + startTimeStr; // 예시)"2024-11-30 13:00"

                // LocalDateTime으로 변환
                DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                startDateTime = LocalDateTime.parse(startDateTimeStr, localDateTimeFormatter);

                Log.d("Debug", "{After}-> Formatted startDateTime: " + startDateTime);
            }
            spendTime = Long.parseLong(timeStr);
            Log.d("Debug", "spendTime: " + spendTime);
            participants = Long.parseLong(participantsStr);
            Log.d("Debug", "participants: " + participants);

            //받은 후에 바로 필터링 서버 연결
            filterPlogging();

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

    // TODO: 플로깅 필터링 서버 연결
    private void filterPlogging() {
        //필터 연결
        PloggingApiService ploggingApiService = new PloggingApiService();

// 파라미터 순서 : region, startDate, endDate, type, spendTime, startTime, maxPeople
        ploggingApiService.filterPlogging(region, startDate, endDate, meetingType, spendTime, startDateTime, participants, GetPloggings.this, recyclerView, ploggingAdapter);
    }

//    private void loadPloggings() {
//        List<PloggingResponse> mockData = new ArrayList<>();
//        mockData.add(new PloggingResponse(1L, "Seoul Plogging", 3, 10, PloggingType.ASSIGN, 2024-12-24, 13:00, 120, "분당구 불정로 6"));
//        ploggingAdapter.updatePloggingList(mockData);
//    }
}