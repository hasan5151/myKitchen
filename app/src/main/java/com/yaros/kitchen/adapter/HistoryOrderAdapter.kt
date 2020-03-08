package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.HistoryOrderModel
import com.yaros.kitchen.models.OrderModel
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*


open class HistoryOrderAdapter(val orders: List<HistoryOrderModel>, val context: Context): RecyclerView.Adapter<HistoryOrderAdapter.KitchenVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenVH {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.kitchen_order_adapter, parent, false)
        return KitchenVH(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: KitchenVH, position: Int) {
        System.out.println("sikerim histroy2 ")
        val order: HistoryOrderModel = orders[position]
        holder.orderId.text ="№ ${order.id}"
        holder.waiterName.text = order.waiterName

        val orderModel = OrderModel()
        orderModel.id = order.id
        orderModel.workerName = order.waiterName

        if (order.orderItemsModelList!=null) {
            val kitchenItemAdapter = HistoryItemAdapter(order.orderItemsModelList, orderModel, context)
            holder.recyclerView.adapter = kitchenItemAdapter
        }
    }

    class KitchenVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId = itemView.orderId
        val waiterName = itemView.waiterName
        val recyclerView = itemView.recyclerView
    }
}