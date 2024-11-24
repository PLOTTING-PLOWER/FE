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

        fun newInstance(ploggingInfo: List<PloggingMapResponse>): PloggingInfoMapFragment {
            val fragment = PloggingInfoMapFragment()
            val args = Bundle().apply {
                putString(ARG_PLOGGING_INFO, Gson().toJson(ploggingInfo)) // Convert List to JSON
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

        // Convert JSON string back to List<PloggingMapResponse>
        val json = arguments?.getString(ARG_PLOGGING_INFO)
        if (json != null) {
            val type = object : TypeToken<List<PloggingMapResponse>>() {}.type
            ploggingInfoList = Gson().fromJson(json, type)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
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

        if (ploggingInfoList.isNullOrEmpty()) {
            recyclerView.visibility = View.GONE
            return
        }

        adapter = PloggingMapAdapter(ploggingInfoList!!)
        recyclerView.adapter = adapter
    }
}
