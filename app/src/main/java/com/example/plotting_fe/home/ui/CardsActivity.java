package com.example.plotting_fe.home.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;

import java.util.ArrayList;
import java.util.List;

public class CardsActivity extends AppCompatActivity {

    private RecyclerView imgCardNewsRecyclerView;
    private TextView titleTextView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_cardnews);

        imgCardNewsRecyclerView = findViewById(R.id.img_cardnews);
        titleTextView = findViewById(R.id.title);
        backButton = findViewById(R.id.ic_back);

        String cardTitle = "[#1024번:공공카드뉴스] \n 일회용품 사용 줄이기 캠페인!";
        titleTextView.setText(cardTitle);

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://plower.s3.ap-northeast-2.amazonaws.com/plastic/zeroPlastic_1");
        imageUrls.add("https://plower.s3.ap-northeast-2.amazonaws.com/plastic/zeroPlastic_2");
        imageUrls.add("https://plower.s3.ap-northeast-2.amazonaws.com/plastic/zeroPlastic_3");

        imgCardNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CardsAdapter adapter = new CardsAdapter(this, imageUrls);
        imgCardNewsRecyclerView.setAdapter(adapter);

        backButton.setOnClickListener(view -> finish());
    }
}
