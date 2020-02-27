package com.yaros.kitchen.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.models.KitchenTop
 import com.yaros.kitchen.room.entity.KitchenModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface KitchenDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: KitchenModel?) : Completable

    @Query("UPDATE KitchenModel SET isSent=1 WHERE id= :id")
    fun deleteItem(id: Int) : Completable
 
    @Query("SELECT * FROM KitchenModel WHERE isSent=0 and count>=0 ORDER BY number DESC")
    fun getAll(): DataSource.Factory<Int, KitchenModel>

    @Query("SELECT * FROM KitchenModel WHERE countDownStatus=0 ORDER BY number DESC") // NOTSTARTED
    fun getNewOrders(): List<KitchenModel>
 
    @Query("SELECT * FROM KitchenModel Where printerId=:printer and isSent=0 and count>=0 ORDER BY number DESC")
    fun getAllByPrinter(printer: String): DataSource.Factory<Int, KitchenModel>

    @Query("UPDATE KitchenModel SET date=:orderTime WHERE id= :itemID")
    fun updateItemTime(orderTime : String, itemID: Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=cancelledOrders-:count WHERE id IN (SELECT id FROM KitchenModel WHERE order_item=:orderItem and dish=:dish and count-cancelledOrders>=-:count LIMIT 1)")
    fun changeAmount(orderItem: String ,dish: String, count: Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=cancelledOrders-:count WHERE id=:id")
    fun changeAmountById(id : Int, count: Int) : Completable

    @Query("SELECT * FROM KitchenModel WHERE order_item=:orderItem and dish=:dish and count-cancelledOrders>=-:count") //because :count is minus value
    fun selectItemDecrease(orderItem: String ,dish: String, count: Int) : KitchenModel

    @Query("Select COUNT(order_item) AS item FROM KitchenModel WHERE order_item= :orderItem and dish=:dish and dateOriginal=:date and count=:count")
    fun check(orderItem: String, dish : String, date : String, count : Int ): Boolean
 
    @Query("UPDATE KitchenModel SET countDownStatus=:status WHERE id= :id")
    fun changeCountDownStatus(id: Int,status : Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=:cancelledOrders WHERE id= :id")
    fun setCancelledOrders(id: Int,cancelledOrders : Int) : Completable

    @Query("SELECT * FROM KitchenModel Where cancelledOrders>0 ORDER BY number DESC")
    fun getCancelledOrders() : List<KitchenModel>

    @Query("SELECT * FROM KitchenModel Where cancelledOrders<0 ORDER BY number DESC")
    fun getCancelledOrders2() : List<KitchenModel>

    @Query("Select * From KitchenModel Where printerId=:pos and isSent=0 and count>=0 Group By dateOriginal Order By item_order DESC")
    fun getAllGroupByPrinter(pos: String?): DataSource.Factory<Int, KitchenTop>

    @Query("Select * From KitchenModel  WHERE isSent=0 and count>=0 Group By item_order Order By dateOriginal DESC")
    fun getAllGroupBy() : DataSource.Factory<Int, KitchenTop>

    @Query("Select * From KitchenModel  WHERE isSent=1 and count>=0 Group By item_order Order By dateOriginal DESC")
    fun stopCountDown() : Flowable<KitchenModel>

    @Query("Select * From KitchenModel Where item_order=:item_order and isSent=0 and count>=0")
    fun getItemOrders(item_order : String?) : LiveData<List<KitchenModel>>

    @Query("Select * From KitchenModel Where item_order=:item_order and isSent=0 and count>=0")
    fun getItemOrders2(item_order : String?) : DataSource.Factory<Int, KitchenModel>

    @Query("DELETE FROM  KitchenModel")
    fun deleteAll() : Completable


}
