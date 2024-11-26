package com.example.plotting_fe.home.ui;

import com.example.plotting_fe.home.dto.response.CardResponse;
import com.example.plotting_fe.home.dto.response.CardResponseList;

import java.util.List;

public interface CardsListener {
    void onCardsReceived(List<CardResponse> cardResponseList);
    void onFailure();
}
