package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenOrderModel

class OrderRepo(val db: RoomDb,val rx: RxSchedulers) {

    fun getAll() : LiveData<PagedList<KitchenOrderModel>> =
        LivePagedListBuilder<Int, KitchenOrderModel>(
            db.KitchenOrderDAO().getAll(), 10
        ).build()

    fun insert(item : KitchenOrderModel){
        db.KitchenOrderDAO().insert(item).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteOrderById(id: Int){
        db.KitchenOrderDAO().deleteItem(id).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteAll(){
        db.KitchenOrderDAO().deleteAll().compose(rx.applyCompletable()).subscribe()
    }

    fun getOrderById(orderId : Int) : LiveData<PagedList<KitchenOrderModel>> =
        LivePagedListBuilder<Int, KitchenOrderModel>(
            db.KitchenOrderDAO().getOrderById(orderId), 10
        ).build()
}