package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.DishesModel
import com.yaros.kitchen.room.entity.WaitersModel
import io.reactivex.Flowable
import io.reactivex.Observable

class WaiterRepo (val db: RoomDb, val rx: RxSchedulers) {

    fun insert(itemModel: WaitersModel?) {
        db.WaiterDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun update(itemModel: WaitersModel?){
        db.WaiterDAO().update(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteItem(id: String) {
        db.WaiterDAO().deleteItem(id)
    }

    fun getAll(): Observable<WaitersModel> {
        return db.WaiterDAO().getAll().compose(rx.applyObservable())
    }

    fun getWaiter(id: String): String? {
        return db.WaiterDAO().getWaiter(id).compose(rx.applyObservable()).map { it.name }.blockingFirst()
    }

    fun isWaitersCreated(): LiveData<Boolean> {
        return db.WaiterDAO().isWaitersCreated()
    }

}