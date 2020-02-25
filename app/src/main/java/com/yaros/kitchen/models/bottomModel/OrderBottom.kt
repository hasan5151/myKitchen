package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.*

class OrderBottom : BottomInterface {
    override fun getItems(): List<BaseFragment> = listOf(
        OrderFragment(), HistoryDishCooked(),
        SentOrdersFragment()
    )
}