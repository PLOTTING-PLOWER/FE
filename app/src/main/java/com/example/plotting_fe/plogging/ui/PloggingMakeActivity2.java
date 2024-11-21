package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.PloggingType;
import com.example.plotting_fe.plogging.dto.request.PloggingRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class PloggingMakeActivity2 extends AppCompatActivity implements AddressSearchFragment.OnAddressSelectedListener {

    private EditText editName, editIntro, startDate, startTimeText,
            startLocation, endLocation, duringTime;
    private Button freeTime, btnFinish, duringTimeBtn;
    private String selectedStartDate;
    private boolean is_start_location;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging2);

        editName = findViewById(R.id.edit_name);
        editIntro = findViewById(R.id.edit_intro);
        startDate = findViewById(R.id.edit_start_date_activity);
        startTimeText = findViewById(R.id.edit_start_time_activity);
        duringTime = findViewById(R.id.input_during_time);
        duringTimeBtn = findViewById(R.id.button_during_time);    // 예상 소요 시간
        freeTime = findViewById(R.id.edit_during_time_free);
        startLocation = findViewById(R.id.edit_start_location);
        endLocation = findViewById(R.id.edit_end_location);
        btnFinish = findViewById(R.id.btn_finish);

        // 활동 시작 날짜 입력 (클릭하면 캘린더로 선택)
        startDate.setOnClickListener(v -> showDatePickerDialog());

        // 예상 소요 시간 - 자유 선택
        freeTime.setOnClickListener(v -> {
            Toast.makeText(PloggingMakeActivity2.this, "자유를 선택했습니다.", Toast.LENGTH_SHORT).show();
        });

        // 예상 소요 시간 입력 { hh:mm 형태로 입력 }
        duringTimeBtn.setOnClickListener(v -> {
            // 직접 입력 선택시 입력란 보이도록 설정함
            Toast.makeText(PloggingMakeActivity2.this, "직접 입력을 선택했습니다.", Toast.LENGTH_SHORT).show();
            duringTime.setVisibility(View.VISIBLE);
        });

        // 출발 장소 입력 {네이버 제공 예시 테스트: 분당구 불정로 6}
        startLocation.setOnClickListener(v -> {
            AddressSearchFragment addressSearchFragment = new AddressSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addressSearchFragment)
                    .addToBackStack(null)
                    .commit();
            is_start_location = true;
        });

        // 도착 장소 입력 {네이버 제공 예시 테스트: 분당구 불정로 6}
        endLocation.setOnClickListener(v -> {
            AddressSearchFragment addressSearchFragment = new AddressSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addressSearchFragment)
                    .addToBackStack(null)
                    .commit();
            is_start_location = false;
        });

        // 모임 만들기 버튼 클릭 시
        btnFinish.setOnClickListener(v -> {
            String title = editName.getText().toString();
            String content = editIntro.getText().toString();
            String spendTimeInput = duringTime.getText().toString();
            String startTimeInput = startTimeText.getText().toString();
            String startLoc = startLocation.getText().toString();
            String endLoc = endLocation.getText().toString();

            Intent intentFromFirstView = getIntent();
            String participantNum = intentFromFirstView.getStringExtra("participantNum"); // 모집 인원 수
            String selectedTypeString= intentFromFirstView.getStringExtra("selectedType");
            String startDate = intentFromFirstView.getStringExtra("startDate");          // 모집 시작일
            String endDate = intentFromFirstView.getStringExtra("endDate");

            // 데이터 유효성 검사
            if (title.isEmpty() || content.isEmpty() || spendTimeInput.isEmpty() ||
                    startTimeInput.isEmpty() || startLoc.isEmpty() || startDate.isEmpty() ||
                    participantNum.isEmpty() ) {

                Toast.makeText(PloggingMakeActivity2.this, "필수 입력 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                long maxPeople = Long.parseLong(participantNum);
                LocalDate recruitStartDate = LocalDate.parse(startDate);
                LocalDate recruitEndDate = LocalDate.parse(endDate);
                long spendTime = Long.parseLong(spendTimeInput);
                LocalTime startTime = LocalTime.parse(startTimeInput);

                // request를 위한 타입 변환 과정
                PloggingType selectedType = PloggingType.valueOf(selectedTypeString.toUpperCase());
                if (selectedType == null) {
                    Toast.makeText(this, "잘못된 유형 값입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 데이터 넘어가는것 확인 위한 임의 로그
                Log.d("gogogo", "Title: " + title);
                Log.d("gogogo", "Content: " + content);
                Log.d("gogogo", "Start Time: " + startTimeInput);
                Log.d("gogogo", "Spend Time: " + spendTimeInput);
                Log.d("gogogo", "Start Location: " + startLoc);
                Log.d("gogogo", "End Location: " + endLoc);
                Log.d("gogogo", "Participant Num: " + participantNum);
                Log.d("gogogo", "Selected Type: " + selectedTypeString);
                Log.d("gogogo", "Start Date: " + startDate);
                Log.d("gogogo", "End Date: " + endDate);


                endLoc = (endLoc == null || endLoc.isEmpty()) ? null : endLoc;
                spendTime = (spendTimeInput == null || spendTimeInput.isEmpty()) ? null : spendTime;

                PloggingRequest request = new PloggingRequest(
                        title,
                        content,
                        maxPeople,
                        selectedType,
                        recruitStartDate,
                        recruitEndDate,
                        startTime,  //localTime으로 바꿔봄   // 계속 오류남 왜저래
                        spendTime,
                        startLoc,
                        endLoc
                );

                Log.d("gogogo", "intent 하기 전");
                Intent intent = new Intent(PloggingMakeActivity2.this, GetPloggings.class);

                Log.d("gogogo", "intent 한 후");
                startActivity(intent);

                // 서버 retrofit 연결되는 부분
                PloggingApiService ploggingApiService = new PloggingApiService();
                Log.d("gogogo", "createPlogging 호출 전");

                ploggingApiService.createPlogging(request, 1L, PloggingMakeActivity2.this);
                Log.d("gogogo", "createPlogging 호출 후");
            }
        });
    }


    // 달력 다이얼로그
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedStartDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            startDate.setText(selectedStartDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onAddressSelected(String address) {
        if (is_start_location) {
            startLocation.setText(address);
            getSupportFragmentManager().popBackStack(); //프레그 먼트 닫기
        } else {
            endLocation.setText(address);
            getSupportFragmentManager().popBackStack(); //프레그 먼트 닫기
        }
    }
}
