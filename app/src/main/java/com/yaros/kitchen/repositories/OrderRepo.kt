package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
 import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenOrderModel

class OrderRepo(val db: RoomDb) {

    fun getAll() : LiveData<PagedList<KitchenOrderModel>> =
        LivePagedListBuilder<Int, KitchenOrderModel>(
            db.KitchenOrderDAO().getAll(), 10
        ).build()

    fun insert(item : KitchenOrderModel){
        db.KitchenOrderDAO().insert(item)
    }

    fun delete(id: Int){
        db.KitchenOrderDAO().deleteItem(id)
    }

    fun deleteAll(){
        db.KitchenOrderDAO().deleteAll()
    }
}