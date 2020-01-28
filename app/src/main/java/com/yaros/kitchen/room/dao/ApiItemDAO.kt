package com.yaros.kitchen.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaros.kitchen.room.entity.ApiItemModel
import com.yaros.kitchen.room.entity.DishesModel
import com.yaros.kitchen.room.entity.ItemInfoModel
import com.yaros.kitchen.room.entity.KitchenItemModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ApiItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemModel: ApiItemModel?) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemModel: ApiItemModel?) : Completable

    @Query("DELETE FROM  ApiItemModel WHERE id= :id")
    fun deleteItem(id: Int) : Completable

    @Query("SELECT * FROM ApiItemModel ORDER BY id DESC")
    fun getAll(): DataSource.Factory<Int, ApiItemModel>

    @Query("SELECT * FROM ApiItemModel ORDER BY id DESC")
    fun getItemById(): Single<ApiItemModel>

    @Query("SELECT ApiItemModel.id as id, ApiItemModel.number as number, ApiItemModel.order_items as order_items , DishesModel.name as name, ApiItemModel.comment as comment , ApiItemModel.date as date ,ApiItemModel.count as count , ApiItemModel.date+DishesModel.cookingTime  as reqTime FROM ApiItemModel INNER JOIN DishesModel ON  DishesModel.id= ApiItemModel.dish")
    fun getKitchenItemModel(itemID: Int) :  DataSource.Factory<Int, KitchenItemModel>

    @Query("SELECT ApiItemModel.number as number, ApiItemModel.order_items as order_items , DishesModel.name as name, ApiItemModel.comment as comment , ApiItemModel.date as date ,ApiItemModel.count as count , ApiItemModel.date+DishesModel.cookingTime  as reqTime FROM ApiItemModel INNER JOIN DishesModel ON  DishesModel.id= ApiItemModel.dish")
    fun getOrdelModel(itemID: Int) : Flowable<KitchenItemModel>


}