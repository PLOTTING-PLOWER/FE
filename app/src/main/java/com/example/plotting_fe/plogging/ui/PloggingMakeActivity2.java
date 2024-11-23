package com.example.plotting_fe.plogging.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plotting_fe.BuildConfig;
import com.example.plotting_fe.R;
import com.example.plotting_fe.global.NCPApiService;
import com.example.plotting_fe.global.dto.response.GeocodeResponse;
import com.example.plotting_fe.global.util.ApiClient;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PloggingMakeActivity2 extends AppCompatActivity implements AddressSearchFragment.OnAddressSelectedListener {

    private EditText editName, editIntro, startDate, startTimeText,
            startLocation, endLocation, duringTime;
    private Button freeTime, btnFinish, duringTimeBtn;
    private String selectedStartDate;
    private boolean is_start_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_makeplogging2);

        editName = findViewById(R.id.edit_name);
        editIntro = findViewById(R.id.edit_intro);
        startDate = findViewById(R.id.edit_start_date_activity);
        startTimeText= findViewById(R.id.edit_start_time_activity);
        duringTime = findViewById(R.id.input_during_time);
        duringTimeBtn = findViewById(R.id.button_during_time);    // 예상 소요 시간
        freeTime = findViewById(R.id.edit_during_time_free);
        startLocation = findViewById(R.id.edit_start_location);
        endLocation = findViewById(R.id.edit_end_location);
        btnFinish = findViewById(R.id.btn_finish);

        // 활동 시작 날짜 입력 (클릭하면 캘린더로 선택)
        startDate.setOnClickListener(v -> showDatePickerDialog());

        // 예상 소요 시간 - 자유 선택
        freeTime.setOnClickListener(v ->
                Toast.makeText(PloggingMakeActivity2.this, "자유를 선택했습니다.", Toast.LENGTH_SHORT).show()
        );

        // 예상 소요 시간 입력 { hh:mm 형태로 입력 }
        duringTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 직접 입력 선택시 입력란 보이도록 설정함
                Toast.makeText(PloggingMakeActivity2.this, "직접 입력을 선택했습니다.", Toast.LENGTH_SHORT).show();
                duringTime.setVisibility(View.VISIBLE);
            }
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
            String name = editName.getText().toString();
            String intro = editIntro.getText().toString();
            String duration = duringTime.getText().toString();
            String startTime = startTimeText.getText().toString();
            String startLoc = startLocation.getText().toString();
            String endLoc = endLocation.getText().toString();

            // 데이터 유효성 검사 (도착지는 null이어도 된다)
            if (name.isEmpty() || intro.isEmpty() || duration.isEmpty() ||
                    startTime.isEmpty() || startLoc.isEmpty()) {
                Toast.makeText(PloggingMakeActivity2.this, "필수 입력 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(PloggingMakeActivity2.this, GetPloggings.class);
                intent.putExtra("name", name);
                intent.putExtra("intro", intro);
                intent.putExtra("startDate", selectedStartDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("duration", duration);
                intent.putExtra("startLocation", startLoc);
                intent.putExtra("endLocation", endLoc);
                startActivity(intent);
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

    // 주소
    private void searchAddress(String query, EditText inputText) {
        NCPApiService apiService = ApiClient.INSTANCE.getNCPApiService();
        // 네이버 주소 검색 API 호출
        Call<GeocodeResponse> call = apiService.getGeocode(BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, query);

        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeocodeResponse geocodeResponse = response.body();
                    List<GeocodeResponse.Addresses> addresses = geocodeResponse.getAddresses();

                    if (!addresses.isEmpty()) {
                        // 네이버 서버에서 주소 가져오기
                        String roadAddress = addresses.get(0).getRoadAddress();
                        // 선택된 주소를 입력창에 표시함
                        inputText.setText(roadAddress);
                    } else {
                        Toast.makeText(PloggingMakeActivity2.this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                Log.e("Geocode", "Failure: " + t.getMessage());
                Toast.makeText(PloggingMakeActivity2.this, "네이버 서버 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddressSelected(String address) {
        if (is_start_location) {
            startLocation.setText(address);
            getSupportFragmentManager().popBackStack(); //프레그 먼트 닫기
        }
        else {
            endLocation.setText(address);
            getSupportFragmentManager().popBackStack(); //프레그 먼트 닫기
        }
    }


}
