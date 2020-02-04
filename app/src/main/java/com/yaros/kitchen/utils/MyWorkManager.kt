package com.yaros.kitchen.utils

import android.content.Context
import android.widget.Toast
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.apiModels.DishCookedModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.internal.http2.Header
import retrofit2.adapter.rxjava2.Result.response
import retrofit2.http.Headers
import java.util.*


@JvmSuppressWildcards
class MyWorkManager(val appContext: Context, val workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {
    @JvmSuppressWildcards
    override fun startWork(): ListenableFuture<Result> {
        val orderId = workerParams.inputData.getString("orderId")
        val dishId = workerParams.inputData.getString("dishId")
        val printer= workerParams.inputData.getString("printer")
        val cooking_date = workerParams.inputData.getLong("cooking_date",-1)
        val cooking_time = workerParams.inputData.getLong("cooking_time",-1)
        val ticket: String = UUID.randomUUID().toString()

        val api = Api(appContext)
        val itemReadyModel = DishCookedModel(
            ticket,
            orderId,
            dishId,
            cooking_date,
            cooking_time,
            printer
        )

        val gson = Gson()
        System.out.println("work manager model ${gson.toJson(itemReadyModel,DishCookedModel::class.java)}")

        val list = listOf(itemReadyModel)
        return CallbackToFutureAdapter.getFuture {callback ->
            CompositeDisposable().add(api.getApi().sendDishCooked(list).compose(RxSchedulers.DEFAULT.applySingle()).map { it.meta }.subscribe ({meta->
                if (meta.status.contentEquals("SUCCESS"))
                    callback.set(Result.success())
                else
                    callback.set(Result.retry())
            },{it.printStackTrace()}))
        }
    }
}