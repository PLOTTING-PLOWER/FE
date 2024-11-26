package com.example.plotting_fe.plogging.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

import java.util.ArrayList;
import java.util.List;

public class GetPloggings extends AppCompatActivity {

    private ImageView addBtn, searchBtn, filterBtn;
    private EditText searchInput;
    private List<PloggingResponse> ploggingList;
    private RecyclerView recyclerView;
    private GetPloggingAdapter getPloggingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_fragment_filterplogging);

        addBtn = findViewById(R.id.btn_add_plogging);
        searchInput = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        filterBtn = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.plogging_list);

        ploggingList = new ArrayList<>();

        // RecyclerView에 어댑터 연결 위한 RecyclerView 초기화
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getPloggingAdapter = new GetPloggingAdapter(ploggingList);
        recyclerView.setAdapter(getPloggingAdapter);

        // 버튼 모음
        buttons();

        Intent intent = getIntent();
        int indexCount = intent.getIntExtra("indexCount", 0); // 총 개수 전달받기
        for (int index = 0; index < indexCount; index++) {
            Long ploggingId = Long.parseLong(intent.getStringExtra("ploggingId_" + index));
            String title = intent.getStringExtra("ploggingTitle_" + index);
            int currentPeople = intent.getIntExtra("ploggingCurrentPeople_" + index, 0);
            int maxPeople = intent.getIntExtra("ploggingMaxPeople_" + index, 0);
            String ploggingType = intent.getStringExtra("ploggingType_" + index);
            String recruitEndDate = intent.getStringExtra("ploggingRecruitEndDate_" + index);
            String startTime = intent.getStringExtra("ploggingStartTime_" + index);
            long spendTime = intent.getLongExtra("ploggingSpendTime_" + index, 0L);
            String startLocation = intent.getStringExtra("ploggingStartLocation_" + index);

            PloggingResponse plogging = new PloggingResponse(
                    ploggingId,
                    title,
//                    currentPeople,
                    maxPeople,
                    ploggingType,
                    recruitEndDate,
                    startTime,
                    spendTime,
                    startLocation
            );
            ploggingList.add(plogging);
        }

        // intent로 받은 데이터를 RecyclerView로 넘기기
        getPloggingAdapter.updateDataList(ploggingList);
        getPloggingAdapter.notifyDataSetChanged();
    }

    private void buttons() {
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
            String searchTitle = searchInput.getText().toString().trim();
            if (searchTitle.isEmpty()) {
                Toast.makeText(GetPloggings.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                searchPlogging(searchTitle);
            }
        });
    }

    private void searchPlogging(String searchTitle) {
        PloggingApiService ploggingApiService = new PloggingApiService();
        ploggingApiService.getPloggingWithTitle(searchTitle, this);
    }
}