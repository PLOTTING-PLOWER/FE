package com.example.plotting_fe.myplogging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.PloggingData

class MyCompletePloggingCreatedAdapter(private val items: List<PloggingData>) : RecyclerView.Adapter<MyCompletePloggingCreatedAdapter.PloggingViewHolder>() {

    class PloggingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val location: TextView = itemView.findViewById(R.id.tvStartLocation)
        val time: TextView = itemView.findViewById(R.id.tvStartTime)
        val duration: TextView = itemView.findViewById(R.id.tvSpendTime)
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.location.text = item.location
        holder.time.text = item.startTime
        holder.duration.text = item.duration.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_complete_plogging, parent, false)
        return PloggingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}