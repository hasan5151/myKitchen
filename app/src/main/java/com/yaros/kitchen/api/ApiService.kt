package com.yaros.kitchen.api

import android.media.session.MediaSession
import com.yaros.kitchen.BuildConfig.API_URL
import com.yaros.kitchen.models.*
 import com.yaros.kitchen.room.entity.DishesModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET(API_URL+ "/MobileKitchen?cmd=get_waiters")
    fun getWaiters(): Single<BaseList<WaitersModel>?>?


    @GET(API_URL+ "/MobileKitchen?cmd=login_waiter")
    fun loginWaiter(
         @Query("waiter_id") waiterId: String,
         @Query("password") password: String,
         @Query("device_name") deviceName: String?,
         @Query("imei") imei: String?,
         @Query("version") version: String?
     ): Single<Base<AuthToken>?>?


    @GET(API_URL+ "/MobileKitchen?cmd=logout_waiter")
    fun logoutWaiter(@Query("waiter_token") waiterToken: String): Single<BaseList<AuthToken>?>?

    @GET(API_URL+ "/MobileKitchen?cmd=get_printers")
    fun getPrinters(): Single<BaseList<PrintersModel>?>?

    @GET(API_URL+ "/MobileKitchen?cmd=get_kitchen_data")
    fun getKitchenData(): Single<BaseList<DishesModel>?>?

    @POST(API_URL+ "/MobileKitchen?cmd=get_order_items")
    fun getOrderItems(@Body printerList: List<String>, date_begin : Long=0,data_end : Long =0 ): Observable<Base<HashModel>?>?

    @GET(API_URL+ "/MobileKitchen?cmd=get_kitchen_hashes")
    fun getHashes(): Observable<Base<HashModel>?>?


}