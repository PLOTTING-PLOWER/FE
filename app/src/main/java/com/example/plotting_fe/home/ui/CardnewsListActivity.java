package com.example.plotting_fe.home.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.ResponseTemplate;
import com.example.plotting_fe.global.util.ApiClient;
import com.example.plotting_fe.home.dto.response.CardnewsResponseList;
import com.example.plotting_fe.home.presentation.HomeController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardnewsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardnewsAdapter cardnewsAdapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_cardnews_list);

        recyclerView = findViewById(R.id.titleRecyclerView);
        btnBack = findViewById(R.id.ic_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            finish();
        });

        loadCardnews();
    }

    private void loadCardnews() {
        HomeController homeController = ApiClient.INSTANCE.getApiClient().create(HomeController.class);
        homeController.getCardnews().enqueue(new Callback<ResponseTemplate<CardnewsResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<CardnewsResponseList>> call, Response<ResponseTemplate<CardnewsResponseList>> response) {
                // API 응답 로그 출력
                Log.d("CardnewsList", "Response Code: " + response.code());
                Log.d("CardnewsList", "Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    ResponseTemplate<CardnewsResponseList> responseTemplate = response.body();
                    if (responseTemplate.isSuccess() != null && responseTemplate.isSuccess()) {

                        CardnewsResponseList cardnewsResponseList = responseTemplate.getResults();
                        cardnewsAdapter = new CardnewsAdapter(CardnewsListActivity.this, cardnewsResponseList.getCardnewsResponseList());
                        recyclerView.setAdapter(cardnewsAdapter);

                        Toast.makeText(CardnewsListActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CardnewsListActivity.this, "실패!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 응답이 성공적이지 않을 때
                    Toast.makeText(CardnewsListActivity.this, "서버에서 응답을 받지 못함!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<CardnewsResponseList>> call, Throwable t) {
                Log.e("CardnewsList", "onFailure: " + t.getMessage(), t);
                Toast.makeText(CardnewsListActivity.this, "접속 조차 안됨!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
