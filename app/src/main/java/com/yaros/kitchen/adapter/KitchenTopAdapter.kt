package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.entity.KitchenModel
  import kotlinx.android.synthetic.main.kitchen_adapter.view.orderId
import kotlinx.android.synthetic.main.kitchen_adapter.view.waiterName
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*


abstract class KitchenTopAdapter(val context : Context): PagedListAdapter<KitchenTop, KitchenTopAdapter.ItemVH>(DIFF_CALLBACK) {
    companion object {
        protected val DIFF_CALLBACK: DiffUtil.ItemCallback<KitchenTop> =
            object : DiffUtil.ItemCallback<KitchenTop>() {
                override fun areItemsTheSame(
                    oldItem: KitchenTop,
                    newItem: KitchenTop
                ): Boolean {
                    return oldItem.item_number.equals(newItem.item_number)
                }

                override fun areContentsTheSame(
                    oldItem: KitchenTop,
                    newItem: KitchenTop
                ): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_order_adapter, parent, false)
        return ItemVH(view)
    }

    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId = itemView.orderId
        val waiterName = itemView.waiterName
        val recyclerView = itemView.recyclerView
        val subInfo = itemView.subInfo
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val kitchenModel = getItem(position)
        holder.orderId.text = "№:  ${kitchenModel?.number}"
        holder.waiterName.text = kitchenModel?.waiterName
        holder.subInfo.text = " Чек №: ${kitchenModel?.item_number} "
        setItemAdapter(holder.recyclerView,kitchenModel?.item_order)
    }

    abstract fun setItemAdapter(recyclerView: RecyclerView?,item_order : String?)

}