package com.yaros.kitchen.viewModel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.models.PrintersModel
import com.yaros.kitchen.models.WaitersModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.*
import com.yaros.kitchen.utils.Preferences
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PaginationVM(db: RoomDb,val rxSchedulers: RxSchedulers,apiService: ApiService) : ViewModel() {
    var disposable = CompositeDisposable()
    var item:  LiveData<PagedList<KitchenItemModel>> = MutableLiveData()
    var order: LiveData<PagedList<KitchenOrderModel>> = MutableLiveData()
    var itemInfo: LiveData<ItemInfoModel> = MutableLiveData()
    var dishesInfo: LiveData<DishesModel> = MutableLiveData()

    var waitersList: MutableLiveData<List<WaitersModel>> = MutableLiveData()
    var printersList: MutableLiveData<List<PrintersModel>> = MutableLiveData()
    var hash: MutableLiveData<HashModel> = MutableLiveData()
    var orderHash: MutableLiveData<String> = MutableLiveData()

    val repos = Repos(db,rxSchedulers)
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService)

    fun loadOrders() {
        order= repos.getOrderRepo().getAll()
    }

    fun loadItems() {
        item= repos.getItemRepo().getAll()
    }

    fun deleteOrderById(orderId : String){
        repos.getOrderRepo().deleteOrderById(orderId)
    }

    fun loadItemsByOrderId(orderId : String){
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

   /* fun deleteItemById(itemId : Int){
        repos.getItemRepo().delete(itemId)
    }*/

    fun  deleteAllItems(){
        repos.getItemRepo().deleteAll()
    }

    fun  updateItemTime(string : String, itemId: Int){
        repos.getItemRepo().updateItemTime(string,itemId)
//        repos.getApiItemRepo().updateItemTime(string,itemId)
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

    //------------------------------------------------------------------

    fun insertNewItem(itemModel: ApiItemModel){
        repos.getApiItemRepo().insert(itemModel)
    }

    fun updateItem(itemModel: ApiItemModel){
        repos.getApiItemRepo().update(itemModel)
    }

    fun deleteItemById(id: Int){
        repos.getItemRepo().delete(id)
    }

    fun deleteItemByOrderId(id: String){
        repos.getApiItemRepo().deleteItemByOrderId(id)
    }

    fun getKitchenItemModel(orderId: String){
        item = repos.getApiItemRepo().getKitchenItemModel(orderId)
    }

    fun getKitchenOrderModel(printerId: String){
        order= repos.getApiItemRepo().getOrdelModel(printerId)
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

    fun  getDishesById(id: String){
        dishesInfo = LiveDataReactiveStreams.fromPublisher(repos.getDishesRepo().getItemById(id))
    }
    fun  getAllDishes() =
        repos.getDishesRepo().getAllList()


    //----------------------------------------------------------------------

    fun getWaiters(){//TODO filter with id
        disposable.add(apiRepo.getWaiters()?.subscribe({
            waitersList.value =it
        },{it.printStackTrace()})!!)
    }

    fun logoutWaiters(token : String){
        apiRepo.logoutWaiter(token)
    }

    fun getPrinters(){
        System.out.println("printer test0 ")

        apiRepo.getPrinters()?.subscribe({
            System.out.println("printer test0.1 ${it}")

            printersList.value = it
        },{it.printStackTrace()})
    }
    fun getDishes(){
        apiRepo.getDishes()
    }

    fun getOrderItems(printerList: List<String>,date_begin: Long?,data_end: Long?){
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

    fun login(waiterId: String,pass: String,androidID :String){
        apiRepo.login(waiterId,pass,androidID)
    }



}
