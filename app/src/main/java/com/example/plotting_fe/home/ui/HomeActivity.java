package com.example.plotting_fe.home.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnCardnews;
    private ImageView btnCatagotyToday, btnCatagoty15Up, btnCatagotyApprove, btnCatagotyRandom;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_home);

        btnCardnews = findViewById(R.id.btn_read_more);
        btnCatagotyToday = findViewById(R.id.categoty_today_finish);
        btnCatagoty15Up = findViewById(R.id.categoty_15_up);
        btnCatagotyApprove = findViewById(R.id.category_approve);
        btnCatagotyRandom = findViewById(R.id.category_random);

//        btnCardnews.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CardnewsListActivity.class);
//            startActivity(intent);
//        });
    }
}