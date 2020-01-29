package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.ApiItemModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ApiItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: ApiItemModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: ApiItemModel?) : Completable

    @Query("DELETE FROM  ApiItemModel WHERE id= :id")
    fun deleteItem(id: Int) : Completable

    @Query("DELETE FROM  ApiItemModel WHERE order_items=:id")
    fun deleteItemByOrderId(id: String) : Completable //delete Order

/*
    @Query("SELECT ApiItemModel.id as id, ApiItemModel.number as number, ApiItemModel.order_items as order_items , 'test' as name, ApiItemModel.comment as comment , ApiItemModel.date as date ,ApiItemModel.count as count ,14353 as reqTime, 0 as isCountDownStarted  FROM ApiItemModel  INNER JOIN DishesModel ON  DishesModel.id= ApiItemModel.dish   ")
    fun getKitchenItemModel() :  DataSource.Factory<Int, KitchenItemModel>
*/

    @Query("SELECT ApiItemModel.id as id, ApiItemModel.number as number, ApiItemModel.order_items as order_items , DishesModel.name as name, ApiItemModel.comment as comment , ApiItemModel.date as date ,ApiItemModel.count as count , DishesModel.cookingTime  as reqTime, 0 as isCountDownStarted  FROM ApiItemModel INNER JOIN DishesModel ON  DishesModel.id= ApiItemModel.dish Where ApiItemModel.order_items=:orderItems")
    fun getKitchenItemModel(orderItems: String) :  DataSource.Factory<Int, KitchenItemModel>

    @Query("SELECT ApiItemModel.number as number, ApiItemModel.order_items as order_item , ApiItemModel.number as waiterName FROM ApiItemModel Where printerId=:printerId Group By ApiItemModel.order_items")
    fun getOrdelModel(printerId: String) :   DataSource.Factory<Int, KitchenOrderModel>

    @Query("UPDATE ApiItemModel SET date=:date WHERE   ApiItemModel.id=:id")
    fun updateItemTime(date: String,id : Int)


    /*@Query("UPDATE KitchenItemModel SET reqTime=:reqTime WHERE id= :itemID")
    fun updateElapsedTime(reqTime: String, itemID: Int) : Completable
    */

}