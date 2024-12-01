package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.util.ClickUtil;

import java.util.Calendar;

public class PloggingMakeActivity1 extends AppCompatActivity {

    private EditText maxPeople, inputStartDate, inputEndDate;
    private String ploggingType;
    private ImageView btnBack;
    private LinearLayout btnArrival, btnApproval;
    private String recruitStartDate, recruitEndDate;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging1);

        btnArrival = findViewById(R.id.btn_time);   //1. 선착순 버튼
        btnApproval = findViewById(R.id.btn_approval);  //2. 승인제 버튼
        maxPeople = findViewById(R.id.input_participant_num);   //3. 모집 인원 수

        inputStartDate = findViewById(R.id.start_date); //4.모집 시작일
        inputEndDate = findViewById(R.id.end_date); //5.모집 마감일

        btnNext = findViewById(R.id.btn_next);  //6. 다음 버튼
        btnBack = findViewById(R.id.btn_back);  //0. 뒤로가기 버튼

        //버튼 색 초기화
        resetTimeButtons();

        // 0. 뒤로 가기 버튼
        ClickUtil.INSTANCE.onBackButtonClick(PloggingMakeActivity1.this, btnBack);

        // 선착순(btn_arrival) 버튼 선택
        btnArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnArrival.setBackgroundTintList(ContextCompat.getColorStateList(PloggingMakeActivity1.this, R.color.main));
                ploggingType = "DIRECT";

                // TextView 색상 변경
                TextView nameTextView = findViewById(R.id.nameTextView);
                TextView detailsTextView = findViewById(R.id.detailsTextView);
                TextView noticeTextView = findViewById(R.id.noticeTextView);

                nameTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.white));
                detailsTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.white));
                noticeTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.white));

                btnApproval.setBackgroundTintList(ContextCompat.getColorStateList(PloggingMakeActivity1.this, R.color.light_gray));

                // TextView 색상 변경
                TextView approvalTextView = findViewById(R.id.approvalTextView); // ID를 지정해 주세요
                TextView approvalDetailsTextView = findViewById(R.id.approvalDetailsTextView); // ID를 지정해 주세요

                approvalTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.gray));
                approvalDetailsTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.gray));

                Toast.makeText(PloggingMakeActivity1.this, "선착순을 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 승인제(btn_approval) 버튼 선택
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnApproval.setBackgroundTintList(ContextCompat.getColorStateList(PloggingMakeActivity1.this, R.color.main));
                ploggingType = "ASSIGN";

                // TextView 색상 변경
                TextView nameTextView = findViewById(R.id.nameTextView);
                TextView detailsTextView = findViewById(R.id.detailsTextView);
                TextView noticeTextView = findViewById(R.id.noticeTextView);

                nameTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.gray));
                detailsTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.gray));
                noticeTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.gray));

                btnArrival.setBackgroundTintList(ContextCompat.getColorStateList(PloggingMakeActivity1.this, R.color.light_gray));

                // TextView 색상 변경
                TextView approvalTextView = findViewById(R.id.approvalTextView); // ID를 지정해 주세요
                TextView approvalDetailsTextView = findViewById(R.id.approvalDetailsTextView); // ID를 지정해 주세요

                approvalTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.white));
                approvalDetailsTextView.setTextColor(ContextCompat.getColor(PloggingMakeActivity1.this, R.color.white));

                Toast.makeText(PloggingMakeActivity1.this, "승인제를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 모집 기간 입력 (시작일)
        inputStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        // 모집 기간 입력 (종료일)
        inputEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });


        // 다음 버튼
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnNext.setBackgroundTintList(ContextCompat.getColorStateList(PloggingMakeActivity1.this, R.color.gray));
                Log.d("PloggingMakeActivity1", "participantNum: " + maxPeople.getText().toString());
                Log.d("PloggingMakeActivity1", "selectedType: " + ploggingType);
                Log.d("PloggingMakeActivity1", "startDate: " + recruitStartDate);
                Log.d("PloggingMakeActivity1", "endDate: " + recruitEndDate);

                // 다음 Activity로 이동
                Intent intent = new Intent(PloggingMakeActivity1.this, PloggingMakeActivity2.class);
                // 다음 페이지에게 데이터 전달
                intent.putExtra("participantNum", maxPeople.getText().toString());   //모집 인원 수
                intent.putExtra("selectedType", ploggingType);  // 선착순 or 승인제
                intent.putExtra("startDate", recruitStartDate);    //시작일
                intent.putExtra("endDate", recruitEndDate);    //종료일
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            if (isStartDate) {
                recruitStartDate = date;
                inputStartDate.setText(date);
            } else {
                recruitEndDate = date;
                inputEndDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void resetTimeButtons() {
        btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        btnArrival.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray));
        btnApproval.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray));
    }
}
