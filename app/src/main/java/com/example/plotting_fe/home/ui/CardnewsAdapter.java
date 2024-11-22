package com.example.plotting_fe.home.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.CardnewsResponse;

import java.util.List;

public class CardnewsAdapter extends RecyclerView.Adapter<CardnewsAdapter.ViewHolder> {

    private Context context;
    private List<CardnewsResponse> cardnewsList;

    public CardnewsAdapter(Context context, List<CardnewsResponse> cardnewsList) {
        this.context = context;
        this.cardnewsList = cardnewsList;

        for (int i = 0; i < cardnewsList.size(); i++) {
            Log.d("CardnewsAdapter", "Index: " + i + ", Title: " + cardnewsList.get(i).getTitle());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardnews_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardnewsResponse cardnews = cardnewsList.get(position);
        holder.titleTextView.setText(cardnews.getTitle());

        holder.itemView.setOnClickListener(v -> {
            // cardId를 CardnewsActivity로 넘기기
            Intent intent = new Intent(context, CardsActivity.class);
            // cardId를 전달함
            intent.putExtra("cardId", cardnews.getId());
//            intent.putExtra("cardId", "1");   //잘 넘어가는지 확인용 - cardId 하드코딩
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cardnewsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
        }
    }
}
