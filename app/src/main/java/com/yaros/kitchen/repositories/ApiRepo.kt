package com.yaros.kitchen.repositories

import android.os.Build
import android.widget.Toast
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.models.*
import com.yaros.kitchen.models.apiModels.HistoryModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.room.entity.KitchenModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.lang.NullPointerException

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService,val compositeDisposable: CompositeDisposable) {


    fun getWaiters(){
        val tokenService = TokenService()
        compositeDisposable.add(
            tokenService.getApi().getWaiters()?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.subscribe({
                System.out.println("init it ${it}")
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
        System.out.println("it sizeXY asdasdansdm,ansd")
        val ordersKitchenPostModel = OrdersKitchenPostModel(printerList,date_begin,data_end)
        compositeDisposable.add(apiService.getOrderItems(ordersKitchenPostModel).compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it->it }?.subscribe(
            { api ->
                api.orders.forEach {order->
                    order.dishes.forEach {item->

                        if (!repos.getKitchenRepo().check(order.order,item.dish,item.item_date,item.count)){
                            System.out.println("it sizeXY ${item.item_date}")
                            System.out.println("it sizeXY asdasdansdm,ansd")


                            var waiterName : String? =  ""
                            try {
                                waiterName=  repos.getWaiterRepo().getWaiter(order.waiter)
                            }catch (e: NullPointerException){
                                waiterName=""
                            }

                            val dishesModel = repos.getDishesRepo().getItem(item.dish)
                            val printerName= repos.getPrintersRepo().getPrintersById(api.printer)
                            if (item.count>0){
                                KitchenModel(order.number,api.printer,order.order,dishesModel.name,item.comment,item.dish,dishesModel.cookingTime,item.item_date,item.item_date,item.count,waiterName,0,printerName.name,0,0).let {
                                    repos.getKitchenRepo().insert(it)
                                }
                            }else{ //TODO ask this part
                                System.out.println("it sizeXX")


                   /*             repos.getKitchenRepo().selectItemDecrease(order.order,item.dish,item.count)?.let {
                                    System.out.println("it sizeX ${it.id}")
                                    System.out.println("it sizeX1 ${item.count}")
                                    repos.getKitchenRepo().changeAmountById(it?.id,item.count)
                                }*/

//                                repos.getKitchenRepo().changeAmount(order.order,item.dish,item.count)
                            }
                        }
                    }
                }
            }
            ,{
                it.printStackTrace()})!!) //TODO filter if count -1
    }

    /*    fun getOrderItems(
        printerList: List<String>?,
        date_begin: Long?,
        data_end: Long?
    ){
        val ordersKitchenPostModel = OrdersKitchenPostModel(printerList,date_begin,data_end)
        compositeDisposable.add(apiService.getOrderItems(ordersKitchenPostModel).compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it->it }?.subscribe(
            { api ->
                api.orders.forEach {order->
                    if (!repos.getOrderRepo().checkOrder(order.order)!!) {

                        var waiterName : String? =  ""
                        try {
                            waiterName=  repos.getWaiterRepo().getWaiter(order.waiter)
                        }catch (e: NullPointerException){
                            waiterName=""
                        }
                        KitchenOrderModel(
                            order.order,
                            order.number,
                            api.printer,
                            waiterName
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
        ,{
                 it.printStackTrace()})!!) //TODO filter if count -1
    }*/

    fun getHashes() : Observable<HashModel>? = apiService.getHashes()?.compose(rxSchedulers.applyObservable())?.map { it.data }

    fun getHistory(post : OrdersKitchenPostModel) : Observable<List<HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }
/*
    fun getHistory(post : OrdersKitchenPostModel) : Observable<GroupedObservable<String?,HistoryModel?>?>? =
        apiService.getHistory(post)?.compose(rxSchedulers.applyObservable())?.map { it.data }?.flatMapIterable { it }?.groupBy { it.order }
*/
}