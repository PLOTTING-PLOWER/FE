package com.example.plotting_fe.home.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plotting_fe.R;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ImageViewHolder> {

    private final Context context;
    private List<String> imageUrls;

    // Constructor
    public CardsAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardnews_img, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            Log.d("CardsAdapter", "Loading image: " + imageUrls);  // URL 확인
            String imageUrl = imageUrls.get(position);
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    // updateData 메소드 구현
    public void updateData(List<String> newImageUrls) {
        if (newImageUrls != null) {
            this.imageUrls = newImageUrls;
            notifyDataSetChanged();  // 데이터가 변경되었음을 RecyclerView에 알림
        }
    }

    // ViewHolder to hold imageView
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_card);  // Ensure this ID exists in your layout
        }
    }
}
