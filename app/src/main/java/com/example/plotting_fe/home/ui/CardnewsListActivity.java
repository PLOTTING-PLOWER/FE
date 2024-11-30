package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.CardnewsResponse;
import com.example.plotting_fe.home.dto.response.CardnewsResponseList;

import java.util.ArrayList;
import java.util.List;


public class CardnewsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardnewsAdapter cardnewsAdapter;
    private ImageView btnBack;
    private HomeApiService homeApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_cardnews_list);

        recyclerView = findViewById(R.id.titleRecyclerView);
        btnBack = findViewById(R.id.ic_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainFragment.class));
        });

        loadCardnews();
    }

    private void loadCardnews() {
        homeApiService = new HomeApiService();
        homeApiService.loadCardnews(new CardnewsListener() {
            @Override
            public void onCardnewsReceived(CardnewsResponseList cardnewsResponseList) {

                setDummyData();
                cardnewsAdapter.updateData(cardnewsResponseList.getCardnewsResponseList());
                Toast.makeText(CardnewsListActivity.this, "카드뉴스 로딩 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDummyData() {
        List<CardnewsResponse> dummyCardnewsList = new ArrayList<>();

        // 더미 데이터 생성
        for (int i = 0; i < 3; i++) {
            CardnewsResponse cardnewsResponse = new CardnewsResponse(
                    (long) i,
                    "카드뉴스 제목 " + (i + 1)
            );
            dummyCardnewsList.add(cardnewsResponse);
        }

        CardnewsResponseList dummyCardnewsResponseList = new CardnewsResponseList(dummyCardnewsList);

        cardnewsAdapter = new CardnewsAdapter(CardnewsListActivity.this, dummyCardnewsResponseList.getCardnewsResponseList());
        recyclerView.setAdapter(cardnewsAdapter);

        cardnewsAdapter.updateData(dummyCardnewsResponseList.getCardnewsResponseList());

        Toast.makeText(CardnewsListActivity.this, "더미 카드뉴스 로딩 완료", Toast.LENGTH_SHORT).show();
    }
}

