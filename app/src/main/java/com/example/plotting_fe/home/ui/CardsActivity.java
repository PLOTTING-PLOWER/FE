package com.example.plotting_fe.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.ResponseTemplate;
import com.example.plotting_fe.global.util.ApiClient;
import com.example.plotting_fe.home.dto.response.CardResponse;
import com.example.plotting_fe.home.dto.response.CardResponseList;
import com.example.plotting_fe.home.presentation.HomeController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsActivity extends AppCompatActivity {

    private RecyclerView imgCardnews;
    private TextView title;
    private CardsAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_cardnews);

        imgCardnews = findViewById(R.id.img_cardnews);
        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.ic_back);


        Intent intent = getIntent();
        String cardIdStr = intent.getStringExtra("cardId");
        long cardId = Long.parseLong(cardIdStr);
        loadCardnews(cardId);

        imgCardnews.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadCardnews(Long cardnewsId) {
        HomeController homeController = ApiClient.INSTANCE.getApiClient().create(HomeController.class);

        // API 호출
        homeController.getCard(cardnewsId).enqueue(new Callback<ResponseTemplate<CardResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<CardResponseList>> call, Response<ResponseTemplate<CardResponseList>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.d("API_RESPONSE", "Response Body: " + response.body());
                    // 응답에서 데이터 가져오기
                    CardResponseList cardList = response.body().getResults();

                    // 디버깅용 로그
                    if (cardList != null) {
                        Log.d("cardnews", "cardnews result: " + cardList);
                    }

                    if (cardList != null && cardList.getCardResponseList() != null) {
                        // cardnewsId로 데이터를 필터링
                        CardResponse selectedCard = null;
                        for (CardResponse card : cardList.getCardResponseList()) {
                            if (card.getCardId() == cardnewsId) {
                                selectedCard = card;
                                break;
                            }
                        }

                        if (selectedCard != null) {
                            // 제목 표시
                            title.setText(selectedCard.getCardTitle());

                            // RecyclerView 어댑터 설정
                            adapter = new CardsAdapter(selectedCard.getCardUrls());
                            imgCardnews.setAdapter(adapter);
                        } else {
                            title.setText("해당 카드 뉴스를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    Log.e("cardnews", "Response is unsuccessful or empty");
                }
            }

            @Override
            public void onFailure(Call<ResponseTemplate<CardResponseList>> call, Throwable t) {
                Log.e("cardnews", "Failed to fetch data", t);
                title.setText("데이터를 불러오는 데 실패했습니다.");
            }
        });
    }
}
