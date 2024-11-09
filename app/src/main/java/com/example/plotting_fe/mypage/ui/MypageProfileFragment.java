package com.example.plotting_fe.mypage.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.plotting_fe.R;
import com.example.plotting_fe.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MpageProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageProfileFragment newInstance(String param1, String param2) {
        MypageProfileFragment fragment = new MypageProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_mypage_profile, container, false);

        // 뒤로 가기 버튼 설정
        ImageView backButton = view.findViewById(R.id.iv_back);
        Utils.onBackButtonClick(getActivity(), backButton);

        // 수정 버튼 클릭 리스너 설정
        ImageView editButton = view.findViewById(R.id.iv_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileActivity", "Edit button clicked"); // 로그 추가
                // SampleActivity로 전환하는 Intent 생성
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent); // 액티비티 시작
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}