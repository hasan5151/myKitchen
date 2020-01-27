package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import com.yaros.kitchen.utils.DialogUtil
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.*

abstract class ItemPageAdapter (val context : Context): PagedListAdapter<KitchenItemModel, ItemPageAdapter.ItemVH>(DIFF_CALLBACK) {

    companion object {
        protected val DIFF_CALLBACK: DiffUtil.ItemCallback<KitchenItemModel> =
            object : DiffUtil.ItemCallback<KitchenItemModel>() {
                override fun areItemsTheSame(
                    oldItem: KitchenItemModel,
                    newItem: KitchenItemModel
                ): Boolean {
                    return oldItem.id === newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: KitchenItemModel,
                    newItem: KitchenItemModel
                ): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }


    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badge = itemView.badge
        val title= itemView.title
        val subTitle= itemView.subTitle
        val orderTime =  itemView.orderTime
        val elapsedTime=  itemView.elapsedTime
        val constraint=  itemView.constraint
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = getItem(position)
        holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.timecolor))
        holder.orderTime.text= "${item?.orderTime}    |    "
        startReqCountDown(item!!)

        if (item?.orderTime!!.contentEquals(context.resources.getString(R.string.cancel)))
            holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.red))
        else if (item.orderTime.contentEquals(context.resources.getString(R.string.ready)))
            holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.green))

        if (item.reqTime.contentEquals("00:00  ")){
            holder.elapsedTime.setTextColor(ContextCompat.getColor(context,R.color.red))
            holder.elapsedTime.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(context,R.drawable.warning),null)
        }

        holder.elapsedTime.text= item?.reqTime
        if(item?.badge!=null)
            if (item.badge>1){
                holder.badge.text = "${item?.badge}"
                holder.badge.visibility = View.VISIBLE
                val paramsTitle =
                    holder.title.getLayoutParams() as ConstraintLayout.LayoutParams
                paramsTitle.leftToRight =R.id.badge
                paramsTitle.leftMargin=16
                holder.title.layoutParams =paramsTitle
            } else{
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 0)
                holder.badge.visibility = View.INVISIBLE
                holder.title.layoutParams =params
            }

        holder.title.text= item?.title!!

        if (!isNullOrEmpty(item?.subTitle)){
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
        } else{
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
        }


        //TODO use here countTimer
        /*      if(!item.countDown) {
                  countDownMeal(
                      item,
                      item.reqTime.replace(":", "").toLong(),
                      holder,
                      object : CountDownFinish {
                          override fun onFinish() {
                              removeItem(holder.adapterPosition)
                          }
                      })
                  item.countDown =true

              } else{
                  holder.elapsedTime.text =item.reqTime
              }*/

        holder.constraint.setOnClickListener { showPopup(item,item.orderId) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_item_adapter, parent, false)
        return ItemVH(view)
    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }


    abstract fun startReqCountDown(item: KitchenItemModel)
    abstract fun showPopup(item: KitchenItemModel, orderId: Int)



}