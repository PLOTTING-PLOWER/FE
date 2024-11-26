package com.example.plotting_fe.home.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.CardResponse;

import java.util.ArrayList;
import java.util.List;

public class CardsActivity extends AppCompatActivity implements CardsListener {

    private RecyclerView imgCardNewsRecyclerView;
    private TextView titleTextView;
    private ImageView backButton;
    private CardsAdapter cardsAdapter;
    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_cardnews);

        imgCardNewsRecyclerView = findViewById(R.id.img_cardnews);
        titleTextView = findViewById(R.id.title);
        backButton = findViewById(R.id.ic_back);

        imageUrls = new ArrayList<>();  // 초기 빈 리스트로 설정

        // 카드뉴스 ID를 받아옴
        String cardIdString = getIntent().getStringExtra("cardnewsId");
        Long cardnewsId = cardIdString != null ? Long.valueOf(cardIdString) : -1L;
        loadCards(cardnewsId);

        // RecyclerView 설정
        imgCardNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Adapter를 초기화
        cardsAdapter = new CardsAdapter(this, imageUrls);
        imgCardNewsRecyclerView.setAdapter(cardsAdapter);

        // 뒤로 가기 버튼 클릭 시 종료
        backButton.setOnClickListener(view -> finish());
    }

    private void loadCards(Long cardId) {
        HomeApiService homeApiService = new HomeApiService();
        homeApiService.loadCardnews(cardId, this);  // 카드뉴스 로드
    }

    @Override
    public void onCardsReceived(List<CardResponse> cardResponse) {
        if (cardResponse != null) {
            // 카드 뉴스 데이터가 존재하면
            String cardTitle = cardResponse.get(0).getCardTitle();
            List<String> cardUrls = cardResponse.get(0).getCardUrls();

            // 제목을 설정하고 이미지 URL 리스트 갱신
            titleTextView.setText(cardTitle);
            cardsAdapter.updateData(cardUrls);  // RecyclerView의 데이터를 갱신하는 메서드 호출
        } else {
            // 카드 뉴스 데이터가 없을 경우 처리
            titleTextView.setText("카드 뉴스 정보를 불러올 수 없습니다.");
            imgCardNewsRecyclerView.setAdapter(null);
        }
    }

    @Override
    public void onFailure() {
        titleTextView.setText("데이터를 불러오지 못했습니다.");
    }
}
