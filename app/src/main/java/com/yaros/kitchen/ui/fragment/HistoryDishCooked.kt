package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.HistoryOrderAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HistoryItemModel
import com.yaros.kitchen.models.HistoryOrderModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.viewModel.HistoryFactory
import com.yaros.kitchen.viewModel.HistoryVM
import com.yaros.kitchen.viewModel.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM

class HistoryDishCooked : BaseFragment() {
    lateinit var kitchenRecyclerView: RecyclerView
    override fun getName(): String = "Готовые"
    override fun getDrawable(): Int = R.drawable.readyorder
    lateinit var historyVM: HistoryVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kitchenRecyclerView = view.findViewById(R.id.kitchen)

        val historyFactory = HistoryFactory(
            RoomDb(context!!), RxSchedulers.DEFAULT,
            Api(context!!).getApi()
        )
        historyVM = ViewModelProvider(this,historyFactory).get(HistoryVM::class.java)

        OrdersKitchenPostModel(listOf("00000000-0000-0000-0000-000000000000"),1577836800000,1579867821000).let { fetchHistory(it) }
    }

    fun fetchHistory(post : OrdersKitchenPostModel){
        val historyOrderAdapter : HistoryOrderAdapter
        val repos = Repos(RoomDb(context!!), RxSchedulers.DEFAULT)
        historyVM.fetchHistory(post)?.subscribe({
             val gson = Gson()
            var historyOrderModel  : List<HistoryOrderModel>  = listOf()
            it?.groupBy { it?.order }?.forEach {order->
                 var historyItemModel : List<HistoryItemModel> = listOf()
                order.value.forEach {item->
                   //val printer = repos.getPrintersRepo().getPrinters(item!!.printer)
                    val dish = repos.getDishesRepo().getItem(item!!.dish)
                    val cookedTime = DateUtil.getHourandMinute(item?.cooking_date)
                    val startTime = DateUtil.getHourandMinute(item!!.cooking_date - (item!!.cooking_time*1000))
                    HistoryItemModel(dish.name,cookedTime,startTime,checkCookingTime(dish.cookingTime,item.cooking_time)).let {
                         historyItemModel = historyItemModel+listOf(it)
                    }
                }
                HistoryOrderModel(1,"",historyItemModel).let {
                     historyOrderModel = historyOrderModel + listOf(it)
                }
            }

            val historyOrderAdapter = HistoryOrderAdapter(historyOrderModel,context!!)
            kitchenRecyclerView.adapter=historyOrderAdapter


        },{it.printStackTrace()})
    }

    fun checkCookingTime(dishCookTime : Long, itemCookTime : Long) : Boolean{
        if (dishCookTime>0){
            if (dishCookTime>=itemCookTime ){
                return true


            } else return false
        }else
            return true
    }
}