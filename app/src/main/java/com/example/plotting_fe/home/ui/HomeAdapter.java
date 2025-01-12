//package com.example.plotting_fe.home.ui;
//
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.plotting_fe.R;
//import com.example.plotting_fe.global.util.ClickUtil;
//import com.example.plotting_fe.myplogging.dto.PloggingData;
//import com.example.plotting_fe.plogging.dto.response.PloggingEntity;
//import com.example.plotting_fe.plogging.dto.response.PloggingGetStarResponse;
//import com.example.plotting_fe.plogging.ui.PloggingDetailActivity;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//
//// FIXME : 파일 안지운 이유: 홈에서 다시 들어올때 초기화 된 상태로 다시
////  반영되어야하는데 안돼서 HomeAdapter.java는 주석처리하고 HomeAdapter.kt 사용함.
//
//public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
//
//    private List<PloggingGetStarResponse> dataList;
//
//    public HomeAdapter(List<PloggingGetStarResponse> dataList) {
//        this.dataList = dataList;
//    }
//
//    // 데이터 업데이트 메서드
//    public void updateDataList(List<PloggingGetStarResponse> newDataList) {
//        this.dataList = newDataList;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plogging_info, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        PloggingGetStarResponse data = dataList.get(position);
//
//        //1. 승인제인가 선착순인가
//        String formattedPloggingType = formatPloggingType(data.getPloggingType());
//        holder.ploggingType.setText(formattedPloggingType);
//        //2. 플로깅 제목
//        holder.title.setText(data.getTitle());
//        //3. 플로깅 진행 여부
//        String formattedStatus = formatStatus(data.getStartTime(), data.getRecruitEndDate()) ? "진행중" : "종료됨";
//        holder.statusText.setText(formattedStatus);
//        holder.statusColor.setColorFilter(
//                "진행중".equals(formattedStatus) ?
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.green) :
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.red)
//        );
//        //5. 플로깅 시작 장소
//        holder.startLocation.setText(data.getStartLocation());
//        //6. 플로깅 시작 시각(2024-10-10T10:00:00) 형식임
//        String formattedStartTime = formatDate(data.getStartTime());
//        holder.startTime.setText(formattedStartTime);
//        //7. 플로깅 활동 시간
//        String formattedSpendTime = formatSpendTime(data.getSpendTime());
//        holder.spendTime.setText(formattedSpendTime);
//        //8. 현재 참여 인원
//        holder.currentPeople.setText(String.valueOf(data.getCurrentPeople()));
//        //9. "/" 표시
//        holder.and.setText("/");
//        //10. 최대 참가 인원
//        holder.maxPeople.setText(String.valueOf(data.getMaxPeople()));
//
//
//        //11. 참여하기 버튼
//        holder.btnJoin.setOnClickListener(v -> {
//            //클릭 시 ploggingId를 통해 PloggingDetailActivity로 이동
//            Intent intent = new Intent(v.getContext(), PloggingDetailActivity.class);
//            intent.putExtra("ploggingId", data.getPloggingId());  // ploggingId 전달
//            v.getContext().startActivity(intent);
//        });
//
////        // 초기 상태 설정
////        if (data.isStar()) {
////            holder.btnStar.setImageDrawable(
////                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_star_color)
////            );
////        } else {
////            holder.btnStar.setImageDrawable(
////                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_star_gray)
////            );
////        }
//
//        // 4. 즐겨찾기 버튼
//        holder.btnStar.setOnClickListener(v -> {
//            ImageView starIcon = (ImageView) v;
//
//            Drawable colorStar = ContextCompat.getDrawable(v.getContext(), R.drawable.ic_star_color);
//            Drawable grayStar = ContextCompat.getDrawable(v.getContext(), R.drawable.ic_star_gray);
//
//            ClickUtil.INSTANCE.togglePloggingStarWithHome(data.getPloggingId(), new ClickUtil.ToggleCallback() {
//                @Override
//                public void onComplete(boolean isStarred) {
//                    if (isStarred) {
//                        holder.btnStar.setImageDrawable(colorStar);
////                       data.setStar(true);
//                    } else {
//                        holder.btnStar.setImageDrawable(grayStar);
////                        data.setStar(false);
//                    }
//                    notifyDataSetChanged();
//                }
//            });
//        });
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        // 데이터 리스트 크기 반환
//        return dataList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView ploggingType, title, startLocation, startTime, spendTime, maxPeople, currentPeople, and, statusText;
//        TextView btnJoin;
//        ImageView statusColor, btnStar;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            // 아이템 뷰에서 TextView와 Button 초기화
//            title = itemView.findViewById(R.id.tvTitle);
//            ploggingType = itemView.findViewById(R.id.tvploggingType);
//            startLocation = itemView.findViewById(R.id.tvStartLocation);
//            startTime = itemView.findViewById(R.id.tvStartTime);
//            spendTime = itemView.findViewById(R.id.tvSpendTime);
//            and = itemView.findViewById(R.id.and);  // 귀찮아서 "/"표시 따로 넣어버림
//            maxPeople = itemView.findViewById(R.id.tvMaxPeople);
//            currentPeople = itemView.findViewById(R.id.tvCurrentPeople);
//            btnJoin = itemView.findViewById(R.id.btnJoin);  // xml Button-> TExtview로 변경되어 있어서 그거 대로 수정함.
//
//            statusColor = itemView.findViewById(R.id.tvStatus);
//            statusText = itemView.findViewById(R.id.tvStatusText);
//            btnStar = itemView.findViewById(R.id.iv_gray_star); //본래 starIcon으로 설정해둔거 merge 이후 xml 이름대로 변경
//        }
//
//    }
//
//    private String formatDate(String dateStr) {
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
//        SimpleDateFormat outputFormat = new SimpleDateFormat("MM월 dd일 a hh:mm", Locale.KOREA);
//
//        try {
//            Date date = inputFormat.parse(dateStr);
//            if (date != null) {
//                return outputFormat.format(date); // 오전 오후 표시하는것
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dateStr;
//    }
//
//    private String formatSpendTime(Long spendTime) {
//        int hours = (int) (spendTime / 60);
//        int minutes = (int) (spendTime % 60);
//
//        return String.format(Locale.getDefault(), "%d시간 %d분", hours, minutes);
//    }
//
//    private String formatPloggingType(String type) {
//        if (type == "DIRECT") {
//            return "선착순";
//        } else {
//            return "승인제";
//        }
//    }
//
//    private boolean formatStatus(String startDate, String recruitEndDate) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//            Date start = dateFormat.parse(startDate);
//            Date end = dateFormat.parse(recruitEndDate);
//
//            if (start != null && end != null) {
//                return start.before(end);  // startDate가 recruitEndDate보다 이전이면 true, 아니면 false
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//}