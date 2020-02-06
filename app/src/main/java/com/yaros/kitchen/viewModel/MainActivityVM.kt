package com.yaros.kitchen.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.room.db.RoomDb

class MainActivityVM (db: RoomDb, val rxSchedulers: RxSchedulers, apiService: ApiService) : ViewModel() {

    var isClicked: MutableLiveData<Boolean> = MutableLiveData()

    fun isStopListAddButtonClick(){
        isClicked.value =true
        isClicked.value=false
    }

}