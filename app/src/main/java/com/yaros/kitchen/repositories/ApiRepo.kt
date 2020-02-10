package com.yaros.kitchen.repositories

import android.os.Build
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.models.*
import com.yaros.kitchen.models.apiModels.HistoryModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService) {
    val compositeDisposable = CompositeDisposable()

    fun getWaiters() {
        val tokenService = TokenService()
        compositeDisposable.add(
            tokenService.getApi().getWaiters()?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.subscribe({
                repos.getWaiterRepo().insert(it)
        },{it.printStackTrace()})!!)
    }

    fun logoutWaiter(waiterToken: String){
        apiService.logoutWaiter(waiterToken)?.compose(rxSchedulers.applySingle())?.subscribe()
    }

    fun login(waiterId: String,pass: String,androidID :String)  : Single<AuthToken>?{
        return apiService.loginWaiter(waiterId,pass, Build.MODEL,androidID,BuildConfig.VERSION_NAME)?.compose(rxSchedulers.applySingle())?.map { it.data }
    }

    fun getPrinters(){
        compositeDisposable.add(apiService.getPrinters()?.compose(rxSchedulers.applyObservable())?.map { it.data?.printers }?.flatMapIterable { it }?.subscribe({
               repos.getPrintersRepo().insert(it)
            },{it.printStackTrace()})!!)
    }

    fun getDishes(){
        compositeDisposable.add(apiService.getKitchenData()?.compose(rxSchedulers.applyObservable())?.map { it.data.dishes }?.flatMapIterable { it->it }?.subscribe(
            {
                repos.getDishesRepo().insert(it)
            }
        ,{it.printStackTrace()})!!)
    }

    fun getOrderItems(
        printerList: List<String>?,
        date_begin: Long?,
        data_end: Long?
    ){
        val ordersKitchenPostModel = OrdersKitchenPostModel(printerList,date_begin,data_end)
        compositeDisposable.add(apiService.getOrderItems(ordersKitchenPostModel).compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it->it }?.subscribe(
            { api ->
                api.orders.forEach {order->
                    if (!repos.getOrderRepo().checkOrder(order.order)!!) {
                        KitchenOrderModel(
                            order.order,
                            order.number,
                            api.printer,
                            repos.getWaiterRepo().getWaiter(order.waiter)!!
                        ).let { repos.getOrderRepo().insert(it) }
                        order.dishes.forEach { item ->
                            val dishesModel = repos.getDishesRepo().getItem(item.dish)
                            KitchenItemModel(
                                order.number,
                                order.order,
                                dishesModel.name,
                                item.comment,
                                dishesModel.cookingTime,
                                item.item_date,
                                item.count,
                                item.dish,
                                0
                            ).let {
                                    repos.getItemRepo().insert(it)
                                }
                        }
                    }
                }
            }
        ,{it.printStackTrace()})!!) //TODO filter if count -1
    }

    fun getHashes() : Observable<HashModel>? = apiService.getHashes()?.compose(rxSchedulers.applyObservable())?.map { it.data }

    fun getHistory(post : OrdersKitchenPostModel) : Observable<List<HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }

/*
    fun getHistory(post : OrdersKitchenPostModel) : Observable<GroupedObservable<String?,HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.groupBy { it.order }
*/

}