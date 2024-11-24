package com.example.plotting_fe.plogging.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plotting_fe.R;
import com.example.plotting_fe.mypage.ui.ProfileDetailFragment;
import com.example.plotting_fe.plogging.dto.Participant;

import java.util.List;

public class PloggingUserAdapter extends RecyclerView.Adapter<PloggingUserAdapter.ParticipantViewHolder> {

    private List<Participant> participantList;

    public PloggingUserAdapter(List<Participant> participantList) {
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participantList.get(position);
        holder.nameTextView.setText(participant.getNickname());
        holder.detailsTextView.setText(participant.getProfileMessage());

        // Glide를 사용하여 이미지 로드
        Glide.with(holder.profileImageView)
                .load(participant.getProfileImageUrl()) // participant.imageUrl에 실제 이미지 URL이 있어야 함
                .placeholder(R.drawable.ic_person) // 로딩 중에 표시할 이미지
                .error(R.drawable.ic_person) // 오류 발생 시 표시할 이미지
                .into(holder.profileImageView);

        holder.itemView.setOnClickListener(v -> {
            Participant item = participantList.get(position);
            Intent intent = new Intent(v.getContext(), ProfileDetailFragment.class);
            intent.putExtra("userId", item.getUserId()); // ploggingId 전달
            v.getContext().startActivity(intent);
            ((Activity)v.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView detailsTextView;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
        }
    }
}
