package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.KitchenOrderModel

@Dao
interface KitchenOrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localAccount: KitchenOrderModel?)

    @Query("DELETE From  KitchenOrderModel WHERE id= :id")
    fun deleteItem(id: Int)

    @Query("SELECT * FROM KitchenOrderModel ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, KitchenOrderModel>

    @Query("Delete from KitchenOrderModel")
    fun deleteAll()
}