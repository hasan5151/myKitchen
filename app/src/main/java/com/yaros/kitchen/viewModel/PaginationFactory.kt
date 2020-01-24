package com.yaros.kitchen.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSingleSchedulers
import com.yaros.kitchen.room.db.RoomDb

class PaginationFactory (val db: RoomDb, val rxSingleSchedulers: RxSingleSchedulers, val apiService: ApiService) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PaginationVM(db,rxSingleSchedulers,apiService) as T
    }
}