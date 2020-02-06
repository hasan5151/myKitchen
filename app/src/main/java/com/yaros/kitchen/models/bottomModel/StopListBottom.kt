package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.R
import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.MenuFragment
import com.yaros.kitchen.ui.fragment.StopListFragment
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*

class StopListBottom : BottomInterface{



    override fun getItems(): List<BaseFragment> =listOf(StopListFragment(), MenuFragment())
}