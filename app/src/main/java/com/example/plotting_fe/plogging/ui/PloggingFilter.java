package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.plotting_fe.R;

import java.util.Calendar;

public class PloggingFilter extends AppCompatActivity {

    private Spinner spinnerRegion;
    private EditText startDate, endDate;
    private RadioGroup meetingTypeGroup, participantCountGroup, timeGroup, startTimeGroup;
    private Button btnSubmit, btnTimeAll, btnTimeFree, startTimeAll, startTimeFree,
            participantsAll, participants5, participants10, participants15,
            meetingAll, meetingArrival, meetingApproval;

    private EditText timeForFree, startTimeAfterInput;
    private TextView btnReset;
    private String selectedStartDate, selectedEndDate;

    //버튼 색상 위한 변수
    private Button lastSelectedMeetingTypeButton;
    private Button lastSelectedParticipantCountButton;
    private Button lastSelectedTimeButton;

    private boolean freeIsCheckedOfTime = false;
    private boolean freeIsCheckedOfStartTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_filter);

        btnReset = findViewById(R.id.btn_reset);    //0. 초기화 버튼
        btnSubmit = findViewById(R.id.btn_submit);  //7. 완료 버튼

        spinnerRegion = findViewById(R.id.spinner_region);  //1.지역
        startDate = findViewById(R.id.startDate);   //2.날짜 -> 시작일
        endDate = findViewById(R.id.endDate);   //2.날짜 -> 종료일

        meetingTypeGroup = findViewById(R.id.meetingTypeGroup); //3.모임방식
        meetingAll = findViewById(R.id.btn_meeting_all);
        meetingArrival = findViewById(R.id.btn_meetin_arrival);
        meetingApproval = findViewById(R.id.btn_meetin_approval);

        participantCountGroup = findViewById(R.id.participantCountGroup);   //6. 인원수
        participantsAll = findViewById(R.id.participant_all);
        participants5 = findViewById(R.id.participant_5);
        participants10 = findViewById(R.id.participant_10);
        participants15 = findViewById(R.id.participant_15);

        timeGroup = findViewById(R.id.timeGroup);
        btnTimeAll = findViewById(R.id.btn_time_all);   //4. 시간 -> (1)전체
        btnTimeFree = findViewById(R.id.btn_time_free); //4. 시간 -> (2)자유
        timeForFree = findViewById(R.id.time_for_btn_time_free);    //4. 시간 -> (2) 자유 -> mm분 입력

        startTimeGroup = findViewById(R.id.startTimeGroup);
        startTimeAll = findViewById(R.id.start_time_all);   //5. 시작 시간 -> (1)전체
        startTimeFree = findViewById(R.id.start_time_free);  //5. 시작시간 -> (2) 자유
        startTimeAfterInput = findViewById(R.id.start_time_after_input);  //5. 시작시간 -> (2) 자유 -> hh시 입력

        //버튼 색 기본
        resetBtn();

        //버튼 색 클릭 리스너
        meetingAll.setOnClickListener(v -> changeButtonColor(meetingAll));
        meetingArrival.setOnClickListener(v -> changeButtonColor(meetingArrival));
        meetingApproval.setOnClickListener(v -> changeButtonColor(meetingApproval));
        participantsAll.setOnClickListener(v -> changeButtonColor(participantsAll));
        participants5.setOnClickListener(v -> changeButtonColor(participants5));
        participants10.setOnClickListener(v -> changeButtonColor(participants10));
        participants15.setOnClickListener(v -> changeButtonColor(participants15));
        btnTimeAll.setOnClickListener(v -> changeButtonColor(btnTimeAll));
        btnTimeFree.setOnClickListener(v -> changeButtonColor(btnTimeFree));
        startTimeAll.setOnClickListener(v -> changeButtonColor(startTimeAll));
        startTimeFree.setOnClickListener(v -> changeButtonColor(startTimeFree));

        // 0. 초기화 버튼
        btnReset.setOnClickListener(v -> resetFilters());

        //2. 달력
        startDate.setOnClickListener(v -> showDatePickerDialogOfStart());
        endDate.setOnClickListener(v -> showDatePickerDialogOfEnd());

