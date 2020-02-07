package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.HistoryDishCooked
import com.yaros.kitchen.ui.fragment.OrderFragment
import com.yaros.kitchen.ui.fragment.SentOrdersFragment

class OrderBottom : BottomInterface {


    override fun getItems(): List<BaseFragment> = listOf(
        OrderFragment(), HistoryDishCooked(),
        SentOrdersFragment()
    )
}