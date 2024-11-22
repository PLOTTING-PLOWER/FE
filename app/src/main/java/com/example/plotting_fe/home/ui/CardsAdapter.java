package com.example.plotting_fe.home.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plotting_fe.R;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {

    private List<String> cardUrls;

    public CardsAdapter(List<String> cardUrls) {
        this.cardUrls = cardUrls;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.a_cardnews, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String imageUrl = cardUrls.get(position);

        // 카드 뉴스 이미지 (url)  로드
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.custom_progress_bar) // 로딩 중 표시할 기본 이미지
                .error(R.drawable.ic_bar) // 로드 실패 시 표시할 기본 이미지
                .into(holder.imageView); // 이미지뷰에 설정
    }

    @Override
    public int getItemCount() {
        return cardUrls.size(); // URL 리스트 크기 반환
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_card);
        }
    }
}
