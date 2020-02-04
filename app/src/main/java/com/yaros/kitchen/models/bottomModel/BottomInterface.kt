package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.BaseFragment

interface BottomInterface {
    fun getName() : String
    fun getDrawable() : Int
     fun getItems() : List<BaseFragment>

}