package com.example.plotting_fe.home.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.home.dto.response.RankingListResponse
import com.example.plotting_fe.home.dto.response.RankingResponse
import com.example.plotting_fe.home.presentation.RankingController
import com.example.plotting_fe.mypage.dto.response.DetailProfileResponse
import com.example.plotting_fe.myplogging.ui.MyPloggingHomeActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_ranking, container, false)

        fetchRanking()
        return view
    }

    private fun fetchRanking() {
        val rankingController: RankingController = ApiClient.getApiClient().create(RankingController::class.java)
        rankingController.getRankingList().enqueue(object :
            Callback<ResponseTemplate<RankingListResponse>>{
            override fun onResponse(
                call: Call<ResponseTemplate<RankingListResponse>>,
                response: Response<ResponseTemplate<RankingListResponse>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val data = response.body()?.results
                    if(data !=null){
                        updateUI(data.topRankings, data.myRanking)
                    }else{
                        Log.d("get", "onResponse 성공: data==null")
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(
                call: Call<ResponseTemplate<RankingListResponse>>,
                t: Throwable
            ) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        });

    }

    private fun updateUI(topRankings:List<RankingResponse>, myRanking: RankingResponse){
//        my ranking
        val profileImageView: AppCompatImageView = requireView().findViewById(R.id.iv_my)
        Glide.with(this)
            .load(myRanking.profileImageUrl)
            .placeholder(R.drawable.ic_flower)
            .into(profileImageView)

        val myRank:TextView = requireView().findViewById(R.id.ranking_my)
        val myNickname:TextView = requireView().findViewById(R.id.nickname_my)
        val myhour:TextView = requireView().findViewById(R.id.hour_my)
        val mycount:TextView = requireView().findViewById(R.id.count_my)

        myRank.text = if (myRanking.hourRank.toInt() == 0) "- 등" else "${myRanking.hourRank} 등"
        myNickname.text = myRanking.nickname
        myhour.text = (myRanking.totalHours/60).toString()+" H"
        mycount.text = myRanking.totalCount.toString()+" 회"

        val activity = requireActivity()
        if (activity is MyPloggingHomeActivity) {
            activity.updateMyRankText(myRank.text.toString())
        }

//        top ranking
        val rankingContainers = listOf(
            R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7
        )

        for (i in rankingContainers.indices) {
            val container: View? = requireView().findViewById(rankingContainers[i])

            container?.let {
                if (i < topRankings.size) {
                    val ranking = topRankings[i]

                    val profileImageView: AppCompatImageView? = it.findViewById(
                        resources.getIdentifier("iv_${i + 1}", "id", requireContext().packageName)
                    )
                    val nicknameTextView: TextView? = it.findViewById(
                        resources.getIdentifier("nickname_${i + 1}", "id", requireContext().packageName)
                    )
                    val hourTextView: TextView? = it.findViewById(
                        resources.getIdentifier("hour_${i + 1}", "id", requireContext().packageName)
                    )
                    val countTextView: TextView? = it.findViewById(
                        resources.getIdentifier("count_${i + 1}", "id", requireContext().packageName)
                    )

                    profileImageView?.let { imageView ->
                        Glide.with(this)
                            .load(ranking.profileImageUrl)
                            .placeholder(R.drawable.ic_flower)
                            .into(imageView)
                    }

                    nicknameTextView?.text = ranking.nickname
                    hourTextView?.text = "${ranking.totalHours} H"
                    countTextView?.text = "${ranking.totalCount} 회"

                    it.visibility = View.VISIBLE
                } else {
                    if ((i + 1) >= 5) {
                        // Divider ID를 직접 참조
                        val dividerId = when (i + 1) {
                            5 -> R.id.view_5
                            6 -> R.id.view_6
                            7 -> R.id.view_7
                            else -> null
                        }
                        dividerId?.let { id ->
                            val divider: View? = it.findViewById(id)
                            divider?.visibility = View.GONE // Divider 숨기기
                        }
                    }
                    it.visibility = View.GONE
                }
            }
        }

    }

}