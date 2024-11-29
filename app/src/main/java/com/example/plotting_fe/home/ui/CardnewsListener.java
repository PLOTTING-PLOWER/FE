package com.example.plotting_fe.home.ui;

import com.example.plotting_fe.home.dto.response.CardnewsResponseList;

public interface CardnewsListener {
    void onCardnewsReceived(CardnewsResponseList cardnewsResponseList);
}
