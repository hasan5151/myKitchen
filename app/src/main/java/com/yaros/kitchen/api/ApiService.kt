package com.yaros.kitchen.api

import android.media.session.MediaSession
import com.yaros.kitchen.BuildConfig.API_URL
import com.yaros.kitchen.models.*
import com.yaros.kitchen.room.entity.ApiItemModel
import com.yaros.kitchen.room.entity.DishesModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET("Services/hs/MobileKitchen?cmd=get_waiters")
    fun getWaiters(): Observable<BaseList<WaitersModel>?>?

    @GET("Services/hs/MobileKitchen?cmd=login_waiter")
    fun loginWaiter(
         @Query("waiter_id") waiterId: String,
         @Query("password") password: String,
         @Query("device_name") deviceName: String?,
         @Query("imei") imei: String?,
         @Query("version") version: String?
     ): Single<Base<AuthToken>?>?

    @GET( "Services/hs/MobileKitchen?cmd=logout_waiter")
    fun logoutWaiter(@Query("waiter_token") waiterToken: String): Single<Base<AuthToken>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_printers")
    fun getPrinters(): Observable<BaseList<PrintersModel>?>?

    @GET("/MobileKitchen?cmd=get_kitchen_data")
    fun getKitchenData(): Observable<BaseList<DishesModel>?>?

    @POST("Services/hs/MobileKitchen?cmd=get_order_items")
    fun getOrderItems(@Body printerList: List<String>, date_begin : Long?,data_end : Long? ): Observable<BaseList<ApiItemModel>?>?

    @GET( "Services/hs/MobileKitchen?cmd=get_kitchen_hashes")
//    @GET("/MobileKitchen?cmd=get_kitchen_hashes&waiter_token=d25b8e59-55d8-41ac-b658-e223a62f2991")
    fun getHashes(): Flowable<Base<HashModel>?>?

    @GET( "Services/hs/MobileKitchen?cmd=get_kitchen_hashes")
//    @GET( "/MobileKitchen?cmd=get_kitchen_hashes&waiter_token=d25b8e59-55d8-41ac-b658-e223a62f2991")
    fun getHashes2(): Observable<Response<Base<HashModel>>>


    @GET( "Services/hs/MobileKitchen?cmd=get_kitchen_hashes")
//    @GET( "/MobileKitchen?cmd=get_kitchen_hashes&waiter_token=d25b8e59-55d8-41ac-b658-e223a62f2991")
    fun getHashes3(): Observable<Base<HashModel>>


}