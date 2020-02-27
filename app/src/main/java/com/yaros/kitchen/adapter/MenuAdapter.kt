package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.room.entity.PrintersModel
import kotlinx.android.synthetic.main.menu_adapter.view.*

abstract class MenuAdapter (val printers : List<PrintersModel>,val context : Context)  : RecyclerView.Adapter<MenuAdapter.MenuVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.menu_adapter, parent, false)
        return MenuVH(view)
    }

    override fun getItemCount(): Int = printers.size

    override fun onBindViewHolder(holder: MenuVH, position: Int) {
        val rainbow: IntArray = context.getResources().getIntArray(R.array.buttoncolors)
        holder.button.setBackgroundColor(rainbow[position%5])
        val printer = printers.get(position)
        holder.button.text = printer.name
        holder.button.setOnClickListener({
            onClick(printer)
        })
    }

    class MenuVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button = itemView.button
    }

    abstract fun onClick(printer: PrintersModel)
}