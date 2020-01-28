package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.PrintersModel
import kotlinx.android.synthetic.main.checkbox_layout.view.*

abstract class CheckBoxAdapter (val printersModel:  List<PrintersModel>, val context : Context) : RecyclerView.Adapter<CheckBoxAdapter.CheckBoxVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxVH {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.checkbox_layout, parent, false)
        return CheckBoxVH(view)
    }

    override fun getItemCount(): Int = printersModel.size


    override fun onBindViewHolder(holder: CheckBoxVH, position: Int) {
        holder.checkBox.isChecked = printersModel.get(position).isChecked

/*        val states =
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        val colors = intArrayOf(R.color.popupcolor, R.color.colorPrimary)
        CompoundButtonCompat.setButtonTintList(holder.checkBox, ColorStateList(states, colors))*/

        if (holder.checkBox.isChecked){
            holder.name.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
    //        holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
        }
        else{

            holder.name.setTextColor(ContextCompat.getColor(context,R.color.popupcolor))
      //      holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.popupcolor))
        }


        holder.name.setOnClickListener({
            holder.checkBox.performClick()
        })

        holder.name.text = printersModel.get(position).name
        holder.checkBox.setOnCheckedChangeListener({ _compoundButton, b ->
            printersModel.get(position).isChecked = b
            getSelectItems(b,printersModel.get(position))
            if (holder.checkBox.isChecked){

                holder.name.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
        //        holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
            }
            else{

                holder.name.setTextColor(ContextCompat.getColor(context,R.color.popupcolor))
        //        holder.checkBox.setTextColor(ContextCompat.getColor(context,R.color.popupcolor))
            }
        })
    }

    class CheckBoxVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox= itemView.checkBox
        val name = itemView.name
    }

    abstract fun getSelectItems(isChecked : Boolean, printersModel: PrintersModel)


}