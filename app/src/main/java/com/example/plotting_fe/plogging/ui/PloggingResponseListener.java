package com.example.plotting_fe.plogging.ui;

import com.example.plotting_fe.plogging.dto.response.PloggingGetStarResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

public interface PloggingResponseListener {
    void onPloggingResponse(PloggingGetStarResponse ploggingGetStarResponse);
}
