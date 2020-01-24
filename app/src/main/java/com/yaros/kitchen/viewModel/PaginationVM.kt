package com.yaros.kitchen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSingleSchedulers
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel

class PaginationVM(db: RoomDb, rxSingleSchedulers: RxSingleSchedulers, apiService: ApiService) : ViewModel() {
    var item:  LiveData<PagedList<KitchenItemModel>> = MutableLiveData()
    var order: LiveData<PagedList<KitchenOrderModel>> = MutableLiveData()

    val repos = Repos(db)

    val apiRepo = ApiRepo(repos,rxSingleSchedulers,apiService)

    fun loadOrders() {
        order= repos.getOrderRepo().getAll()
    }

    fun loadItems() {
        item= repos.getItemRepo().getAll()
    }

    fun deleteAllOrder(){
        repos.getOrderRepo().deleteAll()
    }

    fun fetchOrders(){
        apiRepo.fetchOrders()
    }


}