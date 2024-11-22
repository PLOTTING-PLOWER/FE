package com.example.plotting_fe.home.ui;

import android.content.Context;
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

    // Todo : dto 수정하기
    public CardnewsAdapter(Context context, List<CardnewsResponse> cardnewsList) {
        this.context = context;
        if (cardnewsList == null) {
            Log.e("CardnewsList", "cardnewsList is null");
        } else {
            this.cardnewsList = cardnewsList;
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
