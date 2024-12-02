package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.plotting_fe.R;
import com.example.plotting_fe.global.util.ClickUtil;
import com.example.plotting_fe.plogging.dto.request.PloggingRequest;

import java.util.Calendar;

public class PloggingMakeActivity2 extends AppCompatActivity implements AddressSearchFragment.OnAddressSelectedListener {

    private EditText editName, editIntro, startDate, startTimeText,
            startLocation, endLocation, duringTime;
    private Button freeTime, btnFinish, duringTimeBtn;
    private String selectedStartDate;
    private boolean is_start_location;
    private ImageView btnBack;


    /*
    String으로 타입 바꿈
     */
    private String recruitStartDate;
    private String recruitEndDate;
    private String startDateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging2);

        btnBack = findViewById(R.id.iv_back);
        editName = findViewById(R.id.edit_name);
        editIntro = findViewById(R.id.edit_intro);
        startDate = findViewById(R.id.edit_start_date_activity);
        startTimeText = findViewById(R.id.edit_start_time_activity);
        duringTime = findViewById(R.id.input_during_time);
        duringTimeBtn = findViewById(R.id.button_during_time);
        freeTime = findViewById(R.id.edit_during_time_free);
        startLocation = findViewById(R.id.edit_start_location);
        endLocation = findViewById(R.id.edit_end_location);
        btnFinish = findViewById(R.id.btn_finish);

        //버튼 색 초기화
        resetTimeButtons();

        // 뒤로 가기 버튼
        ClickUtil.INSTANCE.onBackButtonClick(PloggingMakeActivity2.this, btnBack);

        // 날짜 선택을 위한 DatePickerDialog 설정
        startDate.setOnClickListener(v -> showDatePickerDialog());

        // 자유 시간을 선택했을 때의 처리
        freeTime.setOnClickListener(v -> {
            Toast.makeText(PloggingMakeActivity2.this, "자유를 선택했습니다.", Toast.LENGTH_SHORT).show();
        });

        // 직접 시간을 입력하는 버튼 클릭 처리
        duringTimeBtn.setOnClickListener(v -> {
            Toast.makeText(PloggingMakeActivity2.this, "직접 입력을 선택했습니다.", Toast.LENGTH_SHORT).show();
            duringTime.setVisibility(View.VISIBLE);
            duringTimeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
        });

        // 출발지 주소 입력 처리
        startLocation.setOnClickListener(v -> {
            AddressSearchFragment addressSearchFragment = new AddressSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addressSearchFragment)
                    .addToBackStack(null)
                    .commit();
            is_start_location = true;
        });

        // 도착지 주소 입력 처리
        endLocation.setOnClickListener(v -> {
            AddressSearchFragment addressSearchFragment = new AddressSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addressSearchFragment)
                    .addToBackStack(null)
                    .commit();
            is_start_location = false;
        });

        // 완료 버튼 클릭 시 데이터 제출 처리
        btnFinish.setOnClickListener(v -> {


            btnFinish.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));

            //PloggingMakeActivity1에서 가져온 데이터
            Intent intentFromFirstView = getIntent();
            String selectedType = intentFromFirstView.getStringExtra("selectedType");   // 1.모임 방식 (선착순/승인제)
            String participantNum = intentFromFirstView.getStringExtra("participantNum");   //2. 모집 인원수
            String startDateStr = intentFromFirstView.getStringExtra("startDate");  //3.모집 시작일
            String endDateStr = intentFromFirstView.getStringExtra("endDate");   //4. 모집 종료일

            //PloggingMakeActivity2의 데이터
            String title = editName.getText().toString();   //5. 활동명
            String content = editIntro.getText().toString();    //6. 활동 소개
            String spendTimeInput = duringTime.getText().toString();    //7. 활동 시작 시간
            String startTimeInput = startTimeText.getText().toString();     //8. 예상 소요시간
            String startLoc = startLocation.getText().toString();   //9. 출발장소
            String endLoc = endLocation.getText().toString();   //10. 도착장소

            // 데이터 유효성 검사
            if (title.isEmpty() || content.isEmpty() || spendTimeInput.isEmpty() ||
                    startTimeInput.isEmpty() || startLoc.isEmpty() || startDateStr.isEmpty() ||
                    participantNum.isEmpty()) {
                Toast.makeText(PloggingMakeActivity2.this, "필수 입력 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                //1번, 5번, 6번, 9번, 10번 String인지 확인하기 -> String 이다!
                //3번, 4번, 7번
                recruitStartDate = startDateStr;  //형식 2024-11-11
                recruitEndDate = endDateStr;    //형식 2024-11-11
                startDateTime = startDateStr + "T" + startTimeInput + ":00"; //형식 2024-11-11 10:00
                //2번
                long maxPeople = Long.parseLong(participantNum);
                //8번
                long spendTime = spendTimeInput.isEmpty() ? 0 : Long.parseLong(spendTimeInput);

                String endTime = "2024-11-30T19:55:07";

                // 유효한 데이터를 사용하여 PloggingRequest 객체 생성
                PloggingRequest request = new PloggingRequest(
                        title,
                        content,
                        maxPeople,
                        selectedType,
                        recruitStartDate,
                        recruitEndDate,
                        startDateTime,
                        spendTime,
                        endTime,
                        startLoc,
                        endLoc.isEmpty() ? null : endLoc
                );
                Log.d("debug", "PloggingRequest created: " + request);

                // API 호출하여 서버와 통신
                PloggingApiService ploggingApiService = new PloggingApiService();
                ploggingApiService.createPlogging(request, PloggingMakeActivity2.this);

                // TODO : GetPloggins Fragment 로 화면 전환하기
//                onBackButtonClick1(PloggingMakeActivity2.this);

                GetPloggings getPlogginsFragment = new GetPloggings();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, getPlogginsFragment); // fragment_container는 프래그먼트를 표시할 ViewGroup의 ID입니다.
                transaction.addToBackStack(null); // 백 스택에 추가하여 뒤로 가기 가능
                transaction.commit();

            } catch (Exception e) {
                Log.e("Error", "Invalid input data: " + e.getMessage());
                Toast.makeText(PloggingMakeActivity2.this, "입력 데이터를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 시작 날짜를 선택하기 위한 DatePickerDialog 표시
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
        } else {
            endLocation.setText(address);
        }
        getSupportFragmentManager().popBackStack();
    }

    private void resetTimeButtons() {
        freeTime.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray));
        duringTimeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_gray));
        btnFinish.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
    }
//
//    public static void onBackButtonClick1(final Activity activity) {
//        activity.finishAffinity();
//    }
}

