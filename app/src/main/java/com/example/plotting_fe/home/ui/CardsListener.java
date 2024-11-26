package com.example.plotting_fe.home.ui;

import com.example.plotting_fe.home.dto.response.CardResponse;
import com.example.plotting_fe.home.dto.response.CardResponseList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CardsListener {
        void onCardsReceived(List<CardResponse> cardResponse);
    void onFailure();
}
