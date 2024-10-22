package com.example.plotting_fe.plogging.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.Participant;

import java.util.ArrayList;
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
        holder.nameTextView.setText(participant.getName());
        holder.detailsTextView.setText(participant.getDetails());
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView detailsTextView;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
        }
    }
}
