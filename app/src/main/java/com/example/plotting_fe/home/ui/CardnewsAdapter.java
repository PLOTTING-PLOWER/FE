package com.example.plotting_fe.home.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.CardnewsResponse;

import java.util.ArrayList;
import java.util.List;

public class CardnewsAdapter extends RecyclerView.Adapter<CardnewsAdapter.ViewHolder> {

    private Context context;
    private List<CardnewsResponse> cardnewsList;

    public CardnewsAdapter(Context context, List<CardnewsResponse> cardnewsList) {
        this.context = context;
        this.cardnewsList = cardnewsList != null ? cardnewsList : new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardnews_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cardnewsList != null && !cardnewsList.isEmpty()) {
            CardnewsResponse cardnews = cardnewsList.get(position);
            holder.titleTextView.setText(cardnews.getCardnewsTitle());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CardsActivity.class);
                intent.putExtra("cardnewsId", String.valueOf(cardnews.getCardnewsId()));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cardnewsList != null ? cardnewsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
        }
    }

    public void updateData(List<CardnewsResponse> newCardnewsList) {
        if (newCardnewsList != null) {
            this.cardnewsList = newCardnewsList;
            notifyDataSetChanged();
        }
    }
}
