package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.StopListModel
import kotlinx.android.synthetic.main.stop_list_adapter.view.*

abstract class StopListAdapter(val stopList :ArrayList<StopListModel>, val context : Context) : RecyclerView.Adapter<StopListAdapter.StopListVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopListVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.stop_list_adapter, parent, false)
        return StopListVH(view)
    }

    override fun getItemCount(): Int = stopList.size

    override fun onBindViewHolder(holder: StopListVH, position: Int) {
        val stop = stopList.get(position)
        holder.name.text = stop.name
        holder.type.text = stop.type

        if (stop.isStop)
            holder.type.setTextColor(ContextCompat.getColor(context, R.color.red))
        else
            holder.type.setTextColor(ContextCompat.getColor(context, R.color.popupcolor))

        holder.itemView.setOnClickListener({
            onClick(stopList,position)
        })
    }

    class StopListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val type = itemView.type
        val clear = itemView.clear
    }

    abstract fun onClick(stop: ArrayList<StopListModel>, position : Int)
}
