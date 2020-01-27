package com.yaros.kitchen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb

class PaginationFactory (val db: RoomDb, val rxSchedulers: RxSchedulers, val apiService: ApiService) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PaginationVM(db,rxSchedulers,apiService) as T
    }
}