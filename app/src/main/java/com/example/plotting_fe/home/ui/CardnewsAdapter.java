package com.example.plotting_fe.home.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.home.dto.response.CardnewsResponseList;

import java.util.ArrayList;
import java.util.List;

public class CardnewsAdapter extends RecyclerView.Adapter<CardnewsAdapter.ViewHolder> {

    private Context context;
    private List<CardnewsResponseList.CardnewsResponse> cardnewsList;

    public CardnewsAdapter(Context context, List<CardnewsResponseList.CardnewsResponse> cardnewsList) {
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
        CardnewsResponseList.CardnewsResponse cardnews = cardnewsList.get(position);
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
