package com.example.plotting_fe.home.ui;

import com.example.plotting_fe.home.dto.response.HomeResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;
import com.example.plotting_fe.plogging.dto.response.PlowerResponse;

import java.util.List;

public interface HomeResponseListener {
    void onHomeDataReceived(HomeResponse homeResponse);  // 홈 데이터 수신
    void onPloggingDataReceived(List<PloggingResponse> ploggingResponseList);  // 플로깅 데이터 수신
    void onPlowerDataReceived(List<PlowerResponse> plowerResponseList);  // 플로워 데이터 수신
    void onUserDataReceiver(String userNickname);
    void onError(String errorMessage);  // 오류 처리
}
