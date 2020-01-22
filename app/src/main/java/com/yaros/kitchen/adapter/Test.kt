package com.yaros.kitchen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.yaros.kitchen.R

class Test(val string : List<String>, val context : Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = ViewHolder()
        val inflater :LayoutInflater = LayoutInflater.from(context)
        val convertView = inflater.inflate(R.layout.checkbox_layout, null)
        viewHolder.name?.text = string.get(position)
        return  convertView
     }

    override fun getItem(p0: Int): Any  {
        return 1
    }

    override fun getItemId(p0: Int): Long = 0

    override fun getCount(): Int =  string.size


    class ViewHolder {
        var name: TextView? = null
        var checkBox: CheckBox? = null
    }

}