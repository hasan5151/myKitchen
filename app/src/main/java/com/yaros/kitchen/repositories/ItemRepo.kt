package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
 import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel

class ItemRepo(val db: RoomDb) {
    fun getAll() : LiveData<PagedList<KitchenItemModel>> =
        LivePagedListBuilder<Int, KitchenItemModel>(
            db.KitchenItemDAO().getAll(), 10
        ).build()

    fun insert(item : KitchenItemModel){
        db.KitchenItemDAO().insert(item)
    }

    fun delete(id: Int){
        db.KitchenItemDAO().deleteItem(id)
    }

    fun deleteAll(){
        db.KitchenItemDAO().deleteAll()
    }
}