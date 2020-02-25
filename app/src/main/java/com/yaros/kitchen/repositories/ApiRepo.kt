package com.yaros.kitchen.repositories

import android.os.Build
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.models.*
import com.yaros.kitchen.models.apiModels.HistoryModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService,val compositeDisposable: CompositeDisposable) {

    fun getWaiters(){
  /*      val tokenService = TokenService()
        compositeDisposable.add(
            tokenService.getApi().getWaiters()?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.subscribe({
                System.out.println("init it ${it}")
                repos.getWaiterRepo().insert(it)

            },{it.printStackTrace()})!!)*/
    }

    fun logoutWaiter(waiterToken: String){
        apiService.logoutWaiter(waiterToken)?.compose(rxSchedulers.applySingle())?.subscribe()
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

    fun getHashes() : Observable<HashModel>? = apiService.getHashes()?.compose(rxSchedulers.applyObservable())?.map { it.data }

    fun getHistory(post : OrdersKitchenPostModel) : Observable<List<HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }
/*
    fun getHistory(post : OrdersKitchenPostModel) : Observable<GroupedObservable<String?,HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.groupBy { it.order }
*/
}