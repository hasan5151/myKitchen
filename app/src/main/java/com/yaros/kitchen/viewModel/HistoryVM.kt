package com.yaros.kitchen.viewModel

import androidx.lifecycle.ViewModel
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.apiModels.HistoryModel
import com.yaros.kitchen.models.apiModels.OrdersKitchenPostModel
import com.yaros.kitchen.repositories.ApiRepo
import com.yaros.kitchen.repositories.Repos
import com.yaros.kitchen.room.db.RoomDb
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class HistoryVM(db: RoomDb, val rxSchedulers: RxSchedulers,var apiService: ApiService) : ViewModel() {
    val repos = Repos(db,rxSchedulers)
    val compositeDisposable = CompositeDisposable()
    val apiRepo = ApiRepo(repos,rxSchedulers,apiService,compositeDisposable)

    fun setApiService(api: Api){
        apiService = api.getApi()
    }

    fun fetchHistory(post : OrdersKitchenPostModel) : Observable<List<HistoryModel?>?>? = apiRepo.getHistory(post)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}