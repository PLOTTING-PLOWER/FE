package com.example.plotting_fe.home.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.home.dto.response.HomeResponse
import com.example.plotting_fe.myplogging.dto.PloggingData
import com.example.plotting_fe.plogging.dto.response.PloggingGetStarResponse
import com.example.plotting_fe.plogging.dto.response.PlowerResponse
import com.example.plotting_fe.plogging.ui.GetPloggings
import com.example.plotting_fe.plogging.ui.PloggingApiService
import com.example.plotting_fe.utils.UIUtils
import java.time.LocalDate
import java.time.LocalDateTime


class MainFragment : Fragment() {

    private lateinit var btnCardnews: Button
    private lateinit var btnCatagotyToday: ImageView
    private lateinit var btnCatagoty15Up: ImageView
    private lateinit var btnCatagotyApprove: ImageView
    private lateinit var btnCatagotyRandom: ImageView

    private lateinit var btnRead: Button
    private lateinit var btnCategotyTodayWillFinish: ImageView
    private lateinit var btnCategoty15Up: ImageView
    private lateinit var btnCategoryApprove: ImageView
    private lateinit var btnCategoryDirect: ImageView
    private lateinit var btnPlower1: ImageView
    private lateinit var btnPlower2: ImageView
    private lateinit var btnPlower3: ImageView
    private lateinit var btnPlower4: ImageView
    private lateinit var userProfile: ImageView
    private lateinit var btnRanking: ImageView
    private lateinit var btnAlarm: ImageView
    private lateinit var btnSearch: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var btnPlowerName1: TextView
    private lateinit var btnPlowerName2: TextView
    private lateinit var btnPlowerName3: TextView
    private lateinit var btnPlowerName4: TextView
    private lateinit var userNickname: TextView
    private val dataList = mutableListOf<PloggingGetStarResponse>()

