package com.yaros.kitchen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivityVM (db: RoomDb, val rxSchedulers: RxSchedulers, apiService: ApiService) : ViewModel() {

    var isClicked: MutableLiveData<Boolean> = MutableLiveData()
    var isFullScreen: MutableLiveData<Boolean> = MutableLiveData()

    lateinit var isWaitersCreated: LiveData<Boolean>
    lateinit var isDishesCreated: LiveData<Boolean>
    lateinit var isPrintersCreated: LiveData<Boolean>
     var isOrderUpdated: MutableLiveData<Boolean> = MutableLiveData()

    var hash: MutableLiveData<HashModel> = MutableLiveData()

    val repos = Repos(db,rxSchedulers)
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService)

    fun isStopListAddButtonClick(){
        isClicked.value =true
        isClicked.value=false
    }

    fun setIsFullScreen(isFull : Boolean){
        isFullScreen.value = isFull
    }

    fun checkWaiters() {
        isWaitersCreated= repos.getWaiterRepo().isWaitersCreated()
    }
    fun checkDishes() {
        isDishesCreated= repos.getDishesRepo().isDishesCreated()
    }
    fun checkPrinters() {
        isPrintersCreated= repos.getPrintersRepo().isPrintersCreated()
    }

    fun getHashes(){
        CompositeDisposable().add(
            Observable.interval(0, 5, TimeUnit.SECONDS)
                .compose(rxSchedulers.applyObservable())
                .subscribe {
                    apiRepo.getHashes()?.subscribe({
                        hash.value= it
                    },{it.printStackTrace()})
                })
    }

    fun fetchWaiters(){
        apiRepo.getWaiters()
    }


    fun fetchPrinters(){
        apiRepo.getPrinters()
    }
    fun fetchDishes(){
        apiRepo.getDishes()
    }

    fun getOrderItems(printerList: List<String>?,date_begin: Long?=null,data_end: Long?=null){
        apiRepo.getOrderItems(printerList,date_begin, data_end)
    }

    fun setOrderUpdate(){
        isOrderUpdated.value = true
        isOrderUpdated.value= false
    }



}