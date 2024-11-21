package com.example.plotting_fe.plogging.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.ResponseTemplate;
import com.example.plotting_fe.global.util.ApiClient;
import com.example.plotting_fe.plogging.dto.PloggingType;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.presentation.PloggingController;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPloggings extends AppCompatActivity {

    private ImageView addBtn, searchBtn, filterBtn;
    private EditText searchInput;
    private PloggingAdapter ploggingAdapter;
    private List<PloggingResponse> ploggingList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_fragment_filterplogging);

        addBtn = findViewById(R.id.btn_add_plogging);
        searchInput = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        filterBtn = findViewById(R.id.filter);

        // ploggingList 초기화
        ploggingList = new ArrayList<>();

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.plogging_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ploggingAdapter = new PloggingAdapter(this, ploggingList);
        recyclerView.setAdapter(ploggingAdapter);

        // 추가 버튼 클릭 시 플로깅 모임 생성 화면으로 이동
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetPloggings.this, PloggingMakeActivity1.class);
            startActivity(intent);
        });

        // 필터링 버튼 클릭 시 필터 화면으로 이동
        filterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetPloggings.this, PloggingFilter.class);
            startActivity(intent);
        });

        // 검색 버튼 클릭 시 입력한 제목으로 플로깅 검색
        searchBtn.setOnClickListener(v -> {
            String searchQuery = searchInput.getText().toString().trim();
            if (searchQuery.isEmpty()) {
                Toast.makeText(GetPloggings.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                searchPloggings(searchQuery);
            }
        });
    }

    private void searchPloggings(String query) {
        // 기존 목록 초기화
        ploggingList.clear();

        // 필터링 조건을 null로 설정하여 모든 데이터를 가져옴
        LocalDate startDate = null; // 3일 전 날짜
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startDate = LocalDate.now().minusDays(3);
        }
        LocalDate endDate = null; // 현재 날짜
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endDate = LocalDate.now();
        }
        LocalDateTime startTime = null; // 시작 시간 없음
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startTime = LocalDateTime.now();
        }
        String region = "Seoul"; // 모든 지역
        String typeString = "DIRECT"; // 특정 타입
        long spendTime = 5; // 최소 소요 시간
        long maxPeople = 10; // 최대 인원 수

        // 변수 값 로그 출력
        Log.d("FilterInputs", "region: " + region);
        Log.d("FilterInputs", "startDate: " + startDate);
        Log.d("FilterInputs", "endDate: " + endDate);
        Log.d("FilterInputs", "typeString: " + typeString);
        Log.d("FilterInputs", "spendTime: " + spendTime);
        Log.d("FilterInputs", "startTime: " + startTime);
        Log.d("FilterInputs", "maxPeople: " + maxPeople);

        // request를 위한 타입 변환 과정
        PloggingType type = null;
        if (typeString != null) {
            try {
                type = PloggingType.valueOf(typeString.toUpperCase());
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "잘못된 유형 값입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 서버 연결하기
        assert startTime != null;

        //Todo : MalformedJsonException 발생 해서 해결하기
        ApiClient.INSTANCE.getApiClient().create(PloggingController.class)
                .findListByFilter(region, startDate, endDate, type, spendTime, startTime, maxPeople)
                .enqueue(new Callback<ResponseTemplate<List<PloggingResponse>>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(Call<ResponseTemplate<List<PloggingResponse>>> call, Response<ResponseTemplate<List<PloggingResponse>>> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                ResponseTemplate<List<PloggingResponse>> responseTemplate = response.body();
                                if (responseTemplate.isSuccess() != null && responseTemplate.isSuccess()) {
                                    if (responseTemplate.getResults() != null) {
                                        ploggingList.addAll(responseTemplate.getResults());
                                    }
                                    ploggingAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(GetPloggings.this, responseTemplate.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(GetPloggings.this, "서버 오류 발생", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("GetPloggings", "오류 발생: " + e.getMessage(), e);
                            Toast.makeText(GetPloggings.this, "오류 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<List<PloggingResponse>>> call, Throwable t) {
                        if (t instanceof java.net.ConnectException) {
                            Log.e("NetworkError", "서버에 연결할 수 없습니다: " + t.getMessage(), t);
                        } else if (t instanceof java.net.UnknownHostException) {
                            Log.e("NetworkError", "호스트를 찾을 수 없습니다: " + t.getMessage(), t);
                        } else {
                            Log.e("NetworkError", "알 수 없는 오류 발생: " + t.getMessage(), t);
                        }

                        Toast.makeText(GetPloggings.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
