package com.yaros.kitchen.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.HistoryItemModel
import com.yaros.kitchen.models.OrderModel
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.*
import java.util.*


class HistoryItemAdapter (val items: List<HistoryItemModel>?, val order: OrderModel, val context: Context): RecyclerView.Adapter<HistoryItemAdapter.KitchenItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_item_adapter, parent, false)
        return KitchenItemVH(view)

    }

    override fun getItemCount(): Int = items!!.size

    class KitchenItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badge = itemView.badge
        val title= itemView.title
        val subTitle= itemView.subTitle
        val orderTime =  itemView.orderTime
        val elapsedTime=  itemView.elapsedTime
    }

    override fun onBindViewHolder(holder: KitchenItemVH, position: Int) {
        val item = items!!.get(position)
        holder.orderTime.text= "${item.orderTime}    |    "
        holder.elapsedTime.text= item.reqTime
/*        if(item.badge!=null)
        if (item.badge>1){
            holder.badge.text = "${item.badge}"
            holder.badge.visibility = View.VISIBLE
            val paramsTitle =
                holder.title.getLayoutParams() as ConstraintLayout.LayoutParams
            paramsTitle.leftToRight =R.id.badge
            paramsTitle.leftMargin=16
            holder.title.layoutParams =paramsTitle
        } else{*/
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 0)
            holder.badge.visibility = View.INVISIBLE
            holder.title.layoutParams =params
     //   }

        holder.title.text= item.title!!

 /*       if (!isNullOrEmpty(item.subTitle)){
            holder.subTitle.text= "• ${item.subTitle}"
            holder.subTitle.visibility = View.VISIBLE

            val paramsOrderTime =
                holder.orderTime.getLayoutParams() as ConstraintLayout.LayoutParams
            holder.orderTime.getLayoutParams() as ConstraintLayout.LayoutParams
            paramsOrderTime.topToBottom= R.id.subTitle

            val paramsElapsedTime =
                holder.elapsedTime.getLayoutParams() as ConstraintLayout.LayoutParams
            holder.elapsedTime.getLayoutParams() as ConstraintLayout.LayoutParams
            paramsElapsedTime.topToBottom= R.id.subTitle
            paramsElapsedTime.leftToRight= R.id.orderTime

            holder.orderTime.setLayoutParams(paramsOrderTime)
            holder.elapsedTime.setLayoutParams(paramsElapsedTime)
        } else{*/
            holder.subTitle.visibility = View.INVISIBLE

            val paramsOrderTime =
               holder.orderTime.getLayoutParams() as ConstraintLayout.LayoutParams
               holder.orderTime.getLayoutParams() as ConstraintLayout.LayoutParams
            paramsOrderTime.topToBottom= R.id.title

            val paramsElapsedTime =
               holder.elapsedTime.getLayoutParams() as ConstraintLayout.LayoutParams
               holder.elapsedTime.getLayoutParams() as ConstraintLayout.LayoutParams
            paramsElapsedTime.topToBottom= R.id.title
            paramsElapsedTime.leftToRight= R.id.orderTime

            holder.orderTime.setLayoutParams(paramsOrderTime)
            holder.elapsedTime.setLayoutParams(paramsElapsedTime)
      //  }


        if (!item.cookedOnTime){
            holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.red))
            holder.elapsedTime.setTextColor(ContextCompat.getColor(context,R.color.red))
        }else{
            holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.timecolor))
            holder.elapsedTime.setTextColor(ContextCompat.getColor(context,android.R.color.black))

        }
    }


    fun isNullOrEmpty(str: CharSequence): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }




}

