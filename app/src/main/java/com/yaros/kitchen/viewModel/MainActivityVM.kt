package com.yaros.kitchen.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.HashModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivityVM (val db: RoomDb, val rxSchedulers: RxSchedulers,var apiService: ApiService) : ViewModel() {

    var isClicked: MutableLiveData<Boolean> = MutableLiveData()
    var isFullScreen: MutableLiveData<Boolean> = MutableLiveData()
    var isOrderFetched: MutableLiveData<Boolean> = MutableLiveData()
    var isInstallationComplete: MutableLiveData<Boolean> = MutableLiveData()

    var printerFetched: MutableLiveData<Boolean> = MutableLiveData() //first fetch
    var dishFetched: MutableLiveData<Boolean> = MutableLiveData() //first fetch
    var waitersFetched: MutableLiveData<Boolean> = MutableLiveData() //first fetch

    var disposable = CompositeDisposable()

    lateinit var countDownTimer: CountDownTimer

    var isHistoryUpdated: MutableLiveData<Boolean> = MutableLiveData()


    var hash: MutableLiveData<HashModel> = MutableLiveData()

    var repos = Repos(db,rxSchedulers)
    var apiRepo = ApiRepo(repos,rxSchedulers,apiService,disposable)


    fun isStopListAddButtonClick(){
        isClicked.value =true
        isClicked.value=false
    }

    fun setIsFullScreen(isFull : Boolean){
        isFullScreen.value = isFull
    }

    fun setInstallation(isFetched : Boolean){
        disposable.add(Observable.timer(1,TimeUnit.SECONDS)
            .compose(RxSchedulers.DEFAULT.applyObservable()).subscribe({
            isInstallationComplete.value = isFetched
        }))
    }
    fun setIsPrinterFetch(isFetched : Boolean){
        printerFetched.value = isFetched
    }
    fun setIsDishFetch(isFetched : Boolean){
        dishFetched.value = isFetched
    }
    fun setIsWaiterFetch(isFetched : Boolean){
        waitersFetched.value = isFetched
    }

    fun setIsOrderFetched(){
        isOrderFetched.value = true
        isOrderFetched.value = false
     }

    fun getHashes() {
        System.out.println("selam hashes test")
        if (::countDownTimer.isInitialized)
        countDownTimer.cancel()

        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 5000) {
            override fun onFinish() {}

            override fun onTick(millisUntilFinished: Long) {
                disposable.add(apiRepo.getHashes()?.subscribe({
                    System.out.println("selam Hash ${it}")
                    hash.value=it
                },{it.printStackTrace()})!!)
            }
        }.start()
    }

    fun stopCountTimer(){
        if (::countDownTimer.isInitialized)
        countDownTimer.cancel()
    }


    fun setHistoryUpdate(){
        isHistoryUpdated.value = true
        isHistoryUpdated.value= false
    }

    fun changeCountDownStatus(id: Int,status : Int) {
        repos.getKitchenRepo().changeCountDownStatus(id,status)
    }

    fun getNewOrders() : List<KitchenModel> = repos.getKitchenRepo().getNewOrders()

    override fun onCleared() {
        disposable.clear()
         super.onCleared()
    }
}