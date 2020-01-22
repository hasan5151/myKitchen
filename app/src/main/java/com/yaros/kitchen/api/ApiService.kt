package com.yaros.kitchen.api

import com.yaros.kitchen.BuildConfig.API_URL
import com.yaros.kitchen.models.Base
import com.yaros.kitchen.models.BaseList
import com.yaros.kitchen.models.Token
import com.yaros.kitchen.models.WaitersModel
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @POST(API_URL+ "/MobileKitchen?cmd=login_waiter")
    fun getToken(
         @Query("waiter_id") waiterId: Int,
         @Query("password") password: String,
         @Query("device_name") deviceName: String?,
         @Query("imei") imei: String?,
         @Query("mac") mac: String?,
         @Query("version") version: String?
     ): Single<Base<Token>?>?


    @POST(API_URL+ "/MobileKitchen?cmd=get_waiters ")
    fun getWaiters(): Single<BaseList<WaitersModel>?>?
}