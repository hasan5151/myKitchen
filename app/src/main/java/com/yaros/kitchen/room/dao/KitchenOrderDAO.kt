package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.KitchenOrderModel
import io.reactivex.Completable

@Dao
interface KitchenOrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localAccount: KitchenOrderModel?) : Completable

    @Query("DELETE From  KitchenOrderModel WHERE order_item= :id")
    fun deleteItem(id: Int) : Completable

    @Query("SELECT * FROM KitchenOrderModel ORDER BY order_item DESC")
    fun getAll(): DataSource.Factory<Int, KitchenOrderModel>

    @Query("SELECT * FROM KitchenOrderModel WHERE order_item= :id ORDER BY order_item DESC")
    fun getOrderById(id : Int): DataSource.Factory<Int, KitchenOrderModel>

    @Query("Delete from KitchenOrderModel")
    fun deleteAll() : Completable
}