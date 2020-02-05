package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.DishesModel
import io.reactivex.Flowable
import io.reactivex.Single

class DishesRepo (val db: RoomDb, val rx: RxSchedulers) {
    fun insert(itemModel: DishesModel?) {
        db.DishesDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun update(itemModel: DishesModel?){
        db.DishesDAO().update(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteItem(id: String) {
        db.DishesDAO().deleteItem(id).compose(rx.applyCompletable()).subscribe()
    }

    fun getItemById(id: String): Flowable<DishesModel> {
        return db.DishesDAO().getItemById(id).compose(rx.applyFlowable())
    }

    fun getItem(id: String): DishesModel {
        return db.DishesDAO().getItem(id)
    }
    fun getAll(): LiveData<List<DishesModel>> {
        return db.DishesDAO().getAll()
    }
    fun getAllList(): List<DishesModel> {
        return db.DishesDAO().getAllList()
    }

    fun isDishesCreated(): LiveData<Boolean> {
        return db.DishesDAO().isDishesCreated()
    }
}