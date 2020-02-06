package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yaros.kitchen.R

class ChatFragment : BaseFragment() {
    override fun getName(): String = "Чат"
    override fun getDrawable(): Int =0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.empty, container, false)
    }
}