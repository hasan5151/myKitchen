package com.yaros.kitchen.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.DishesModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DishesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: DishesModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: DishesModel?) : Completable

    @Query("DELETE FROM  DishesModel WHERE id= :id")
    fun deleteItem(id: String) : Completable

    @Query("SELECT * FROM DishesModel")
    fun getAll(): LiveData<List<DishesModel>>

    @Query("SELECT * FROM DishesModel")
    fun getAllList(): List<DishesModel>

    @Query("SELECT * FROM DishesModel Where id=:id")
    fun getItemById(id: String): Flowable<DishesModel>

    @Query("SELECT * FROM DishesModel Where id=:id")
    fun getItem(id: String): DishesModel

    @Query("Select COUNT(id) AS item FROM DishesModel")
    fun isDishesCreated(): LiveData<Boolean>
}