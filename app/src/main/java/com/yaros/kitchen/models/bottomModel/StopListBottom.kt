package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.MenuFragment
import com.yaros.kitchen.ui.fragment.StopListFragment

class StopListBottom : BottomInterface{
    override fun getItems(): List<BaseFragment> =listOf(StopListFragment(), MenuFragment())
}