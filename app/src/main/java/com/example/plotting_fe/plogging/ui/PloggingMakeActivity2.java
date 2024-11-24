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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class PloggingMakeActivity2 extends AppCompatActivity implements AddressSearchFragment.OnAddressSelectedListener {

    private EditText editName, editIntro, startDate, startTimeText,
            startLocation, endLocation, duringTime;
    private Button freeTime, btnFinish, duringTimeBtn;
    private String selectedStartDate;
    private boolean is_start_location;

    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private LocalDateTime startDateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging2);

        // UI 컴포넌트 초기화
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

        /*
         완료 버튼 클릭 시 데이터 제출 처리
         */
        btnFinish.setOnClickListener(v -> {
            String title = editName.getText().toString();
            String content = editIntro.getText().toString();
            String spendTimeInput = duringTime.getText().toString();
            String startTimeInput = startTimeText.getText().toString();
            String startLoc = startLocation.getText().toString();
            String endLoc = endLocation.getText().toString();

            Intent intentFromFirstView = getIntent();
            String participantNum = intentFromFirstView.getStringExtra("participantNum");
            String selectedTypeString = intentFromFirstView.getStringExtra("selectedType");
            String startDateStr = intentFromFirstView.getStringExtra("startDate");  //recruitStart
            String endDateStr= intentFromFirstView.getStringExtra("endDate");   //recruitEnd

            // 데이터 유효성 검사
            if (title.isEmpty() || content.isEmpty() || spendTimeInput.isEmpty() ||
                    startTimeInput.isEmpty() || startLoc.isEmpty() || startDateStr.isEmpty() ||
                    participantNum.isEmpty()) {
                Toast.makeText(PloggingMakeActivity2.this, "필수 입력 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                /*
                데이터 변환
                 */
                //Formatter 이용한 날짜 변환


                DateTimeFormatter dateFormatter = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE.ofPattern("yyyy-MM-dd");
                    recruitStartDate = LocalDate.parse(startDateStr, dateFormatter);
                    recruitEndDate = LocalDate.parse(endDateStr, dateFormatter);
                }
//                DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE.ofPattern("HH:mm");    //혹시 몰라서 두는 것

                //fixme
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // 시작 날짜와 시간을 합쳐서 LocalDateTime 객체 생성   fixme
                    String startDateTimeStr = startDateStr + " " + startTimeInput + ":00";

                    startDateTime = LocalDateTime.parse(startDateTimeStr, dateTimeFormatter);
                    Log.d("gogogo", startDateTime.toString());
                    }

                long maxPeople = Long.parseLong(participantNum);

                long spendTime = spendTimeInput.isEmpty() ? 0 : Long.parseLong(spendTimeInput);



                // PloggingRequest 객체 생성
                PloggingType selectedType = PloggingType.valueOf(selectedTypeString.toUpperCase());
                if (selectedType == null) {
                    Toast.makeText(this, "잘못된 유형 값입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                데이터 확인
                * */
                Log.d("debug", "recruitStartDate: " + recruitStartDate);
                Log.d("debug", "recruitEndDate: " + recruitEndDate);
                Log.d("debug", "startDateTime: " + startDateTime);

                /*
                 유효한 데이터를 사용하여 PloggingRequest 객체 생성
                 */
                PloggingRequest request = new PloggingRequest(
                        title,
                        content,
                        maxPeople,
                        selectedType,
                        recruitStartDate,
                        recruitEndDate,
                        startDateTime,
                        spendTime,
                        startLoc,
                        endLoc.isEmpty() ? null : endLoc
                );
                Log.d("debug", "PloggingRequest created: " + request);

                /*
                 다음 액티비티로 데이터 전송
                 */
                Log.d("gogogo", "intent 하기 전");
//                Intent intent = new Intent(PloggingMakeActivity2.this, GetPloggings.class);
//                startActivity(intent);

                // 필요시 API를 호출하여 서버와 통신
                PloggingApiService ploggingApiService = new PloggingApiService();
                ploggingApiService.createPlogging(request, 1L, PloggingMakeActivity2.this);
            } catch (Exception e) {
                // 파싱 오류 또는 기타 예외 처리
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
            getSupportFragmentManager().popBackStack(); // 프래그먼트 닫기
        } else {
            endLocation.setText(address);
            getSupportFragmentManager().popBackStack(); // 프래그먼트 닫기
        }
    }
}
