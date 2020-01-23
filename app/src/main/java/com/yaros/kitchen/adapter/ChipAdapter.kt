package com.yaros.kitchen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.models.CheckBoxModel
import kotlinx.android.synthetic.main.chip_adapter.view.*

abstract class ChipAdapter (val string : List<CheckBoxModel>) : RecyclerView.Adapter<ChipAdapter.MyChipAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChipAdapter {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.chip_adapter, parent, false)
        return MyChipAdapter(view)
    }

    override fun onBindViewHolder(holder: MyChipAdapter, position: Int) {
        System.out.println("size of string ${string}")
        if (string.size==1){
            holder.button.text = "Выберите кухню"
            holder.button.setOnClickListener( {showDialog()  })
        } else {

            holder.button.setOnClickListener({
                if (!string.get(position).name!!.contentEquals("add"))
                    clickListener(string.get(position).name,position)
                else
                    showDialog()
            })
        }

        if (string.get(position).name!!.contentEquals("add"))
            holder.button.text = "+"
        else
            holder.button.text = string.get(position).name
    }

    override fun getItemCount(): Int = string.size


    class MyChipAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button = itemView.button
    }
    abstract fun clickListener(chip: String?,pos : Int)
    abstract fun showDialog()

}


