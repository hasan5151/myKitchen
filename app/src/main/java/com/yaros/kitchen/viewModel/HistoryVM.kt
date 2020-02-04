package com.yaros.kitchen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.models.HistoryItemModel
import com.yaros.kitchen.models.HistoryOrderModel
import com.yaros.kitchen.models.apiModels.HistoryModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.utils.DateUtil
import io.reactivex.Observable

class HistoryVM(db: RoomDb, val rxSchedulers: RxSchedulers, apiService: ApiService) : ViewModel() {
    val repos = Repos(db,rxSchedulers)
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService)

    fun fetchHistory(post : OrdersKitchenPostModel) : Observable<List<HistoryModel?>?>? = apiRepo.getHistory(post)

/*
    fun fetchHistory(post : OrdersKitchenPostModel){
        apiRepo.getHistory(post)?.subscribe({
            val historyOrderModel  : List<HistoryItemModel>  = listOf()
            it?.groupBy { it?.order }?.forEach {order->
                var historyItemModel : List<HistoryItemModel> = listOf()
                order.value.forEach {item->
                    val printer = repos.getPrintersRepo().getPrinters(item!!.printer)
                    val dish = repos.getDishesRepo().getItem(item!!.dish)
                    val cookedTime = DateUtil.getHourandMinute(item?.cooking_date)
                    val startTime = DateUtil.getHourandMinute(item?.cooking_date-(item?.cooking_time*1000))
                    HistoryItemModel(dish.name,cookedTime,startTime).let {
                        historyItemModel.plus(it)
                    }

                }
                HistoryOrderModel(1,"",historyItemModel).let {
                    historyOrderModel.plus(it)
                }
            }


        },{it.printStackTrace()})
    }
*/


}