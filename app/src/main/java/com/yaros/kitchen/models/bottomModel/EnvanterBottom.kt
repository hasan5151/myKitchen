package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.EnvanterFragment

class EnvanterBottom : BottomInterface{
    override fun getItems(): List<BaseFragment> = listOf(EnvanterFragment())

}