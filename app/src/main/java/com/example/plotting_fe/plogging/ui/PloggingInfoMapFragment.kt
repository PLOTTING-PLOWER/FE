package com.example.plotting_fe.plogging.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.plotting.server.plogging.dto.response.PloggingMapResponse

class PloggingInfoMapFragment : Fragment() {

    companion object {
        private const val ARG_PLOGGING_INFO = "plogging_info"

        fun newInstance(ploggingInfo: List<PloggingMapResponse>): PloggingInfoMapFragment {
            val fragment = PloggingInfoMapFragment()
            val args = Bundle().apply {
                putSerializable(ARG_PLOGGING_INFO, ArrayList(ploggingInfo))
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
        arguments?.let {
            ploggingInfoList = it.getSerializable(ARG_PLOGGING_INFO) as List<PloggingMapResponse>
        }
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

        ploggingInfoList?.let {
            adapter = PloggingMapAdapter(it)
            recyclerView.adapter = adapter
        }
    }
}
