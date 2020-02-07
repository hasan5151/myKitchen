package com.yaros.kitchen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.room.entity.KitchenOrderModel
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*

abstract class OrderPageAdapter : PagedListAdapter<KitchenOrderModel, OrderPageAdapter.OrderVH>(DIFF_CALLBACK) {

    companion object {
        protected val DIFF_CALLBACK: DiffUtil.ItemCallback<KitchenOrderModel> =
            object : DiffUtil.ItemCallback<KitchenOrderModel>() {
                override fun areItemsTheSame(
                    oldConcert: KitchenOrderModel,
                    newConcert: KitchenOrderModel
                ): Boolean {
                    return oldConcert.order_item === newConcert.order_item
                }

                override fun areContentsTheSame(
                    oldConcert: KitchenOrderModel,
                    newConcert: KitchenOrderModel
                ): Boolean {
                    return oldConcert.equals(newConcert)
                }
            }
    }

    class OrderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId = itemView.orderId
        val waiterName = itemView.waiterName
        val recyclerView = itemView.recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_order_adapter, parent, false)
        return OrderVH(view)
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        val order = getItem(position)
        holder.orderId.text ="№ ${order?.number}"
        holder.waiterName.text = order?.waiterName
        setItemAdapter(holder.recyclerView,order)
    }

    abstract fun setItemAdapter(recyclerView: RecyclerView,orderModel : KitchenOrderModel?)
}