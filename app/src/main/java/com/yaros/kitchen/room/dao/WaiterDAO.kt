package com.yaros.kitchen.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.WaitersModel
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface WaiterDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: WaitersModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: WaitersModel?) : Completable

    @Query("DELETE FROM  WaitersModel WHERE id= :id")
    fun deleteItem(id: String) : Completable

    @Query("SELECT * FROM WaitersModel")
    fun getAll(): Observable<WaitersModel>

    @Query("SELECT * FROM WaitersModel Where id=:id")
    fun getWaiter(id: String): WaitersModel

    @Query("Select COUNT(id) AS item FROM WaitersModel")
    fun isWaitersCreated(): LiveData<Boolean>

    @Query("DELETE FROM  WaitersModel")
    fun deleteAll() : Completable

    @Query("Select * From WaitersModel Limit 1")
    fun getWaiter() : WaitersModel



}