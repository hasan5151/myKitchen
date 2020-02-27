package com.yaros.kitchen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.room.entity.PrintersModel
import kotlinx.android.synthetic.main.search_adapter.view.*

abstract class SearchAdapter(val breadcrumbs : List<PrintersModel>) : RecyclerView.Adapter<SearchAdapter.SearchVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.search_adapter, parent, false)
        return SearchVH(view)
    }

    override fun getItemCount(): Int =breadcrumbs.size

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        val breadcrumb = breadcrumbs.get(position)
        holder.textView.text = "${breadcrumb.name}  >  "
        holder.textView.setOnClickListener({
            onClick(breadcrumb,position)
        })
    }

    class SearchVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.textView
    }

    abstract fun onClick(printersModel: PrintersModel,pos : Int)
}