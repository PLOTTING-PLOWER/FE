package com.example.plotting_fe.myplogging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plotting_fe.R
import com.example.plotting_fe.myplogging.dto.PloggingData

class MyPloggingCreatedAdapter(private val items: List<PloggingData>) : RecyclerView.Adapter<MyPloggingCreatedAdapter.PloggingViewHolder>() {

    class PloggingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val startLocation: TextView = itemView.findViewById(R.id.tvStartLocation)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
        val spendTime: TextView = itemView.findViewById(R.id.tvSpendTime)
        val ploggintType: TextView = itemView.findViewById(R.id.tv_plogging_type)
        val currentPeople: TextView = itemView.findViewById(R.id.tvCurrentPeople)
        val maxPeople: TextView = itemView.findViewById(R.id.tvMaxPeople)

        fun bind(item: PloggingData) {
            title.text = item.title
            startLocation.text = item.startLocation
            startTime.text = item.startTime
            spendTime.text = (item.spendTime / 60).toString()
            currentPeople.text = item.currentPeople.toString()
            maxPeople.text = item.maxPeople.toString()

            if (item.ploggingType.name == "DIRECT") {
                ploggintType.text = "선착순"
            } else {
                ploggintType.text = "승인제"
            }
        }
    }

    override fun onBindViewHolder(holder: PloggingViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_plogging, parent, false)
        return PloggingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}