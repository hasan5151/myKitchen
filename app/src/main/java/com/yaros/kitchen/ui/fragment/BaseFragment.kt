package com.yaros.kitchen.ui.fragment

import androidx.fragment.app.Fragment

abstract class BaseFragment  : Fragment(){
    abstract fun getName(): String
    abstract fun getDrawable(): Int
}