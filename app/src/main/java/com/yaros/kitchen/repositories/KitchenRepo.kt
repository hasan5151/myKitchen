package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenModel

class KitchenRepo (val db: RoomDb, val rx : RxSchedulers) {
    fun insert(itemModel: KitchenModel?) {
        db.KitchenDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun delete(id: Int) {
        db.KitchenDAO().deleteItem(id).compose(rx.applyCompletable()).subscribe()
    }

    fun getAllGroupBy() : LiveData<PagedList<KitchenTop>> =
        LivePagedListBuilder<Int, KitchenTop>(
            db.KitchenDAO().getAllGroupBy(), 10
        ).build()


    fun getAllGroupByPrinter(pos : String?) : LiveData<PagedList<KitchenTop>> =
        LivePagedListBuilder<Int, KitchenTop>(
            db.KitchenDAO().getAllGroupByPrinter(pos), 10
        ).build()


    fun getItemOrders2(pos : String?) : LiveData<PagedList<KitchenModel>> =
        LivePagedListBuilder<Int, KitchenModel>(
            db.KitchenDAO().getItemOrders2(pos), 10
        ).build()

    fun getItemOrders(item_order : String?) : LiveData<List<KitchenModel>> =
            db.KitchenDAO().getItemOrders(item_order)

    fun stopCountDown() : LiveData<KitchenModel> =
            LiveDataReactiveStreams.fromPublisher(db.KitchenDAO().stopCountDown())

    fun getAll() : LiveData<PagedList<KitchenModel>> =
        LivePagedListBuilder<Int, KitchenModel>(
            db.KitchenDAO().getAll(), 10
        ).build()

    fun updateItemTime(orderTime: String, itemID: Int) {
        db.KitchenDAO().updateItemTime(orderTime,itemID).compose(rx.applyCompletable()).subscribe()
    }

    fun changeAmount(orderItem: String, dish: String, count: Int) {
        db.KitchenDAO().changeAmount(orderItem,dish,count).compose(rx.applyCompletable()).subscribe()
    }

    fun changeAmountById(id : Int, count: Int) {
        db.KitchenDAO().changeAmountById(id,count).compose(rx.applyCompletable()).subscribe()
    }

    fun selectItemDecrease(orderItem: String, dish: String, count: Int) : KitchenModel = db.KitchenDAO().selectItemDecrease(orderItem,dish,count)

    fun check(orderItem: String, dish: String,date : String, count: Int) : Boolean = db.KitchenDAO().check(orderItem,dish,date,count)


    fun changeCountDownStatus(id: Int,status : Int) {
        db.KitchenDAO().changeCountDownStatus(id,status).compose(rx.applyCompletable()).subscribe()
    }

    fun setCancelledOrders(id: Int,cancelledOrders : Int) {
        db.KitchenDAO().setCancelledOrders(id,cancelledOrders).compose(rx.applyCompletable()).subscribe()
    }

    fun getNewOrders() : List<KitchenModel> = db.KitchenDAO().getNewOrders()
}