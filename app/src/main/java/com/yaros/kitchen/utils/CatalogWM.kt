package com.yaros.kitchen.utils

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenModel
import io.reactivex.disposables.CompositeDisposable

class CatalogWM  (val appContext: Context, val workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {
    companion object {
        const val ORDERS = 0
        const val PRINTERS = 1
        const val DISHES = 2
        const val WAITERS = 3
        const val SUCCESS = "SUCCESS"
    }

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var repos: Repos
    lateinit var apiService: ApiService
    override fun startWork(): ListenableFuture<Result> {
        val type = workerParams.inputData.getInt("type", -1)
        compositeDisposable = CompositeDisposable()
        apiService = Api(appContext).getApi()
        repos = Repos(RoomDb(appContext), RxSchedulers.DEFAULT)
        when (type) {
            ORDERS -> return fetchOrder()
            PRINTERS -> return fetchPrinters()
            DISHES -> return fetchDishes()
            else -> return fetchWaiters() //waiters
        }
    }

    private fun fetchWaiters(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { callback ->
            val tokenService = TokenService(appContext)
            compositeDisposable.add(
                tokenService.getApi().getWaiterss()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                    {
                        System.out.println("hi url ${it?.raw()?.request()?.url()}")
                        val bodySize= it?.body()?.data?.size

                        if(it?.code()!=200)
                            callback.set(Result.retry())
                        else if (!it?.body()?.meta?.status!!.contentEquals(SUCCESS)) {
                            callback.set(Result.retry())
                        } else if (bodySize==0){
                             val data: Data = Data.Builder()
                                .putInt("type", WAITERS)
                                .build()
                            callback.set(Result.success(data))
                        }
                        else
                            it?.body()?.data?.forEachIndexed {index,item->
                            repos.getWaiterRepo().insert(item)

                                System.out.println("cook type ${item.id}")
                            if  (index==bodySize!!-1){
//                                 Preferences.savePref("lastSyncTime",it.item_date,appContext)
                                val data: Data = Data.Builder()
                                    .putInt("type", WAITERS)
                                    .build()
                                callback.set(Result.success(data))
                            }
                        }
                    },
                    { it.printStackTrace() })!!
            )
        }
    }

    private fun fetchDishes(): ListenableFuture<Result> { //
        return CallbackToFutureAdapter.getFuture { callback ->
            compositeDisposable.add(apiService.getKitchenDataa()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                {
                     val bodySize= it?.body()?.data?.dishes?.size

                    if(it?.code()!=200)
                        callback.set(Result.retry())
                    else if (!it?.body()?.meta?.status!!.contentEquals(SUCCESS)) {
                        callback.set(Result.retry())
                    } else if (bodySize==0){
                        val data: Data = Data.Builder()
                            .putInt("type", DISHES)
                            .build()
                        callback.set(Result.success(data))
                    }
                    else {
                        it?.body()?.data?.dishes?.forEachIndexed {index,it->
                            repos.getDishesRepo().insert(it)

                            if  (index==bodySize!!-1){
                                // Preferences.savePref("lastSyncTime",it.item_date,appContext)
                                val data: Data = Data.Builder()
                                    .putInt("type", DISHES)
                                    .build()
                                callback.set(Result.success(data))
                            }
                        }
                    }
                },
                { it.printStackTrace() })!!)
        }
    }

    private fun fetchPrinters(): ListenableFuture<Result> { //                 repos.getPrintersRepo().insert(it)
        return CallbackToFutureAdapter.getFuture { callback ->
            compositeDisposable.add(apiService.getPrinterss()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                {
                     val bodySize= it?.body()?.data?.printers?.size

                    if(it?.code()!=200)
                        callback.set(Result.retry())
                    else if (!it?.body()?.meta?.status!!.contentEquals(SUCCESS)) {
                        callback.set(Result.retry())
                    } else if (bodySize==0){
                        val data: Data = Data.Builder()
                            .putInt("type", PRINTERS)
                            .build()
                        callback.set(Result.success(data))
                    }
                    else {
                        it?.body()?.data?.printers?.forEachIndexed {index,item->
                            repos.getPrintersRepo().insert(item)

                            if  (index==bodySize!!-1){
                                // Preferences.savePref("lastSyncTime",it.item_date,appContext)
                                val data: Data = Data.Builder()
                                    .putInt("type",PRINTERS)
                                    .build()
                                callback.set(Result.success(data))
                            }
                        }
                    }
                },
                { it.printStackTrace() })!!)
        }
    }

    private fun fetchOrder(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { callback ->
            val date_begin = Preferences.getPref("lastSyncTime", null, appContext)
            val ordersKitchenPostModel = OrdersKitchenPostModel(null, date_begin?.toLong(), null)
            compositeDisposable.add(
                apiService.getOrderItemsNew(ordersKitchenPostModel).compose(RxSchedulers.DEFAULT.applyObservable())
                    .subscribe({base->
                         if(base.code()!=200)
                            callback.set(Result.retry())
                        else{
                            base?.body()?.data?.sortedBy { it.item_date }?.forEachIndexed {index,it ->
                                val bodySize= base?.body()?.data?.size
                                if (!base?.body()?.meta?.status!!.contentEquals(SUCCESS)) {
                                    callback.set(Result.retry())
                                }else if (bodySize==0){
                                    val data: Data = Data.Builder()
                                        .putInt("type",ORDERS)
                                        .build()
                                    callback.set(Result.success(data))
                                }else if  (index==bodySize!!-1){
                                    Preferences.savePref("lastSyncTime",it.item_date,appContext)
                                    val data: Data = Data.Builder()
                                        .putInt("type",ORDERS)
                                        .build()
                                    callback.set(Result.success(data))
                                }
                                if (!repos.getKitchenRepo().check(
                                        it.order!!,
                                        it.dish!!,
                                        it.item_date!!,
                                        it.count!!
                                    )
                                ){
                                    var waiterName: String? = ""
                                    try {
                                        waiterName =
                                            repos.getWaiterRepo().getWaiter(it.waiter!!)
                                    } catch (e: NullPointerException) {
                                        waiterName = ""
                                    }

                                    val dishesModel = repos.getDishesRepo().getItem(it.dish!!)
                                    val printerName = repos.getPrintersRepo().getPrintersById(it.printer!!)

                                    if (it.count>0)
                                        for (i in 0..it.count-1) {
                                            KitchenModel(
                                                it.number,
                                                it.printer,
                                                it.order,
                                                dishesModel.name,
                                                it.comment,
                                                it.dish,
                                                dishesModel.cookingTime,
                                                it.item_date,
                                                it.item_date,
                                                1,
                                                waiterName,
                                                0,
                                                printerName.name,
                                                0,
                                                0,
                                                it.item_order,
                                                it.item_number
                                            ).let {
                                                repos.getKitchenRepo().insert(it)
                                            }
                                        }else
                                        for (i in 0 downTo it.count+1) {
                                            KitchenModel(
                                                it.number,
                                                it.printer,
                                                it.order,
                                                dishesModel.name,
                                                it.comment,
                                                it.dish,
                                                dishesModel.cookingTime,
                                                it.item_date,
                                                it.item_date,
                                                -1,
                                                waiterName,
                                                0,
                                                printerName.name,
                                                0,
                                                0,
                                                it.item_order,
                                                it.item_number
                                            ).let {
                                                repos.getKitchenRepo().insert(it)
                                            }
                                            repos.getKitchenRepo().changeAmount(it.order,it.dish,-1)
                                        }
                                }
                            }
                        }
                    }
                        ,{it.printStackTrace()})
            )
        }
    }

    override fun onStopped() {
        super.onStopped()
        compositeDisposable.clear()
    }
}