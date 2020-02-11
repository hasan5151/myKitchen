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
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.utils.TVDrawable
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.*
import java.lang.NumberFormatException

abstract class  ItemAdapter(val context : Context, val items : List<KitchenItemModel>)  : RecyclerView.Adapter<ItemAdapter.ItemVH>() {

    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badge = itemView.badge
        val title= itemView.title
        val subTitle= itemView.subTitle
        val orderTime =  itemView.orderTime
        val elapsedTime=  itemView.elapsedTime
        val constraint=  itemView.constraint
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.kitchen_item_adapter, parent, false)
        return ItemVH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val tvDrawable = TVDrawable(context)

        val item = items.get(position)
        holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.timecolor))

        if (item.reqTime>0L) {
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
                    item.reqTime * item.count,
                    0 //TODO set server diff
                ) <1000) {
                //   holder.elapsedTime.text = "00:00  "
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

        if(item?.count!=null)
            if (item.count>1){
                holder.badge.text = "${item?.count}"
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

        holder.constraint.setOnClickListener {
            showPopup(item,item.id)
        }    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

    private fun countDown(item : KitchenItemModel, holder: ItemVH) {
        if (item.isCountDownStarted!=1) {
            try {
                object : CountDownTimer(
                    DateUtil.remainCookTime(
                        item.date.replace(" ", "").toLong(),
                        item.reqTime * item.count,
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

    abstract fun updateRemainTime(item: KitchenItemModel,milisUntilFinish : Long)
    abstract fun startCountDown(item: KitchenItemModel, countDownTimer: CountDownTimer)
    abstract fun showPopup(item: KitchenItemModel, orderId: Int)
}