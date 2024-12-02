package com.example.plotting_fe.plogging.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.plogging.dto.response.PloggingGetStarResponse;
import com.example.plotting_fe.plogging.dto.response.PloggingResponse;

import java.util.ArrayList;
import java.util.List;

public class GetPloggings extends Fragment {

    private ImageView addBtn, searchBtn, filterBtn;
    private EditText searchInput;
    private List<PloggingGetStarResponse> ploggingList;
    private RecyclerView recyclerView;
    private GetPloggingAdapter getPloggingAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_filterplogging, container, false);

        addBtn = view.findViewById(R.id.btn_add_plogging);
        searchInput = view.findViewById(R.id.search_edit_text);
        searchBtn = view.findViewById(R.id.search_button);
        filterBtn = view.findViewById(R.id.filter);
        recyclerView = view.findViewById(R.id.plogging_list);

        ploggingList = new ArrayList<>();

        // RecyclerView에 어댑터 연결 위한 RecyclerView 초기화
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPloggingAdapter = new GetPloggingAdapter(ploggingList);
        recyclerView.setAdapter(getPloggingAdapter);

        // 버튼 모음
        buttons();

        return view;
    }

    private void buttons() {
        // 추가 버튼
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PloggingMakeActivity1.class);
            startActivity(intent);
        });

        filterBtn.setOnClickListener(v -> {
            Log.d("GetPloggings", "Filter button clicked");
            Intent intent = new Intent(getActivity(), PloggingFilter.class);
            startActivity(intent);
        });

        searchBtn.setOnClickListener(v -> {
            Log.d("GetPloggings", "Search button clicked");
            String searchTitle = searchInput.getText().toString().trim();
            if (searchTitle.isEmpty()) {
                Toast.makeText(requireActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                searchPlogging(searchTitle);
            }
        });
    }

    private void searchPlogging(String searchTitle) {
        PloggingApiService ploggingApiService = new PloggingApiService();
        ploggingApiService.getPloggingWithTitle(searchTitle, new PloggingResponseListener() {
            @Override
            public void onPloggingResponse(PloggingGetStarResponse ploggingResponse) {
                List<PloggingGetStarResponse> ploggingList = new ArrayList<>();
                ploggingList.add(ploggingResponse);

                if (getPloggingAdapter != null) {
                    getPloggingAdapter.updateDataList(ploggingList);
                } else {
                    Log.d("PloggingSearch", "어댑터가 초기화되지 않았습니다.");
                }
            }
        });
    }
}
