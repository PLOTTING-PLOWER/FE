package com.example.plotting_fe.plogging.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.R;

public class PloggingFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_filter);
    }

    //필터링 버튼 설정하고 완료 버튼 누르면 request 담아서 서버 보내고 해당 파라미터를 기준으로 조회 된 데이터를
    // 받아서 GetPloggings 에서 RecyclerView 설정한 plogging_list 에 보여준다

}
