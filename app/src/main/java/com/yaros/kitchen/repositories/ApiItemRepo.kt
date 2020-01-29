package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.dao.ApiItemDAO
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.ApiItemModel
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

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

    fun deleteItemByOrderId(id: String) {
        db.ApiItemDAO().deleteItemByOrderId(id).compose(rx.applyCompletable()).subscribe()
    }

    fun getKitchenItemModel(orderId: String): LiveData<PagedList<KitchenItemModel>> =
        LivePagedListBuilder<Int, KitchenItemModel>(
            db.ApiItemDAO().getKitchenItemModel(orderId), 10
        ).build()

    fun getOrdelModel(printerId: String): LiveData<PagedList<KitchenOrderModel>> =
        LivePagedListBuilder<Int, KitchenOrderModel>(
            db.ApiItemDAO().getOrdelModel(printerId), 10
        ).build()

    fun updateItemTime(date: String, id : Int){
        db.ApiItemDAO().updateItemTime(date,id)
    }
}