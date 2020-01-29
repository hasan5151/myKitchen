package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.DishesModel
import io.reactivex.Flowable

class DishesRepo (val db: RoomDb, val rx: RxSchedulers) {
    fun insert(itemModel: DishesModel?) {
        db.DishesDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun update(itemModel: DishesModel?){
        db.DishesDAO().update(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteItem(id: String) {
        db.DishesDAO().deleteItem(id)
    }

    fun getItemById(id: String): Flowable<DishesModel> {
        return db.DishesDAO().getItemById(id).compose(rx.applyFlowable())
    }
    fun getAll(): Flowable<DishesModel> {
        return db.DishesDAO().getAll().compose(rx.applyFlowable())
    }
    fun getAllList(): List<DishesModel> {
        return db.DishesDAO().getAllList()
    }
}