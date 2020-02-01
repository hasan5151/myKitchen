package com.yaros.kitchen.api

import com.yaros.kitchen.models.*
import io.reactivex.Observable
import io.reactivex.Single
 import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET("Services/hs/MobileKitchen?cmd=get_waiters")
    fun getWaiters(): Observable<BaseList<BaseType>?>?

    @GET("Services/hs/MobileKitchen?cmd=login_waiter")
    fun loginWaiter(
         @Query("waiter_id") waiterId: String,
         @Query("password") password: String,
         @Query("device_name") deviceName: String?,
         @Query("imei") imei: String?,
         @Query("version") version: String?
     ): Single<Base<AuthToken>?>?

    @GET("Services/hs/MobileKitchen?cmd=login_waiter")
    fun login(
         @Query("waiter_id") waiterId: String,
         @Query("password") password: String,
         @Query("app_id") appId: String?,
         @Query("version") version: String?
     ): Observable<Base<AuthToken>?>?
//                        .url(API_URL+"hs/MobileKitchen?cmd=login_waiter&password=123&waiter_id=fa907412-a069-11e7-aa0e-002522ec5b96&app_id=mobile_kitchen&version=9")

    @GET("Services/hs/MobileKitchen?cmd=login_waiter")
    fun refreshToken(
        @Query("waiter_id") waiterId: String,
        @Query("password") password: String,
        @Query("device_name") deviceName: String?,
        @Query("imei") imei: String?,
        @Query("version") version: String?
    ): Call<Base<AuthToken>>


    @GET( "Services/hs/MobileKitchen?cmd=logout_waiter")
    fun logoutWaiter(@Query("waiter_token") waiterToken: String): Single<Base<AuthToken>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_printers")
    fun getPrinters(): Observable<BaseList<BaseType>?>?

    @GET("/MobileKitchen?cmd=get_kitchen_data")
    fun getKitchenData(): Observable<BaseList<BaseType>?>?

    @POST("Services/hs/MobileKitchen?cmd=get_order_items")
    fun getOrderItems(@Body printerList: List<String>, date_begin : Long?,data_end : Long? ): Observable<BaseList<BaseType>?>?

    @GET( "Services/hs/MobileKitchen?cmd=get_kitchen_hashes")
    fun getHashes(): Observable<Base<HashModel>?>?
}