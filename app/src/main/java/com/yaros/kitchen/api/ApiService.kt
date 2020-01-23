package com.yaros.kitchen.api

import com.yaros.kitchen.BuildConfig.API_URL
import com.yaros.kitchen.models.Base
import com.yaros.kitchen.models.BaseList
import com.yaros.kitchen.models.Token
import com.yaros.kitchen.models.WaitersModel
import io.reactivex.Single
import retrofit2.http.*


interface ApiService {
    @GET(API_URL+ "/MobileKitchen?cmd=login_waiter")
    fun getToken(
         @Query("waiter_id") waiterId: String,
         @Query("password") password: String,
         @Query("device_name") deviceName: String?,
         @Query("imei") imei: String?,
         @Query("version") version: String?
     ): Single<Base<Token>?>?


    @GET(API_URL+ "/MobileKitchen?cmd=get_waiters")
    fun getWaiters(): Single<BaseList<WaitersModel>?>?

    @GET(API_URL+ "/MobileKitchen?cmd=logout_waiter")
    fun revokeToken(@Query("waiter_token") waiterToken: String): Single<BaseList<Token>?>?

}