package com.yaros.kitchen.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.models.KitchenTop
import com.yaros.kitchen.room.entity.PrintersModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface PrintersDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: PrintersModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: PrintersModel?) : Completable

    @Query("DELETE FROM  PrintersModel WHERE id= :id")
    fun deleteItem(id: String) : Completable

    @Query("SELECT * FROM PrintersModel")
    fun getAll(): Observable<PrintersModel>

    @Query("SELECT * FROM PrintersModel")
    fun getAllList(): List<PrintersModel>

    @Query("SELECT * FROM PrintersModel")
    fun getAllWithLiveData(): LiveData<List<PrintersModel>>

    @Query("SELECT * FROM PrintersModel Where id=:id")
    fun getWaiter(id: String): Observable<PrintersModel>

    @Query("SELECT * FROM PrintersModel Where id=:id")
    fun getPrinerById(id: String): PrintersModel

    @Query("Select COUNT(id) AS item FROM PrintersModel")
    fun isWaitersCreated(): LiveData<Boolean>

    @Query("UPDATE PrintersModel  SET isChecked=:isChecked  Where id=:id")
    fun checkPrinter(isChecked: Boolean, id: String ): Completable

    @Query("SELECT * FROM PrintersModel WHERE isChecked=:isChecked")
    fun getCheckedPrinters(isChecked: Boolean=true) : LiveData<List<PrintersModel>>

    @Query("DELETE FROM  PrintersModel")
    fun deleteAll() : Completable


}