package com.yaros.kitchen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.room.entity.DishesModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import kotlinx.android.synthetic.main.menu_item_adapter.view.*

abstract class MenuItemAdapter(val items : List<DishesModel>)  : RecyclerView.Adapter<MenuItemAdapter.MenuItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.menu_item_adapter, parent, false)
        return MenuItemVH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MenuItemVH, position: Int) {
        val item = items.get(position)
        holder.name.text = item.name
        holder.itemView.setOnClickListener({ onClick(item)
        })
    }

    class MenuItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val explanation = itemView.explanation
    }

    abstract fun onClick (dish: DishesModel)
}