package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.PloggingType;

public class PloggingMakeActivity1 extends AppCompatActivity {

    private EditText maxPeople, inputStartDate, inputEndDate;
    private String ploggingType;
    private ImageView btnBack, btnArrival, btnApproval;
    private String recruitStartDate, recruitEndDate;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging1);

        maxPeople = findViewById(R.id.input_participant_num);
        btnArrival = findViewById(R.id.btn_arrival);
        btnApproval = findViewById(R.id.btn_approval);
        inputStartDate = findViewById(R.id.start_date);
        inputEndDate = findViewById(R.id.end_date);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.ic_back);


        // 선착순(btn_arrival) 버튼 선택
        btnArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ploggingType = "DIRECT";
                Toast.makeText(PloggingMakeActivity1.this, "선착순을 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 승인제(btn_approval) 버튼 선택
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ploggingType = "APPROVE";
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
}
