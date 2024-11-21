package com.example.plotting_fe.plogging.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PloggingAdapter extends RecyclerView.Adapter<PloggingAdapter.ViewHolder> {
    private List<PloggingResponse> ploggingList;
    private Context context;

    public PloggingAdapter(Context context, List<PloggingResponse> ploggingList) {
        this.context = context;
        this.ploggingList = ploggingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plogging_info, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PloggingResponse plogging = ploggingList.get(position);
        holder.titleTextView.setText(plogging.getTitle());
        holder.startLocation.setText(plogging.getStartLocation());
        holder.ploggingType.setText(plogging.getPloggingType().toString()); // PloggingType의 toString() 메서드에 따라 적절히 표시
        holder.startDateAndTime.setText(plogging.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); // 시작 시간 포맷
        holder.duringTime.setText(String.valueOf(plogging.getSpendTime())); // 소요 시간
        holder.maxPeople.setText(String.valueOf(plogging.getMaxPeople())); // 최대 인원
        holder.currentPeople.setText(String.valueOf(plogging.getCurrentPeople())); // 현재 인원

        // 참여하기 버튼 클릭 리스너 설정
        holder.joinBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, PloggingDetailActivity.class);
            intent.putExtra("ploggingId", plogging.getPloggingId()); // 플로깅 ID 전달
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ploggingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView startLocation; // 시작 위치
        public TextView ploggingType; // 플로깅 유형
        public TextView startDateAndTime; // 시작 날짜 및 시간
        public TextView duringTime; // 소요 시간
        public TextView maxPeople; // 최대 인원
        public TextView currentPeople; // 현재 인원
        public Button joinBtn; // 참여하기 버튼

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            startLocation = itemView.findViewById(R.id.tvStartLocation);
            ploggingType = itemView.findViewById(R.id.tvFirstCome);
            startDateAndTime = itemView.findViewById(R.id.tvStartTime);
            duringTime = itemView.findViewById(R.id.tvSpendTime);
            maxPeople = itemView.findViewById(R.id.tvMaxPeople);
            currentPeople = itemView.findViewById(R.id.tvCurrentPeople);
            joinBtn = itemView.findViewById(R.id.btnJoin);
        }
    }
}
