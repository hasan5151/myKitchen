package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface KitchenDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: KitchenModel?) : Completable

    /*@Query("DELETE FROM  KitchenModel WHERE id= :id")
    fun deleteItem(id: Int) : Completable*/

    @Query("UPDATE KitchenModel SET isSent=1 WHERE id= :id")
    fun deleteItem(id: Int) : Completable

    @Query("SELECT * FROM KitchenModel WHERE isSent=1 ORDER BY number DESC")
    fun getAll(): DataSource.Factory<Int, KitchenModel>

    @Query("SELECT * FROM KitchenModel Where printerId=:printer and isSent=1 ORDER BY number DESC")
    fun getAllByPrinter(printer: String): DataSource.Factory<Int, KitchenModel>

    @Query("UPDATE KitchenModel SET date=:orderTime WHERE id= :itemID")
    fun updateItemTime(orderTime : String, itemID: Int) : Completable

    @Query("UPDATE KitchenModel SET count=count-:count WHERE order_item=:orderItem and dish=:dish and count>:count")
    fun changeAmount(orderItem: String ,dish: String, count: Int) : Completable

    @Query("Select COUNT(order_item) AS item FROM KitchenModel WHERE order_item= :orderItem and dish=:dish and dateOriginal=:date and count=:count")
    fun check(orderItem: String, dish : String, date : String, count : Int ): Boolean


    @Query("UPDATE KitchenModel SET countDownStatus=:status WHERE id= :id")
    fun changeCountDownStatus(id: Int,status : String) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=:cancelledOrders WHERE id= :id")
    fun setCancelledOrders(id: Int,cancelledOrders : Int) : Completable
}