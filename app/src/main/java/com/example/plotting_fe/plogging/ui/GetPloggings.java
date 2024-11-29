package com.example.plotting_fe.plogging.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.ui.MainActivity;
import com.example.plotting_fe.mypage.ui.MypageFragment;
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        setupBottomNavigationView();
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
        ploggingApiService.getPloggingWithTitle(searchTitle, new PloggingResponseListener() {
            @Override
            public void onPloggingResponse(PloggingResponse ploggingResponse) {
                List<PloggingResponse> ploggingList = new ArrayList<>();
                ploggingList.add(ploggingResponse);

                if (getPloggingAdapter != null) {
                    getPloggingAdapter.updateDataList(ploggingList);
                } else {
                    Log.d("PloggingSearch", "어댑터가 초기화되지 않았습니다.");
                }
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_plogging) { // 1.홈
            } else if (itemId == R.id.navigation_home) { //2. 플로깅 조회
                startActivity(new Intent(GetPloggings.this, MainActivity.class));
            } else if (itemId == R.id.navigation_map) { //3. 지도
                startActivity(new Intent(GetPloggings.this, PloggingMapActivity.class));
            } else if (itemId == R.id.navigation_steps) { //4. 내 걸음
                startActivity(new Intent(GetPloggings.this, MyPloggingHomeActivity.class));
            } else { //5. 내 정보
                openFragment(new MypageFragment()); //MypageFragment로 이동
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
}