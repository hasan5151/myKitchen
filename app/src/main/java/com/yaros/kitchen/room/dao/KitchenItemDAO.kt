package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.KitchenItemModel

@Dao
interface KitchenItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localAccount: KitchenItemModel?)

    @Query("DELETE From  KitchenItemModel WHERE id= :id")
    fun deleteItem(id: Int)

    @Query("SELECT * FROM KitchenItemModel ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, KitchenItemModel>

    @Query("Delete from KitchenItemModel")
    fun deleteAll()
}