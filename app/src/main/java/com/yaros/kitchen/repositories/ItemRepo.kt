package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import io.reactivex.Flowable


class ItemRepo(val db: RoomDb, val rx : RxSchedulers) {
    fun getAll() : LiveData<PagedList<KitchenItemModel>> =
        LivePagedListBuilder<Int, KitchenItemModel>(
            db.KitchenItemDAO().getAll(), 10
        ).build()

    fun insert(item : KitchenItemModel){
        db.KitchenItemDAO().insert(item).compose(rx.applyCompletable()).subscribe()
    }

    fun delete(id: Int){
        db.KitchenItemDAO().deleteItem(id).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteAll(){
        db.KitchenItemDAO().deleteAll().compose(rx.applyCompletable()).subscribe()
    }

    fun updateItemTime(string : String, itemId : Int){
        db.KitchenItemDAO().updateItemTime(string,itemId).compose(rx.applyCompletable()).subscribe()
    }

    fun startCountDown(itemId : Int){
        db.KitchenItemDAO().startCountDown(itemId).compose(rx.applyCompletable()).subscribe()
    }

    fun updateElapsedTime(string : String, itemId : Int){
        db.KitchenItemDAO().updateElapsedTime(string,itemId).compose(rx.applyCompletable()).subscribe()
    }

    fun getItemInfo(itemId : Int) : Flowable<ItemInfoModel>{
        return db.KitchenItemDAO().getItemInfo(itemId).compose(rx.applyFlowable())
    }

    fun getItemByOrder(orderId : Int) : LiveData<PagedList<KitchenItemModel>> =
        LivePagedListBuilder<Int, KitchenItemModel>(
            db.KitchenItemDAO().getItemByOrder(orderId), 10
        ).build()
}