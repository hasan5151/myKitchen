package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.KitchenOrderModel
import com.yaros.kitchen.models.OrderModel
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*


class KitchenOrderAdapter(val orders: ArrayList<KitchenOrderModel>,val context: Context): RecyclerView.Adapter<KitchenOrderAdapter.KitchenVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_order_adapter, parent, false)
        return KitchenVH(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: KitchenVH, position: Int) {
        val order: KitchenOrderModel = orders.get(position)
        holder.orderId.text ="№ ${order.id}"
        holder.workerName.text = order.waiterName

        val orderModel = OrderModel()
        orderModel.id = order.id
        orderModel.workerName = order.waiterName

        if (order.orderItemsModelList!=null) {
            val kitchenItemAdapter = object :
                KitchenItemAdapter(order.orderItemsModelList, orderModel, context){
                override fun ItemSize(itemSize: Int) {
                    if (itemSize==0)
                        removeItem(position)
                }

            }
            holder.recyclerView.adapter = kitchenItemAdapter
        }

    }

    private fun removeItem(position: Int) {
        orders?.removeAt(position)
        notifyDataSetChanged()

    }



    class KitchenVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId = itemView.orderId
        val workerName = itemView.workerName
        val recyclerView = itemView.recyclerView
    }


}