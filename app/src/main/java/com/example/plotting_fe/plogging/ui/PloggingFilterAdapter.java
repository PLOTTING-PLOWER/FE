package com.example.plotting_fe.plogging.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

import java.util.List;

// TODO: 플로깅 필터로 받아온 데이터를 ADapter와 연결하기 in GetPloggings
public class PloggingFilterAdapter extends RecyclerView.Adapter<PloggingFilterAdapter.PloggingViewHolder> {

    private Context context;
    private List<PloggingResponse> ploggingList;

    public PloggingFilterAdapter(Context context, List<PloggingResponse> ploggingList) {
        this.context = context;
        this.ploggingList = ploggingList;
    }

    @NonNull
    @Override
    public PloggingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.a_fragment_filterplogging, parent, false);
        return new PloggingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PloggingViewHolder holder, int position) {
        PloggingResponse plogging = ploggingList.get(position);

        // 플로깅 데이터를 뷰에 바인딩
        holder.regionTextView.setText(plogging.getRegion());
        holder.startDateTextView.setText(plogging.getStartDate().toString());
        holder.endDateTextView.setText(plogging.getEndDate().toString());
        holder.spendTimeTextView.setText(String.valueOf(plogging.getSpendTime()));
        holder.maxPeopleTextView.setText(String.valueOf(plogging.getMaxPeople()));
    }

    @Override
    public int getItemCount() {
        return ploggingList.size();
    }

    // ViewHolder 클래스를 정의하여 각 아이템의 뷰를 관리합니다.
    public static class PloggingViewHolder extends RecyclerView.ViewHolder {

        TextView regionTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView spendTimeTextView;
        TextView maxPeopleTextView;

        public PloggingViewHolder(View itemView) {
            super(itemView);
            regionTextView = itemView.findViewById(R.id.spinner_region);
            startDateTextView = itemView.findViewById(R.id.startDate);
            endDateTextView = itemView.findViewById(R.id.endDate);
            spendTimeTextView = itemView.findViewById(R.id.tvSpendTime);
            maxPeopleTextView = itemView.findViewById(R.id.tv_max_people);
        }
    }

    public void updateData(List<PloggingResponse> newPloggingList) {
        ploggingList.clear();
        ploggingList.addAll(newPloggingList);
        notifyDataSetChanged();
    }
}

