package com.example.plotting_fe.mypage.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.mypage.dto.Person
import com.example.plotting_fe.mypage.dto.response.MyUserStarListResponse
import com.example.plotting_fe.mypage.presentation.StarController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarPeopleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PeopleAdapter
    private val peopleList: MutableList<Person> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_star_people, container, false)

        recyclerView = view.findViewById(R.id.participantsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 데이터 로드 및 리사이클러 뷰 업데이트
        fetchUserStarList()

        // NavController 초기화
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        // 어댑터 초기화 시 클릭 리스너 추가
        adapter = PeopleAdapter(peopleList, object : PeopleAdapter.OnPersonClickListener {
            override fun onPersonClick(person: Person) {
                // ProfileDetailFragment로 이동
                val args = Bundle().apply {
                    putLong("userId", person.userId)
                }
                navController.navigate(R.id.action_star_to_profile_detail, args)
            }
        })

        recyclerView.adapter = adapter

        return view
    }

    private fun fetchUserStarList(){
        val userStarApi = ApiClient.getApiClient().create(StarController::class.java)

        userStarApi.getMyUserStarList().enqueue(object :
            Callback<ResponseTemplate<MyUserStarListResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<MyUserStarListResponse>>,
                response: Response<ResponseTemplate<MyUserStarListResponse>>
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
                call: Call<ResponseTemplate<MyUserStarListResponse>>,
                t: Throwable
            ) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }

        })
    }

    private fun updateRecyclerView(newList: List<Person>) {
        peopleList.clear()
        peopleList.addAll(newList)
        adapter.notifyDataSetChanged()
    }



}
