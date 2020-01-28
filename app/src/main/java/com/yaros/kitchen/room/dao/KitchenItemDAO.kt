package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.*
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface KitchenItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: KitchenItemModel?) : Completable

    @Query("DELETE FROM  KitchenItemModel WHERE id= :id")
    fun deleteItem(id: Int) : Completable

    @Query("SELECT * FROM KitchenItemModel ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, KitchenItemModel>

    @Query("SELECT * FROM KitchenItemModel WHERE number= :orderId ORDER BY id DESC")
    fun getItemByOrder(orderId: Int): DataSource.Factory<Int, KitchenItemModel>

    @Query("DELETE FROM KitchenItemModel")
    fun deleteAll() : Completable

    @Query("UPDATE KitchenItemModel SET date=:orderTime WHERE id= :itemID")
    fun updateItemTime(orderTime : String, itemID: Int) : Completable

    @Query("UPDATE KitchenItemModel SET isCountDownStarted=:start WHERE id= :itemID")
    fun startCountDown(itemID: Int,start: Boolean=true) : Completable

    @Query("UPDATE KitchenItemModel SET reqTime=:reqTime WHERE id= :itemID")
    fun updateElapsedTime(reqTime: String, itemID: Int) : Completable

    @Query("SELECT KitchenItemModel.id as id, KitchenItemModel.date as orderTime , KitchenItemModel.count as badge, KitchenItemModel.reqTime as reqTime, KitchenItemModel.comment as subTitle, KitchenItemModel.name as title , KitchenOrderModel.waiterName as waiterName, KitchenOrderModel.id as orderId FROM KitchenItemModel INNER JOIN KitchenOrderModel ON  KitchenOrderModel.id = KitchenItemModel.number WHERE KitchenItemModel.id= :itemID")
    fun getItemInfo(itemID: Int) : Flowable<ItemInfoModel>
}