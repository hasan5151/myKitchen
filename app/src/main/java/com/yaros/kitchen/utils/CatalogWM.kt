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
            val tokenService = TokenService()
            compositeDisposable.add(
                tokenService.getApi().getWaiters()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                    {
                        if (!it?.meta?.status!!.contentEquals(SUCCESS)) {
                            callback.set(Result.retry())
                        } else {
                            it.data.forEach {
                                repos.getWaiterRepo().insert(it)
                            }
                            callback.set(Result.success())
                        }
                    },
                    { it.printStackTrace() })!!
            )
        }
    }

    private fun fetchDishes(): ListenableFuture<Result> { //
        return CallbackToFutureAdapter.getFuture { callback ->
            compositeDisposable.add(apiService.getKitchenData()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                {
                    if (!it?.meta?.status!!.contentEquals(SUCCESS)) {
                        callback.set(Result.retry())
                    } else {
                        it.data.dishes.forEach {
                            repos.getDishesRepo().insert(it)
                        }
                        callback.set(Result.success())
                    }
                },
                { it.printStackTrace() })!!)
        }
    }

    private fun fetchPrinters(): ListenableFuture<Result> { //                 repos.getPrintersRepo().insert(it)

        return CallbackToFutureAdapter.getFuture { callback ->
            compositeDisposable.add(apiService.getPrinters()?.compose(RxSchedulers.DEFAULT.applyObservable())?.subscribe(
                {
                    if (!it?.meta?.status!!.contentEquals(SUCCESS)) {
                        callback.set(Result.retry())
                    } else {
                        it.data.printers.forEach {
                            repos.getPrintersRepo().insert(it)
                        }
                        callback.set(Result.success())
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
                apiService.getOrderItems(ordersKitchenPostModel).compose(
                    RxSchedulers.DEFAULT.applyObservable()
                )?.subscribe({
                    if (!it?.meta?.status!!.contentEquals(SUCCESS)) {
                        callback.set(Result.retry())
                    } else {
                        it.data.forEach { api ->
                            api.orders.forEach { order ->
                                order.dishes.forEachIndexed { index , item->

                                    if (!repos.getKitchenRepo().check(
                                            order.order,
                                            item.dish,
                                            item.item_date,
                                            item.count
                                        )
                                    ) {
                                        var waiterName: String? = ""
                                        try {
                                            waiterName =
                                                repos.getWaiterRepo().getWaiter(order.waiter)
                                        } catch (e: NullPointerException) {
                                            waiterName = ""
                                        }

                                        val dishesModel = repos.getDishesRepo().getItem(item.dish)
                                        val printerName =
                                            repos.getPrintersRepo().getPrintersById(api.printer)
                                        if (item.count > 0) {
                                            KitchenModel(
                                                order.number,
                                                api.printer,
                                                order.order,
                                                dishesModel.name,
                                                item.comment,
                                                item.dish,
                                                dishesModel.cookingTime,
                                                item.item_date,
                                                item.item_date,
                                                item.count,
                                                waiterName,
                                                0,
                                                printerName.name,
                                                0,
                                                0
                                            ).let {
                                                repos.getKitchenRepo().insert(it)
                                            }
                                        }/*else{ //TODO ask this part
                                             repos.getKitchenRepo().changeAmount(order.order,item.dish,item.count)
                                        }*/
                                    }
                                    val data: Data = Data.Builder()
                                        .putInt("type",ORDERS)
                                        .build()
                                    callback.set(Result.success(data))
                                }
                            }
                        }
                    }
                },{it.printStackTrace()})!!
            )
        }
    }

}