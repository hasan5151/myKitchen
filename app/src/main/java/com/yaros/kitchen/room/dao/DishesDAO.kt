package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.DishesModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DishesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: DishesModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: DishesModel?) : Completable

    @Query("DELETE FROM  DishesModel WHERE id= :id")
    fun deleteItem(id: Int) : Completable

    @Query("SELECT * FROM DishesModel ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, DishesModel>

    @Query("SELECT * FROM DishesModel ORDER BY id DESC")
    fun getItemById(): Single<DishesModel>
}