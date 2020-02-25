package com.yaros.kitchen.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yaros.kitchen.R
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.entity.KitchenModel
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.utils.TVDrawable
import kotlinx.android.synthetic.main.kitchen_adapter.view.*
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.badge
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.constraint
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.elapsedTime
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.orderTime
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.subTitle
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.title
import java.lang.NumberFormatException

abstract class KitchenSupAdapter (val context : Context): PagedListAdapter<KitchenModel, KitchenSupAdapter.ItemVH>(DIFF_CALLBACK) {
    companion object {
        protected val DIFF_CALLBACK: DiffUtil.ItemCallback<KitchenModel> =
            object : DiffUtil.ItemCallback<KitchenModel>() {
                override fun areItemsTheSame(oldItem: KitchenModel, newItem: KitchenModel) : Boolean {
                    return  oldItem.id.equals(newItem.id)
                }
                override fun areContentsTheSame(oldItem: KitchenModel, newItem: KitchenModel) : Boolean {
                    if (!oldItem.reqTime.equals(newItem.reqTime))
                        return true
                    else if (!oldItem.countDownStatus.equals(newItem.countDownStatus))
                        return true
                    return oldItem.equals(newItem)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_adapter, parent, false)
        return ItemVH(view)
    }

    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badge = itemView.badge
        val title= itemView.title
        val subTitle= itemView.subTitle
        val orderTime =  itemView.orderTime
        val elapsedTime=  itemView.elapsedTime
        val constraint=  itemView.constraint
        val orderId=  itemView.orderId
        val waiterName=  itemView.waiterName
        val printerName=  itemView.printerName
        val cancelledOrders=  itemView.cancelledOrders
        val hideOrder=  itemView.hideOrder
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val tvDrawable = TVDrawable(context)
        val item = getItem(position)

        holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.timecolor))
        holder.orderId.text ="№ ${item?.number}"
        holder.waiterName.text = item?.waiterName
        holder.printerName.text = item?.printerName

        val gson = Gson()
        System.out.println(gson.toJson(item,KitchenModel::class.java))

        if (item!!.cancelledOrders>0){
            holder.cancelledOrders.visibility = View.VISIBLE
            holder.hideOrder.visibility = View.VISIBLE
            holder.cancelledOrders.text = "отменено"

            holder.hideOrder.setOnClickListener {
                hideOrder(item)
            }

        }else{
            holder.cancelledOrders.visibility = View.GONE
            holder.hideOrder.visibility = View.GONE

            holder.constraint.setOnClickListener {
                showPopup(item,item.id)
            }

        }

        if (item!!.reqTime>0L) {
            countDown(item, holder)
            holder.elapsedTime.text=""
        }

        if (item.date!!.contentEquals(context.resources.getString(R.string.cancel))){
            holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.red))
            holder.orderTime.text= "${item.date}       "
            holder.elapsedTime.text = ""
        }
        else if (item.date!!.contentEquals(context.resources.getString(R.string.ready))) {
            holder.orderTime.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.orderTime.text = "${item.date}        "
            holder.elapsedTime.text = ""
        } else{
            if (DateUtil.remainCookTime(
                    item.date.replace(" ", "").toLong(),
                    item.reqTime,
//                    item.reqTime * item.count,
                    Preferences.getPref("diff", "0", context)?.toLong()!!
                ) <1000) {
                holder.elapsedTime.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                tvDrawable.drawSize(holder.elapsedTime,R.drawable.error,0.9,false)
                // updateRemainTime(item, 0)
            }else{
                holder.elapsedTime.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )

                holder.elapsedTime.setTextColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.black
                    )
                )
            }
            holder.orderTime.text= "${DateUtil.getHourandMinute(item.date?.replace(" ","")?.toLong())}    |    "
        }

        if(item.count!=null)
            if (item.count>1){
                holder.badge.text = "${item?.count}"
                holder.badge.visibility = View.VISIBLE
                val paramsTitle =
                    holder.title.getLayoutParams() as ConstraintLayout.LayoutParams
                paramsTitle.topToBottom = R.id.orderId
                paramsTitle.leftToRight =R.id.badge
                paramsTitle.leftMargin=16
                holder.title.layoutParams =paramsTitle
            } else{

                holder.badge.visibility = View.GONE

                val paramsTitle =
                    holder.title.getLayoutParams() as ConstraintLayout.LayoutParams

                paramsTitle.topToBottom = R.id.orderId
                paramsTitle.leftToRight= R.id.badge
                paramsTitle.leftMargin = 0
                holder.title.layoutParams =paramsTitle
            }

        holder.title.text= item.name!!

        if (!isNullOrEmpty(item.comment)){
            holder.subTitle.text= "• ${item.comment}"
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


    }

    private fun countDown(item : KitchenModel, holder: ItemVH) {
        if (item.countDownStatus <2) {
            try {
                object : CountDownTimer(
                    DateUtil.remainCookTime(
                        item.date.replace(" ", "").toLong(),
                        item.reqTime,
//                        item.reqTime * item.count,
                        Preferences.getPref("diff", "0", context)?.toLong()!!
                    ), 1000
                ) { //TODO change this
                    override fun onFinish() {
                        if (item.reqTime <1000) {
                            holder.elapsedTime.text = "00:00  "
                            holder.elapsedTime.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red
                                )
                            )
                            TVDrawable(context).drawSize(holder.elapsedTime,R.drawable.error,0.9,false)
                            updateRemainTime(item, 0)
                        }
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        holder.elapsedTime.text = "${DateUtil.cookTimeDate(millisUntilFinished)}"
                        updateRemainTime(item, millisUntilFinished)
                    }
                }.start()
                    .let { startCountDown(item, it) }
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }
        }
    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

    abstract fun updateRemainTime(item: KitchenModel, milisUntilFinish : Long)
    abstract fun startCountDown(item: KitchenModel, countDownTimer: CountDownTimer)
    abstract fun showPopup(item: KitchenModel, orderId: Int)
    abstract fun hideOrder(item: KitchenModel)

}