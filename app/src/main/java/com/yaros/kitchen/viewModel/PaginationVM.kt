package com.yaros.kitchen.viewModel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class PaginationVM(db: RoomDb,val rxSchedulers: RxSchedulers,apiService: ApiService) : ViewModel() {
    var disposable = CompositeDisposable()
    var item:  LiveData<PagedList<KitchenItemModel>> = MutableLiveData()
    var order: LiveData<PagedList<KitchenOrderModel>> = MutableLiveData()
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
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService)

    fun loadOrders() {
        order= repos.getOrderRepo().getAll()
    }

    fun loadOrdersByPrinter(printerId: String) {
        order= repos.getOrderRepo().getOrderByPrinterId(printerId)
    }

    fun deleteOrderById(orderId : String){
        repos.getOrderRepo().deleteOrderById(orderId)
    }

    fun loadItemsByOrderId(orderId : String){
        item = repos.getItemRepo().getItemByOrder(orderId)
    }

    fun  updateItemTime(string : String, itemId: Int){
        repos.getItemRepo().updateItemTime(string,itemId)
//        repos.getApiItemRepo().updateItemTime(string,itemId)
    }
    //------------------------------------------------------------------

    fun deleteItemById(id: Int){
        repos.getItemRepo().delete(id)
    }

    //--------------------------------------------------------------------

    fun insertNewDishes(dish: DishesModel){
        repos.getDishesRepo().insert(dish)
    }
    fun updateDish(dish: DishesModel){
        repos.getDishesRepo().update(dish)
    }

    fun deleteDishes(id: String){
        repos.getDishesRepo().deleteItem(id)
    }

 /*   fun  getDishesById(id: String){
        dishesInfo = LiveDataReactiveStreams.fromPublisher(repos.getDishesRepo().getItemById(id))
    }*/
    fun  getAllDishes() {
     dishesInfo = repos.getDishesRepo().getAll()
    }




    //----------------------------------------------------------------------

    fun checkWaiters() {
        isWaitersCreated= repos.getWaiterRepo().isWaitersCreated()
    }
    fun checkDishes() {
        isDishesCreated= repos.getDishesRepo().isDishesCreated()
    }
    fun checkPrinters() {
        isPrintersCreated= repos.getPrintersRepo().isPrintersCreated()
    }

    fun fetchWaiters(){
        apiRepo.getWaiters()
    }

    fun logoutWaiters(token : String){
        apiRepo.logoutWaiter(token)
    }

    fun fetchPrinters(){
        apiRepo.getPrinters()
    }
    fun fetchDishes(){
        apiRepo.getDishes()
    }

    fun getOrderItems(printerList: List<String>,date_begin: Long?=null,data_end: Long?=null){
        apiRepo.getOrderItems(printerList,date_begin, data_end)
    }

    fun getHashes(){
        disposable.add(Observable.interval(0, 5, TimeUnit.SECONDS)
            .compose(rxSchedulers.applyObservable())
            .subscribe {
                apiRepo.getHashes()?.subscribe({
                    hash.value= it
                    orderHash.value= it.orders_hash
                },{it.printStackTrace()})
            })
    }

    fun getPrinters(){
        printersList = repos.getPrintersRepo().getAllWithLiveData()
    }

    fun login(waiterId: String,pass: String,androidID :String){
        apiRepo.login(waiterId,pass,androidID)
    }

    fun checkPrinter(isChecked : Boolean, id : String){
        repos.getPrintersRepo().checkPrinter(isChecked,id)
    }

    fun getCheckedPrinters(){
        printerChipList = repos.getPrintersRepo().getCheckedPrinters()
    }



}
