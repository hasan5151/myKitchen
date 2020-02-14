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

    @Query("SELECT * FROM KitchenModel WHERE isSent=0 and count>=0 ORDER BY number DESC")
    fun getAll(): DataSource.Factory<Int, KitchenModel>

    @Query("SELECT * FROM KitchenModel WHERE countDownStatus=0 ORDER BY number DESC") // NOTSTARTED
    fun getNewOrders(): List<KitchenModel>

    @Query("SELECT * FROM KitchenModel ORDER BY number DESC") // NOTSTARTED
    fun getAll2(): List<KitchenModel>

    @Query("SELECT * FROM KitchenModel Where printerId=:printer and isSent=0 and count>=0 ORDER BY number DESC")
    fun getAllByPrinter(printer: String): DataSource.Factory<Int, KitchenModel>

    @Query("UPDATE KitchenModel SET date=:orderTime WHERE id= :itemID")
    fun updateItemTime(orderTime : String, itemID: Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=cancelledOrders-:count WHERE id IN (SELECT id FROM KitchenModel WHERE order_item=:orderItem and dish=:dish and count-cancelledOrders>=-:count LIMIT 1)")
//    @Query("UPDATE KitchenModel SET count=count-:count WHERE order_item=:orderItem and dish=:dish and count>:count ")
    fun changeAmount(orderItem: String ,dish: String, count: Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=cancelledOrders-:count WHERE id=:id")
    fun changeAmountById(id : Int, count: Int) : Completable

    @Query("SELECT * FROM KitchenModel WHERE order_item=:orderItem and dish=:dish and count-cancelledOrders>=-:count") //because :count is minus value
    fun selectItemDecrease(orderItem: String ,dish: String, count: Int) : KitchenModel

    @Query("Select COUNT(order_item) AS item FROM KitchenModel WHERE order_item= :orderItem and dish=:dish and dateOriginal=:date and count=:count")
    fun check(orderItem: String, dish : String, date : String, count : Int ): Boolean

    @Query("Select COUNT(order_item) AS item FROM KitchenModel WHERE order_item= :orderItem and dish=:dish and dateOriginal=:date and count=:count")
    fun check2(orderItem: String, dish : String, date : String, count : Int ): Int

    @Query("UPDATE KitchenModel SET countDownStatus=:status WHERE id= :id")
    fun changeCountDownStatus(id: Int,status : Int) : Completable

    @Query("UPDATE KitchenModel SET cancelledOrders=:cancelledOrders WHERE id= :id")
    fun setCancelledOrders(id: Int,cancelledOrders : Int) : Completable

    @Query("SELECT * FROM KitchenModel Where cancelledOrders>0 ORDER BY number DESC")
    fun getCancelledOrders() : List<KitchenModel>

    @Query("SELECT * FROM KitchenModel Where cancelledOrders<0 ORDER BY number DESC")
    fun getCancelledOrders2() : List<KitchenModel>

}