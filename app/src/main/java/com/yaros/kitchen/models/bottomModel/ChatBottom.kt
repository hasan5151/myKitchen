package com.yaros.kitchen.models.bottomModel

import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.ChatFragment

class ChatBottom  : BottomInterface{
    override fun getItems(): List<BaseFragment> = listOf(ChatFragment())
}