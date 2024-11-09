package com.example.plotting_fe.mypage.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.plotting_fe.R;

public class MypageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageFragment() {
        // Required empty public constructor
    }

    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypage, container, false);

        LinearLayout profile = rootView.findViewById(R.id.ll_menu).findViewById(R.id.ll_profile);
        // 버튼을 찾고 클릭 리스너를 설정합니다.
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 통해 action_mypage_to_details로 이동
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_mypage_to_profile);
            }
        });

        LinearLayout star = rootView.findViewById(R.id.ll_menu).findViewById(R.id.ll_star);
        // 버튼을 찾고 클릭 리스너를 설정합니다.
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 통해 action_mypage_to_star로 이동
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_mypage_to_star);
            }
        });
        

        // Inflate the layout for this fragment
        return rootView;
    }
}