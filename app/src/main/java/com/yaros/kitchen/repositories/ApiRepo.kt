package com.yaros.kitchen.repositories

import android.os.Build
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.*
import com.yaros.kitchen.room.entity.*
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import com.yaros.kitchen.utils.Preferences
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService) {
    val compositeDisposable = CompositeDisposable()
    fun fetchItems(){
         fetchDishes()

        getTestItems().filter { it.count>-1 }.forEach {
            val api = it
            System.out.println("selam teset2 ${it.order_items}")
            //insert order
            KitchenOrderModel(it.order_items,it.number,"").let { repos.getOrderRepo().insert(it) }

            val dishesModel = repos.getDishesRepo().getItem(it.dish)
            KitchenItemModel(api.number,api.order_items,dishesModel.name,api.comment,dishesModel.cookingTime,api.date,api.count,0).let {
                System.out.println("selam teset ${it.name}  ${api.order_items}")
                repos.getItemRepo().insert(it)
            }

        }
    }

    fun getTestItems(): List<ApiItemModel>{
        val api1 = ApiItemModel(1,"1","1","001",3,"test",System.currentTimeMillis().toString(),"1")
        val api2 = ApiItemModel(2,"1","2","001",2,"test",System.currentTimeMillis().toString(),"1")
        val api3 = ApiItemModel(3,"2","2","002",1,null,System.currentTimeMillis().toString(),"1")
        val api4= ApiItemModel(4,"2","1","002",5,null,System.currentTimeMillis().toString(),"1")
        return listOf(api1,api2,api3,api4)
    }

    fun fetchOrders(){ }

    fun fetchDishes(){
        DishesModel("1","Fish",12).let {
            repos.getDishesRepo().insert(it)
        }
        DishesModel("2","Cola",10).let {
            repos.getDishesRepo().insert(it)
        }
     }

    //--------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------
    fun getWaiters() :Observable<List<WaitersModel>>? {
        return apiService.getWaiters()?.compose(rxSchedulers.applyObservable())?.map { it.data.waiters }
    }

    fun logoutWaiter(waiterToken: String){
        apiService.logoutWaiter(waiterToken)?.compose(rxSchedulers.applySingle())?.subscribe()
    }

    fun login(waiterId: String,pass: String,androidID :String)  : Single<AuthToken>?{
        return apiService.loginWaiter(waiterId,pass, Build.MODEL,androidID,BuildConfig.VERSION_NAME)?.compose(rxSchedulers.applySingle())?.map { it.data }
    }

    fun getPrinters(): Observable<List<PrintersModel>>?
            = apiService.getPrinters()?.compose(rxSchedulers.applyObservable())?.map { it.data?.printers }

    fun getDishes(){
        compositeDisposable.add(apiService.getKitchenData()?.compose(rxSchedulers.applyObservable())?.map { it.data.dishes }?.flatMapIterable { it->it }?.subscribe(
            {
                repos.getDishesRepo().insert(it)
            }
        ,{it.printStackTrace()})!!)
    }

    fun getOrderItems(
        printerList: List<String>,
        date_begin: Long?,
        data_end: Long?
    ){
        compositeDisposable.add(apiService.getOrderItems(printerList,date_begin,data_end)?.compose(rxSchedulers.applyObservable())?.map { it.data.orders }?.flatMapIterable { it->it }?.subscribe(
            {
                val api = it
                System.out.println("selam teset2 ${it.order_items}")
                //insert order
                KitchenOrderModel(it.order_items,it.number,"").let { repos.getOrderRepo().insert(it) }

                val dishesModel = repos.getDishesRepo().getItem(it.dish)
                KitchenItemModel(api.number,api.order_items,dishesModel.name,api.comment,dishesModel.cookingTime,api.date,api.count,0).let {
                    System.out.println("selam teset ${it.name}  ${api.order_items}")
                    repos.getItemRepo().insert(it)
                }
            }
        ,{it.printStackTrace()})!!) //TODO filter if count -1

    }

    fun getHashes() : Observable<HashModel>? = apiService.getHashes()?.compose(rxSchedulers.applyObservable())?.map { it.data }

}