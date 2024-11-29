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

    // 생성자
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

        // 데이터 바인딩
        holder.titleTextView.setText(plogging.getTitle());
        holder.startLocation.setText(plogging.getStartLocation());
        holder.ploggingType.setText(plogging.getPloggingType().toString());
        holder.startDateAndTime.setText(plogging.getStartTime().toString());
        holder.duringTime.setText(String.valueOf(plogging.getSpendTime()));
        holder.maxPeople.setText(String.valueOf(plogging.getMaxPeople()));
//        holder.currentPeople.setText(String.valueOf(plogging.getCurrentPeople()));

//        // 참여하기 버튼의 상태 설정 (현재 인원이 최대 인원에 도달하면 버튼 비활성화)
//        if (plogging.getCurrentPeople() >= plogging.getMaxPeople()) {
//            holder.joinBtn.setEnabled(false);
//            holder.joinBtn.setText("마감된 플로깅");
//        } else {
//            holder.joinBtn.setEnabled(true);
//            holder.joinBtn.setText("참여하기");
//        }

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

    // 데이터 갱신 메서드 추가
    public void updatePloggingList(List<PloggingResponse> newPloggingList) {
        this.ploggingList = newPloggingList;
        notifyDataSetChanged(); // RecyclerView 갱신
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView startLocation;
        public TextView ploggingType;
        public TextView startDateAndTime;
        public TextView duringTime;
        public TextView maxPeople;
        public TextView currentPeople;
        public Button joinBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            startLocation = itemView.findViewById(R.id.tvStartLocation);
            ploggingType = itemView.findViewById(R.id.tvploggingType);
            startDateAndTime = itemView.findViewById(R.id.tvStartTime);
            duringTime = itemView.findViewById(R.id.tvSpendTime);
            maxPeople = itemView.findViewById(R.id.tvMaxPeople);
            currentPeople = itemView.findViewById(R.id.tvCurrentPeople);
            joinBtn = itemView.findViewById(R.id.btnJoin);
        }
    }
}