    private lateinit var homeApiService: HomeApiService
    private lateinit var ploggingApiService: PloggingApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.a_activity_home, container, false)

        btnCardnews = view.findViewById(R.id.btn_read_more)
        btnCatagotyToday = view.findViewById(R.id.categoty_today_finish)
        btnCatagoty15Up = view.findViewById(R.id.categoty_15_up)
        btnCatagotyApprove = view.findViewById(R.id.category_approve)
        btnCatagotyRandom = view.findViewById(R.id.category_random)
        btnAlarm = view.findViewById(R.id.welcome_alarm)
        btnRanking = view.findViewById(R.id.welcome_rank)


        btnCardnews.setOnClickListener {
            val intent = Intent(activity, CardnewsListActivity::class.java)
            startActivity(intent)
        }

        btnRanking.setOnClickListener { v ->
            val navController = Navigation.findNavController(v)
            navController.navigate(R.id.action_home_to_ranking)
        }

        btnAlarm.setOnClickListener {
            val intent = Intent(activity, AlarmActivity::class.java)
            startActivity(intent)
        }

        initializeViews(view)
        setupRecyclerView()
        setupAdapter()
        setDummyData()
        getHome()
        setupButtonListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is ComponentActivity) {
            UIUtils.enableEdgeToEdgeMode(activity as ComponentActivity)
        }

        Handler(Looper.getMainLooper()).post {
            if (activity is ComponentActivity) {
                UIUtils.setupBottomNavigation(activity as ComponentActivity)
            }
        }
    }

    private fun initializeViews(view: View) {
        userProfile = view.findViewById(R.id.welcome_image)
        userNickname = view.findViewById(R.id.welcome_nickname)
        btnRead = view.findViewById(R.id.btn_read_more)
        btnCategotyTodayWillFinish = view.findViewById(R.id.categoty_today_finish)
        btnCategoty15Up = view.findViewById(R.id.categoty_15_up)
        btnCategoryApprove = view.findViewById(R.id.category_approve)
        btnCategoryDirect = view.findViewById(R.id.category_random)
        recyclerView = view.findViewById(R.id.rv_popular_plogging)
        btnPlower1 = view.findViewById(R.id.popular_plowers_first)
        btnPlower2 = view.findViewById(R.id.popular_plowers_second)
        btnPlower3 = view.findViewById(R.id.popular_plowers_third)
        btnPlower4 = view.findViewById(R.id.popular_plowers_fourth)
        btnPlowerName1 = view.findViewById(R.id.plower_name_first)
        btnPlowerName2 = view.findViewById(R.id.plower_name_second)
        btnPlowerName3 = view.findViewById(R.id.plower_name_third)
        btnPlowerName4 = view.findViewById(R.id.plower_name_fourth)
        btnRanking = view.findViewById(R.id.welcome_rank)
        btnAlarm = view.findViewById(R.id.welcome_alarm)
        btnSearch = view.findViewById(R.id.welcome_search)

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private fun setupAdapter() {
        homeAdapter = HomeAdapter(dataList)
        recyclerView.adapter = homeAdapter
    }

    private fun setDummyData() {
        val dummyData = mutableListOf<PloggingGetStarResponse>()
        dummyData.add(
            PloggingGetStarResponse(
                1L,
                "플로깅1",
                5L,
                10L,
                "ASSIGN",
                "2024-12-31",
                "2024-12-15T10:00:00",
                120L,
                "Seoul",
                true
            )
        )
        dummyData.add(
            PloggingGetStarResponse(
                2L,
                "플로깅2",
                10L,
                15L,
                "APPROVE",
                "2024-12-31",
                "2024-12-16T14:00:00",
                150L,
                "Busan",
                false
            )
        )
        homeAdapter.updateDataList(dummyData)
    }

    private fun setupButtonListeners() {
        btnRead.setOnClickListener {
            it.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.button_color_in_home)
            startActivity(Intent(activity, CardnewsListActivity::class.java))
        }

        btnRanking.setOnClickListener { v ->
            val navController = Navigation.findNavController(v)
            navController.navigate(R.id.action_home_to_ranking)
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(activity, GetPloggings::class.java))
        }

        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        if (!::ploggingApiService.isInitialized) {
            ploggingApiService = PloggingApiService()
        }
        val activity = requireActivity() // 또는 activity as FragmentActivity
        btnCategotyTodayWillFinish.setOnClickListener {
            val region = "Seoul"
            val endDate = LocalDate.now().toString()

            ploggingApiService.filterPlogging(
                region,
                "2024-01-01",
                endDate,
                "DIRECT",
                1L,
                LocalDateTime.now().toString(),
                1000L,
                activity
            )
        }

        btnCategoty15Up.setOnClickListener {
            val region = "Seoul"
            ploggingApiService.filterPlogging(
                region,
                "2024-01-01",
                "2025-01-01",
                "DIRECT",
                1L,
                "2024-01-01T01:00:00",
                15L,
                activity
            )
        }

        btnCategoryApprove.setOnClickListener {
            val region = "Seoul"
            ploggingApiService.filterPlogging(
                region,
                "2024-01-01",
                "2025-01-01",
                "APPROVE",
                1L,
                "2024-01-01T01:00:00",
                1000L,
                activity
            )
        }

        btnCategoryDirect.setOnClickListener {
            val region = "Seoul"
            ploggingApiService.filterPlogging(
                region,
                "2024-01-01",
                "2025-01-01",
                "DIRECT",
                1L,
                "2024-01-01T01:00:00",
                1000L,
                activity
            )
        }
    }

    private fun getHome() {
        homeApiService = HomeApiService()
        homeApiService.getHome(object : HomeResponseListener {
            override fun onHomeDataReceived(homeResponse: HomeResponse) {
                // 홈 데이터 처리
                Toast.makeText(activity, "반갑습니다!", Toast.LENGTH_SHORT).show()
                Log.d("MainFragment", "Home data received: $homeResponse")
            }

            override fun onPloggingDataReceived(ploggingGetStarListResponse: List<PloggingGetStarResponse>) {
                // 플로깅 데이터 처리
                homeAdapter.updateDataList(ploggingGetStarListResponse)
            }

            override fun onPlowerDataReceived(plowerList: List<PlowerResponse>) {
                // 플로워 데이터 처리
                Log.d("Plower", plowerList.toString())

                // 각 플로워에 대해 처리
                for (index in plowerList.indices) {
                    val plower = plowerList[index]

                    when (index) {
                        0 -> {
                            // 첫 번째 플로워 설정
                            btnPlowerName1.text = plower.nickname
                            Glide.with(activity!!)
                                .load(plower.profileImageUrl)
                                .placeholder(R.drawable.ic_icon_round)
                                .into(btnPlower1)
                        }

                        1 -> {
                            // 두 번째 플로워 설정
                            btnPlowerName2.text = plower.nickname
                            Glide.with(activity!!)
                                .load(plower.profileImageUrl)
                                .placeholder(R.drawable.ic_icon_round)
                                .into(btnPlower2)
                        }

                        2 -> {
                            // 세 번째 플로워 설정
                            btnPlowerName3.text = plower.nickname
                            Glide.with(activity!!)
                                .load(plower.profileImageUrl)
                                .placeholder(R.drawable.ic_icon_round)
                                .into(btnPlower3)
                        }

                        3 -> {
                            // 네 번째 플로워 설정
                            btnPlowerName4.text = plower.nickname
                            Glide.with(activity!!)
                                .load(plower.profileImageUrl)
                                .placeholder(R.drawable.ic_icon_round)
                                .into(btnPlower4)
                        }

                        else -> {}
                    }
                }
            }

            override fun onUserDataReceiver(nickname: String) {
                // 사용자 닉네임 처리
                userNickname.text = nickname
            }

            override fun onError(errorMessage: String) {
                // 에러 처리
                Log.e("MainFragment", "Error fetching home data: $errorMessage")
                Toast.makeText(activity, "실패: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        })

        //    private fun setStarIcon(btnStar: ImageView, star: Boolean) {
//        // 색이 채워진 별과 회색 별 아이콘을 Drawable로 가져오기
//        val colorStar = ContextCompat.getDrawable(btnStar.context, R.drawable.ic_star_color)
//        val grayStar = ContextCompat.getDrawable(btnStar.context, R.drawable.ic_star_gray)
//
//        // star 값에 따라 아이콘 설정
//        if (star) {
//            btnStar.setImageDrawable(colorStar) // star가 true일 때, 채워진 별 아이콘 설정
//        } else {
//            btnStar.setImageDrawable(grayStar) // star가 false일 때, 회색 별 아이콘 설정
//        }
//    }\
    }
}