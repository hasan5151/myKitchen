package com.yaros.kitchen.repositories

import androidx.lifecycle.LiveData
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.PrintersModel
import io.reactivex.Observable

class PrintersRepo (val db: RoomDb, val rx: RxSchedulers) {

    fun insert(itemModel: PrintersModel?) {
        db.PrintersDAO().insert(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun update(itemModel: PrintersModel?){
        db.PrintersDAO().update(itemModel).compose(rx.applyCompletable()).subscribe()
    }

    fun deleteItem(id: String) {
        db.PrintersDAO().deleteItem(id)
    }

    fun checkPrinter(isChecked : Boolean,id: String) {
        db.PrintersDAO().checkPrinter(isChecked,id).compose(rx.applyCompletable()).subscribe()
    }

    fun getCheckedPrinters() : LiveData<List<PrintersModel>> = db.PrintersDAO().getCheckedPrinters()

    fun getAll(): Observable<PrintersModel> {
        return db.PrintersDAO().getAll().compose(rx.applyObservable())
    }

    fun getAllWithLiveData(): LiveData<List<PrintersModel>> {
        return db.PrintersDAO().getAllWithLiveData()
    }

    fun getPrinters(id: String): String? {
        return db.PrintersDAO().getWaiter(id).compose(rx.applyObservable()).map { it.name }.blockingFirst()
    }

    fun isPrintersCreated(): LiveData<Boolean> {
        return db.PrintersDAO().isWaitersCreated()
    }

}