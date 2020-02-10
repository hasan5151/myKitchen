package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.HistoryOrderAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HistoryItemModel
import com.yaros.kitchen.models.HistoryOrderModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.viewModel.factory.HistoryFactory
import com.yaros.kitchen.viewModel.HistoryVM
import com.yaros.kitchen.viewModel.PaginationVM
import com.yaros.kitchen.viewModel.factory.PaginationFactory
import io.reactivex.disposables.CompositeDisposable

class HistoryDishCooked : BaseFragment() {
    lateinit var kitchenRecyclerView: RecyclerView
    override fun getName(): String = "Готовые"
    override fun getDrawable(): Int = R.drawable.readyorder
    lateinit var historyVM: HistoryVM
    lateinit var paginationVM: PaginationVM

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

        val paginationFactory =
            PaginationFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        paginationVM = ViewModelProvider(this,paginationFactory).get(PaginationVM::class.java)
        isDataInitialize()
    }

    private fun isDataInitialize() { //if init then get orders
        paginationVM.checkDishes()
        paginationVM.checkWaiters()
        paginationVM.checkPrinters()
        paginationVM.isDishesCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {dish->
            paginationVM.isWaitersCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {waiters->
                paginationVM.isPrintersCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {printers->
                    if (dish&&waiters&&printers){
                          OrdersKitchenPostModel(null,null,null).let { fetchHistory(it) } //test
                    }else{
                        //show loading bar
                    }
                })
            })
        })
    }

    fun fetchHistory(post : OrdersKitchenPostModel){
        val repos = Repos(RoomDb(context!!), RxSchedulers.DEFAULT)
        CompositeDisposable().add(historyVM.fetchHistory(post)?.subscribe({
            var historyOrderModel  : List<HistoryOrderModel>  = listOf()
            it?.groupBy { it?.order }?.forEach {order->
                 var historyItemModel : List<HistoryItemModel> = listOf()
                order.value.forEach {item->
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

            kitchenRecyclerView.adapter=HistoryOrderAdapter(historyOrderModel,context!!)
        },{it.printStackTrace()})!!)
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