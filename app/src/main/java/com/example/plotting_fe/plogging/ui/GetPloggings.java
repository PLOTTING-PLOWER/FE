package com.example.plotting_fe.plogging.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.R;

public class GetPloggings extends AppCompatActivity {

    private ImageView addBtn, searchBtn, filterBtn;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_fragment_filterplogging);

        addBtn = findViewById(R.id.btn_add_plogging);
        searchInput = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        filterBtn = findViewById(R.id.filter);

        // 추가 버튼 누르면 플로깅_모임_생성_화면 으로 이동
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GetPloggings.this, PloggingMakeActivity1.class);
                startActivity(intent);
            }
        });

        //필터링 버튼을 누르면
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GetPloggings.this, PloggingFilter.class);
                startActivity(intent);
            }
        });

        //돋보기 버튼을 누르면 searchInput에 입력한 title의 플로깅을 검색하여 나타내게 된다
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchInput.getText().toString().trim();

                if (searchQuery.isEmpty()) {
                    Toast.makeText(GetPloggings.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 검색 로직 구현

                }
            }
        });
    }
}

