package com.yaros.kitchen.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class CountDownVM : ViewModel() {
    lateinit var items: HashMap<Int, MutableLiveData<Boolean>>

    fun putLiveData(id : Int){
        items.put(id, MutableLiveData())
    }

    fun updateLiveData(id : Int, boolean: Boolean){
        items.set(id, MutableLiveData(boolean))
    }

    fun removeLiveData(id : Int){
        items.remove(id)
    }
}