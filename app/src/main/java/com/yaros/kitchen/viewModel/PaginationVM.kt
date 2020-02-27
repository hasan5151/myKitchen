package com.yaros.kitchen.viewModel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.*
import io.reactivex.disposables.CompositeDisposable

class PaginationVM(db: RoomDb,val rxSchedulers: RxSchedulers,var apiService: ApiService) : ViewModel() {
    var disposable = CompositeDisposable()
     var itemKitchen:  LiveData<PagedList<KitchenModel>> = MutableLiveData()
    var itemTopKitchen:  LiveData<PagedList<KitchenTop>> = MutableLiveData()
    var itemSubKitchen:  LiveData<List<KitchenModel>> = MutableLiveData()
    var itemSubKitchen2:  LiveData<PagedList<KitchenModel>> = MutableLiveData()
    var stopCountDown:  LiveData<KitchenModel> = MutableLiveData()
      lateinit var dishesInfo:  LiveData<List<DishesModel>>

    var printersList: LiveData<List<PrintersModel>> = MutableLiveData()
    var printerChipList: LiveData<List<PrintersModel>> = MutableLiveData()


    val repos = Repos(db,rxSchedulers)
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService,disposable)

    fun setApiService(api: Api){
        apiService = api.getApi()
    }

    fun  getAllDishes() {
     dishesInfo = repos.getDishesRepo().getAll()
    }

    fun getPrinters(){
        printersList = repos.getPrintersRepo().getAllWithLiveData()
    }


    fun checkPrinter(isChecked : Boolean, id : String){
        repos.getPrintersRepo().checkPrinter(isChecked,id)
    }

    fun getCheckedPrinters(){
        printerChipList = repos.getPrintersRepo().getCheckedPrinters()
    }

    //-----------------------------------------------------

    fun deleteItemKitchen(id: Int){
        repos.getKitchenRepo().delete(id)

    }

    fun getAll(){
        itemKitchen = repos.getKitchenRepo().getAll()
    }


    fun updateItemTimeKitchen(orderTime: String, itemId : Int){
          repos.getKitchenRepo().updateItemTime(orderTime,itemId)
    }


    fun getAllGroupBy(){
        itemTopKitchen = repos.getKitchenRepo().getAllGroupBy()
    }

    fun getAllGroupByPrinter(pos : String?){
        itemTopKitchen = repos.getKitchenRepo().getAllGroupByPrinter(pos)
    }

    fun getItemOrders(item_order : String?) {
        itemSubKitchen = repos.getKitchenRepo().getItemOrders(item_order)
    }

    fun getItemOrders2(item_order : String?) {
        itemSubKitchen2 = repos.getKitchenRepo().getItemOrders2(item_order)
    }
    fun stopCountDown() {
        stopCountDown = repos.getKitchenRepo().stopCountDown()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
