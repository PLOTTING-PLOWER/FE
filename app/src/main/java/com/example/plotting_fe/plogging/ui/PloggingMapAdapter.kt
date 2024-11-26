package com.example.plotting_fe.plogging.ui

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plotting_fe.R;
import com.plotting.server.plogging.dto.response.PloggingMapResponse

class PloggingMapAdapter(private val itemList: List<PloggingMapResponse>) :
    RecyclerView.Adapter<PloggingMapAdapter.PloggingMapViewHolder>() {

    inner class PloggingMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPloggingType: TextView = view.findViewById(R.id.tvploggingType)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvCurrentPeople: TextView = view.findViewById(R.id.tvCurrentPeople)
        val tvMaxPeople: TextView = view.findViewById(R.id.tvMaxPeople)
        val tvStartLocation: TextView = view.findViewById(R.id.tvStartLocation)
        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvSpendTime: TextView = view.findViewById(R.id.tvSpendTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PloggingMapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plogging_info, parent, false)
        return PloggingMapViewHolder(view)
    }

    override fun onBindViewHolder(holder: PloggingMapViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.tvPloggingType.text = currentItem.ploggingType
        holder.tvTitle.text = currentItem.title
        holder.tvCurrentPeople.text = currentItem.currentPeople.toString()
        holder.tvMaxPeople.text = currentItem.maxPeople.toString()
        holder.tvStartLocation.text = currentItem.startLocation
        holder.tvStartTime.text = currentItem.startTime
        holder.tvSpendTime.text = currentItem.spendTime.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
