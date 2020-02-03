package com.yaros.kitchen.utils

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.apiModels.DishCookedModel
import java.util.*

@JvmSuppressWildcards
class MyWorkManager(val appContext: Context, val workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {
    @JvmSuppressWildcards
    override fun startWork(): ListenableFuture<Result> {
        val orderId = workerParams.inputData.getString("orderID")
        val dishId = workerParams.inputData.getString("dishId")
        val cooking_date = workerParams.inputData.getLong("cooking_date",-1)
        val cooking_time = workerParams.inputData.getLong("cooking_time",-1)
        val ticket: String = UUID.randomUUID().toString()

        val api = Api(appContext)
        val itemReadyModel = DishCookedModel(
            ticket,
            orderId,
            dishId,
            cooking_date,
            cooking_time
        )

        val list = listOf(itemReadyModel)

        return CallbackToFutureAdapter.getFuture {callback ->
            api.getApi().sendDishCooked(list).compose(RxSchedulers.DEFAULT.applyObservable()).subscribe {
                callback.set(Result.success())
                       if (it.meta.status!!.contentEquals("SUCCESS"))
                           callback.set(Result.success())
                       else
                           callback.set(Result.retry())

            }
        }
    }
}