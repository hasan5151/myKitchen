package com.yaros.kitchen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel

class PaginationVM(db: RoomDb,rxSchedulers: RxSchedulers, apiService: ApiService) : ViewModel() {
    var item:  LiveData<PagedList<KitchenItemModel>> = MutableLiveData()
    var order: LiveData<PagedList<KitchenOrderModel>> = MutableLiveData()
    var itemInfo: LiveData<ItemInfoModel> = MutableLiveData()

    val repos = Repos(db,rxSchedulers)

    val apiRepo = ApiRepo(repos,rxSchedulers,apiService)

    fun loadOrders() {
        order= repos.getOrderRepo().getAll()
    }

    fun loadItems() {
        item= repos.getItemRepo().getAll()
    }

    fun deleteOrderById(orderId : Int){
        repos.getOrderRepo().deleteOrderById(orderId)
    }

    fun loadItemsByOrderId(orderId : Int){
        item = repos.getItemRepo().getItemByOrder(orderId)
    }

    fun deleteAllOrders(){
        repos.getOrderRepo().deleteAll()
    }

    fun fetchOrders(){ // from api
        apiRepo.fetchOrders()
    }

    fun fetchItems() { // from api
        apiRepo.fetchItems()
    }

    fun deleteItemById(itemId : Int){
        repos.getItemRepo().delete(itemId)
    }

    fun  deleteAllItems(){
        repos.getItemRepo().deleteAll()
    }

    fun  updateItemTime(string : String, itemId: Int){
        repos.getItemRepo().updateItemTime(string,itemId)
    }

    fun  startCountDown(itemId: Int){
        repos.getItemRepo().startCountDown(itemId)
    }

    fun  updateElapsedTime(string : String, itemId: Int){
        repos.getItemRepo().updateElapsedTime(string,itemId)
    }

    fun  getItemInfo(itemId: Int){
        itemInfo= LiveDataReactiveStreams.fromPublisher(repos.getItemRepo().getItemInfo(itemId))
    }

}