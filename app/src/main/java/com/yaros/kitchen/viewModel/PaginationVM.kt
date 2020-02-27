package com.yaros.kitchen.viewModel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class PaginationVM(db: RoomDb,val rxSchedulers: RxSchedulers,var apiService: ApiService) : ViewModel() {
    var disposable = CompositeDisposable()
    var itemKitchen:  LiveData<PagedList<KitchenModel>> = MutableLiveData()
    var itemTopKitchen:  LiveData<PagedList<KitchenTop>> = MutableLiveData()
    var itemSubKitchen:  LiveData<List<KitchenModel>> = MutableLiveData()
    var itemSubKitchen2:  LiveData<PagedList<KitchenModel>> = MutableLiveData()
    var stopCountDown:  LiveData<KitchenModel> = MutableLiveData()
    var itemInfo: LiveData<ItemInfoModel> = MutableLiveData()
    lateinit var dishesInfo:  LiveData<List<DishesModel>>

    var printersList: LiveData<List<PrintersModel>> = MutableLiveData()
    var printerChipList: LiveData<List<PrintersModel>> = MutableLiveData()
    var hash: MutableLiveData<HashModel> = MutableLiveData()
    var orderHash: MutableLiveData<String> = MutableLiveData()

    lateinit var isWaitersCreated: LiveData<Boolean>
    lateinit var isDishesCreated: LiveData<Boolean>
    lateinit var isPrintersCreated: LiveData<Boolean>

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

    fun insertKitchen(itemModel: KitchenModel){
        repos.getKitchenRepo().insert(itemModel)

    }
    fun deleteItemKitchen(id: Int){
        repos.getKitchenRepo().delete(id)

    }
    fun getAllByPrinterId(printer : String){
        itemKitchen = repos.getKitchenRepo().getAllByPrinter(printer)
    }
    fun getAll(){
        itemKitchen = repos.getKitchenRepo().getAll()
    }


    fun updateItemTimeKitchen(orderTime: String, itemId : Int){
          repos.getKitchenRepo().updateItemTime(orderTime,itemId)
    }

    fun changeAmount(orderItem: String, dish: String,  count : Int){
          repos.getKitchenRepo().changeAmount(orderItem,dish,count)
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
