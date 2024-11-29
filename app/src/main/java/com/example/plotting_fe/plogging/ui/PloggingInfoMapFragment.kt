package com.example.plotting_fe.plogging.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plotting.server.plogging.dto.response.PloggingMapResponse

class PloggingInfoMapFragment : DialogFragment() {

    companion object {
        private const val ARG_PLOGGING_INFO = "plogging_info"

        // JSON 문자열을 받는 newInstance 메서드
        fun newInstance(ploggingInfoJson: String): PloggingInfoMapFragment {
            val fragment = PloggingInfoMapFragment()
            val args = Bundle().apply {
                putString(ARG_PLOGGING_INFO, ploggingInfoJson) // JSON 문자열 전달
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var ploggingInfoList: List<PloggingMapResponse>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PloggingMapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // arguments에서 JSON 문자열을 꺼내 List<PloggingMapResponse>로 변환
        val json = arguments?.getString(ARG_PLOGGING_INFO)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<PloggingMapResponse>>() {}.type
            ploggingInfoList = Gson().fromJson(json, type)
        } else {
            ploggingInfoList = emptyList() // null일 경우 빈 리스트로 초기화
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // DialogFragment가 dismiss될 때 부모 fragment의 메서드를 호출
        (parentFragment as? PloggingMapFragment)?.resetSelectedMarker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plogging_info_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPlogging)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // RecyclerView Adapter 설정
        adapter = PloggingMapAdapter(requireContext(),ploggingInfoList!!)
        recyclerView.adapter = adapter
    }
}
