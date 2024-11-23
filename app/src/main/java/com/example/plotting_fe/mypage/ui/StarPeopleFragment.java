//package com.example.plotting_fe.mypage.ui;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.plotting_fe.R;
//import com.example.plotting_fe.mypage.dto.Person;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class StarPeopleFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private PeopleAdapter adapter;
//    private List<Person> peopleList = new ArrayList<>();
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;
//
//    public StarPeopleFragment() {
//        // Required empty public constructor
//    }
//
//    public static StarPeopleFragment newInstance(String param1, String param2) {
//        StarPeopleFragment fragment = new StarPeopleFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_star_people, container, false);
//
//        recyclerView = view.findViewById(R.id.titleRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // 샘플 데이터
//        preparePeopleData();
//
//        // NavController 초기화
//        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//
//        // 어댑터 초기화 시 클릭 리스너 추가
//        adapter = new PeopleAdapter(peopleList, new PeopleAdapter.OnPersonClickListener() {
//            @Override
//            public void onPersonClick(Person person) {
//                // ProfileDetailFragment로 이동
//                Bundle args = new Bundle();
//                args.putString("personName", person.getName());
//                args.putString("personDetails", person.getDetails());
//                navController.navigate(R.id.action_star_to_profile_detail, args);
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//        // Inflate the layout for this fragment
//        return view;
//    }
//
//    private void preparePeopleData() {
//        peopleList.add(new Person("Tiana Saris", "BCA • 2468 3545 ****"));
//        peopleList.add(new Person("Kaiya Baptista", "BCA • 2468 3545 ****"));
//        // 추가 데이터
//    }
//}