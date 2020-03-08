package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
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
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.PaginationVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory
import com.yaros.kitchen.viewModel.factory.PaginationFactory
import io.reactivex.disposables.CompositeDisposable

class HistoryDishCooked : BaseFragment() {
    lateinit var kitchenRecyclerView: RecyclerView
    override fun getName(): String = "Готовые"
    override fun getDrawable(): Int = R.drawable.readyorder
    lateinit var historyVM: HistoryVM
    lateinit var paginationVM: PaginationVM
    lateinit var mainActivityVM: MainActivityVM

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

        val mainActivityFactory =
            MainActivityFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        mainActivityVM =
            ViewModelProvider(activity!!, mainActivityFactory).get(MainActivityVM::class.java)

        isDataInitialize()
    }

    private fun isDataInitialize() { //if init then get orders
        mainActivityVM.isInstallationComplete.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it){
                historyVM.setApiService(Api(context!!))
                OrdersKitchenPostModel(null,null,null).let { fetchHistory(it) } //test
            }
        })

        mainActivityVM.isHistoryUpdated.observe(viewLifecycleOwner, Observer {
            if (it){
                OrdersKitchenPostModel(null,null,null).let { fetchHistory(it) } //test
            }else{
                //show loading bar
            }
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
//                    HistoryItemModel(dish.name,cookedTime,startTime,checkCookingTime(500,item.cooking_time)).let {
                    HistoryItemModel(dish.name,cookedTime,startTime,checkCookingTime(dish.cooking_time,item.cooking_time)).let {
                        historyItemModel = historyItemModel+listOf(it)
                    }
                }
                HistoryOrderModel(1,"",historyItemModel).let {his->
                    historyOrderModel = historyOrderModel + listOf(his)
                    kitchenRecyclerView.adapter=HistoryOrderAdapter(historyOrderModel,context!!)


                }
            }
        },{it.printStackTrace()})!!)
    }

    private fun checkCookingTime(dishCookTime : Long, itemCookTime : Long) : Boolean =
        if (dishCookTime>0){
            dishCookTime>=itemCookTime
        }else
            true

}