//        //4. 시간 -> (2) 자유 선택시 입력 안내 Toast 발생
//        btnTimeFree.setOnClickListener(v -> {
//                    Toast.makeText(this, "상세 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
//                    freeIsCheckedOfTime = true;
//                    btnTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
//                }
//        );
//        //5. 시작 시간 -> (2) 자유 선택시 입력 안내 Toast 발생
//        startTimeFree.setOnClickListener(v -> {
//                    Toast.makeText(this, "상세 시간을 입력해주세요", Toast.LENGTH_SHORT).show();
//                    freeIsCheckedOfStartTime = true;
//                    startTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
//                }
//        );

        // 6. 완료 버튼
        btnSubmit.setOnClickListener(v -> submitFilters());
    }

    // 필터 초기화 함수
    private void resetFilters() {
        resetBtn();

        //Todo: 데이터 초기화
        spinnerRegion.setSelection(0); // 지역 초기화
        startDate.setText("");
        endDate.setText("");
        meetingTypeGroup.clearCheck();
        participantCountGroup.clearCheck();
//        btnTimeAll.setChecked(true); // 시간 필터 초기화
//        startTimeAll.setChecked(true); // 시작 시간 필터 초기화
    }

    //1. 지역 선택 -> spanning 이라서 자동으로 된다
    //2. 날짜 선택 -> 날짜 입력 달력 나와서 값 입력 된다

    //3. 모임 방식 선택
    private String getSelectedMeetingType() {
        int selectedId = meetingTypeGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.btn_meeting_all) {
            return "전체";
        } else if (selectedId == R.id.btn_meetin_arrival) {
            return "선착순";
        } else if (selectedId == R.id.btn_meetin_approval) {
            return "승인제";
        }
        return "전체"; // 기본값
    }

    //4. 시간
    private String getTimeType() {
        int selectedId = timeGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.time_for_btn_time_free) {
            return "자유";
        } else {
            return "전체";
        }
    }


    //5. 시작 시간
    private String getStartTimeType() {
        int selectedId = startTimeGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.start_time_free) {
            return "자유";
        } else {
            return "전체";
        }
    }


    // 6. 인원수 선택
    private String getSelectedParticipants() {
        int selectedId = participantCountGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.participant_all) {
            return "전체";
        } else if (selectedId == R.id.participant_5) {
            return "5";
        } else if (selectedId == R.id.participant_10) {
            return "10";
        } else if (selectedId == R.id.participant_15) {
            return "15";
        }
        return "전체"; // 기본값
    }

    //최종 완료 버튼

    // 필터 선택 값을 최종적으로 유효성 검사 거친 후에 -> PloggingResponse 담아서 서버로 보낸다.
    // ploggingController 의 findListByFilter 메서드
    private void submitFilters() {

        // if 데이터 유효성 검사 성공하면 -> 완료 버튼 색 main으로 변경

        // 1. 지역
        String region = spinnerRegion.getSelectedItem().toString();

        // 2. 날짜
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();

        // 3. 모임 방식
        String meetingType = getSelectedMeetingType();

        // 4. 시간
        //만약 TimeFree 선택시 time_for_btn_time_free 는 무조건 입력 필수
        String time = timeForFree.getText().toString();     //타입 확인하기


        // 5. 시작 시간
        //만약 StartTimefree 선택시 time_for_btn_time_free 는 무조건 입력 필수
        String startTime = startTimeAfterInput.getText().toString();    //타입 확인하기


        // 6. 인원수 선택
        String participants = getSelectedParticipants();

        // 값 response 담기
        //서버 보내기
    }

    // 달력 다이얼로그 -> 시작 시간
    private void showDatePickerDialogOfStart() {
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

    // 달력 다이얼로그 -> 종료 시간
    private void showDatePickerDialogOfEnd() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedEndDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            endDate.setText(selectedEndDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void resetBtn(){
        meetingAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        meetingArrival.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        meetingApproval.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participantsAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants10.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants15.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        btnTimeAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        btnTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        startTimeAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        startTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
    }

    // 버튼 색상 변경
    private void changeButtonColor(Button button) {
        setLastSelectedButton(button);

        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
    }

    // 모임 방식 버튼 색상 초기화
    private void resetMeetingTypeButtons() {
        meetingAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        meetingArrival.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        meetingApproval.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        // 마지막 선택된 버튼만 색상 유지
        if (lastSelectedMeetingTypeButton != null) {
            lastSelectedMeetingTypeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
        }
    }

    // 인원수 버튼 초기화
    private void resetParticipantCountButtons() {
        participantsAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants10.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        participants15.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        if (lastSelectedParticipantCountButton != null) {
            lastSelectedParticipantCountButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
        }
    }

    // 시간 버튼 초기화
    private void resetTimeButtons() {
        btnTimeAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        btnTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        if (lastSelectedTimeButton != null) {
            lastSelectedTimeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
        }
    }

    //시작 시간 버튼 초기화
    private void resetStartTimeButtons() {
        startTimeAll.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        startTimeFree.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        if (lastSelectedTimeButton != null) {
            lastSelectedTimeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.main));
        }
    }

    // 최종 버튼 선택
    private void setLastSelectedButton(Button button) {
        if (button == meetingAll || button == meetingArrival || button == meetingApproval) {
            lastSelectedMeetingTypeButton = button;
            resetMeetingTypeButtons();  // 모임 방식 버튼 초기화
        } else if (button == participantsAll || button == participants5 || button == participants10 || button == participants15) {
            lastSelectedParticipantCountButton = button;
            resetParticipantCountButtons();  // 인원수 버튼 초기화
        } else if (button == btnTimeAll || button == btnTimeFree) {
            lastSelectedTimeButton = button;
            resetTimeButtons();  // 시간 버튼 초기화
        } else if ( button == startTimeAll || button == startTimeFree) {
            lastSelectedTimeButton = button;
            resetStartTimeButtons();  // 시간 버튼 초기화
        }
    }
}
