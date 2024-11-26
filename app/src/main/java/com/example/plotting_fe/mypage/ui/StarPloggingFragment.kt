package com.example.plotting_fe.mypage.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.mypage.dto.Plogging
import com.example.plotting_fe.mypage.dto.response.MyPloggingStarListResponse
import com.example.plotting_fe.mypage.presentation.StarController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarPloggingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PloggingAdapter
    private val ploggingList: MutableList<Plogging> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_star_plogging, container, false)

        recyclerView = view.findViewById(R.id.ploggingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchStarredPlogging()

        // NavController 초기화
//        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        // 어댑터 초기화 시 클릭 리스너 추가
        adapter = PloggingAdapter(ploggingList, object : PloggingAdapter.OnPloggingClickListener {
            override fun onPloggingClick(plogging: Plogging){
                // ProfileDetailFragment로 이동
//                val args = Bundle().apply {
//                    putLong("userId", person.userId)
//                }
//                navController.navigate(R.id.action_star_to_profile_detail, args)
            }
        })

        recyclerView.adapter = adapter

        return view
    }
    private fun fetchStarredPlogging(){
        val userStarApi = ApiClient.getApiClient().create(StarController::class.java)

        userStarApi.getMyPloggingStarList().enqueue(object :
            Callback<ResponseTemplate<MyPloggingStarListResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyPloggingStarListResponse>>,
                response: Response<ResponseTemplate<MyPloggingStarListResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val responseBody = response.body()?.results
                    if(responseBody?.responses  != null){
                        updateRecyclerView(responseBody.responses )
                    }else{
                        Log.d("get", "onResponse 성공: responseBody  == null")
                        updateRecyclerView(emptyList())
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<MyPloggingStarListResponse>>,
                t: Throwable
            ) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }

        })
    }
    private fun updateRecyclerView(newList: List<Plogging>) {
        ploggingList.clear()
        ploggingList.addAll(newList)
        adapter.notifyDataSetChanged()
    }
}
