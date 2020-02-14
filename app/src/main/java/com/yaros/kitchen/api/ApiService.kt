package com.yaros.kitchen.api

import com.yaros.kitchen.models.*
import com.yaros.kitchen.models.apiModels.*
import com.yaros.kitchen.room.entity.WaitersModel
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@JvmSuppressWildcards
interface ApiService {
    @GET("Services/hs/MobileKitchen?cmd=get_waiters")
    fun getWaiters(): Observable<BaseList<WaitersModel>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_waiters")
    fun getWaiterss(): Observable<Response<BaseList<WaitersModel>?>?>?

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
    fun getPrinters(): Observable<Base<BaseType>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_printers")
    fun getPrinterss(): Observable<Response<Base<BaseType>?>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_kitchen_data")
    fun getKitchenData(): Observable<Base<BaseType>?>?

    @GET("Services/hs/MobileKitchen?cmd=get_kitchen_data")
    fun getKitchenDataa(): Observable<Response<Base<BaseType>?>?>?

    @POST("Services/hs/MobileKitchen?cmd=get_orders_kitchen")
    fun getOrderItems(@Body ordersKitchenPostModel: OrdersKitchenPostModel): Observable<BaseList<GetOrderItems>>

    @POST("Services/hs/MobileKitchen?cmd=get_orders_kitchen")
    fun getOrderItemsNew(@Body ordersKitchenPostModel: OrdersKitchenPostModel): Observable<Response<BaseList<OrdersKitchen>>>

    @GET( "Services/hs/MobileKitchen?cmd=get_kitchen_hashes")
    fun getHashes(): Observable<Base<HashModel>?>?

    @JvmSuppressWildcards
    @POST( "Services/hs/MobileKitchen?cmd=dish_cooked")
    fun sendDishCooked(@Body dishCookedModel : List<DishCookedModel>): Single<BaseList<Meta>>

    @POST( "Services/hs/MobileKitchen?cmd=history_dish_cooked")
    fun getHistory(@Body historyModel : OrdersKitchenPostModel): Observable<BaseList<HistoryModel>>
}