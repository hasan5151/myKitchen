package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
 import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yaros.kitchen.api.RxSchedulers
 import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.ApiItemModel
 import com.yaros.kitchen.room.entity.KitchenItemModel


class ApiItemRepo (val db: RoomDb, val rx: RxSchedulers) {
    fun insert(itemModel: ApiItemModel?) {
        db.ApiItemDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun update(itemModel: ApiItemModel?) {
        db.ApiItemDAO().update(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteItem(id: Int) {
        db.ApiItemDAO().deleteItem(id).compose(rx.applyCompletable()).subscribe()
    }

}