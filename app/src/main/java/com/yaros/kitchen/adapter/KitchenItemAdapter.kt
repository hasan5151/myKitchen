package com.yaros.kitchen.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.KitchenItemModel
import com.yaros.kitchen.models.OrderModel
import com.yaros.kitchen.utils.DialogUtil
import kotlinx.android.synthetic.main.kitchen_item_adapter.view.*
import java.util.*


abstract class KitchenItemAdapter (val items: ArrayList<KitchenItemModel>?, val order: OrderModel, val context: Context): RecyclerView.Adapter<KitchenItemAdapter.KitchenItemVH>() {
    val countDownHash: HashMap<Int, CountDownTimer> = HashMap()

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
        val constraint=  itemView.constraint
    }

    override fun onBindViewHolder(holder: KitchenItemVH, position: Int) {
        val item = items!!.get(position)
        holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.timecolor))
        holder.orderTime.text= "${item.orderTime}    |    "

        holder.elapsedTime.text= item.reqTime

        if(item.badge!=null)
        if (item.badge>1){
            holder.badge.text = "${item.badge}"
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

        holder.title.text= item.title!!

        if (!isNullOrEmpty(item.subTitle)){
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

        holder.constraint.setOnClickListener {
            System.out.println("click me !!!!")
            val dialog  = DialogUtil.bottomConstraint(R.layout.meal_ready_popup,context)
            val title: TextView = dialog!!.findViewById(R.id.title)
            val description: TextView = dialog.findViewById(R.id.description)
            val badge: TextView = dialog.findViewById(R.id.badge)
            val cancel: TextView = dialog.findViewById(R.id.cancel)
            val ok: TextView = dialog.findViewById(R.id.ok)
            title.text = item.title
            dialog.show()

            //test

            badge.text = "${item.badge}"
            description.text = "№  ${order.id}    ${order.workerName}     ${item.orderTime}    |   ${item.reqTime}"

            cancel.setOnClickListener {
                holder.orderTime.text= context.resources.getString(R.string.cancelled)
                holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.red))
                holder.elapsedTime.text=""
                dialog.dismiss()
                countDown(position)
            }

            ok.setOnClickListener({
                holder.orderTime.text= context.resources.getString(R.string.ready)
                holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.green))
                holder.elapsedTime.text=""
                dialog.dismiss()
                countDown(position)
            })
        }
    }

    private fun removeItem(position: Int) {
        if (items?.size==1){
            items?.removeAt(items.size-1)
            notifyItemRemoved(position)
            notifyDataSetChanged()
             ItemSize(items?.size)
        }else{
            items?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
            ItemSize(items!!.size)
        }
    }/*

    private fun removeItem(position: Int) {
        items?.removeAt(position)
        notifyDataSetChanged()
        ItemSize(items!!.size)
    }
    */

    open fun clear() {
        val size: Int = items!!.size
        if (size > 0) {
            items.subList(0, size).clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    fun isNullOrEmpty(str: CharSequence): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

    fun countDown(position: Int) =
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                removeItem(position)
            }

            override fun onTick(millisUntilFinished: Long) {
                println("%$millisUntilFinished")
            }
        }.start()


    fun countDownMeal(kitchen: KitchenItemModel,time: Long,holder: KitchenItemVH,countDownFinish: CountDownFinish)  =
        object : CountDownTimer(time*5, 1000) {
            override fun onFinish() {
                holder.orderTime.text= context.resources.getString(R.string.ready)
                holder.orderTime.setTextColor(ContextCompat.getColor(context,R.color.green))
                holder.elapsedTime.text=""
                countDownFinish.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                kitchen.reqTime=millisUntilFinished.toString()
                holder.elapsedTime.text="${millisUntilFinished}"
                println("$millisUntilFinished ${kitchen.title}")
            }
        }.start()


    abstract fun ItemSize(itemSize : Int)

    interface CountDownFinish{
        fun onFinish()
    }

}

