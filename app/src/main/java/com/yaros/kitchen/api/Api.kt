package com.yaros.kitchen.api

 import android.content.Context
 import com.yaros.kitchen.room.db.RoomDb
 import com.yaros.kitchen.utils.Preferences
 import okhttp3.*
 import retrofit2.Retrofit
 import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
 import retrofit2.converter.gson.GsonConverterFactory
 import java.lang.NullPointerException
 import java.util.concurrent.TimeUnit

class Api(val context : Context) {
    fun getApi(): ApiService {
        var ipStr= Preferences.getPref("ip","-1",context)
        if(ipStr?.contains("http://")!!)
            ipStr = ipStr.replace("http:///","")


        val folderStr= Preferences.getPref("folder","-1",context)
        val loginStr= Preferences.getPref("loginStr","-1",context)
        val passwordStr= Preferences.getPref("passwordStr","-1",context)

        System.out.println("amanian ${loginStr}") //bW9iaToxMjM=
        System.out.println("amanian ${passwordStr}")

        val room = RoomDb(context)
        var waiterId = ""
        try{
            val waiter = room.WaiterDAO().getWaiter()
            waiterId = waiter.id

        }catch (e : NullPointerException){
            waiterId = ""
        }


            val client = OkHttpClient.Builder()
                .addInterceptor(OauthInterceptor(Preferences.getPref("loginStr","-1",context),Preferences.getPref("passwordStr","-1",context)))
                .addInterceptor(TokenInterceptor(waiterId, "123", context))
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(
                    100,
                    TimeUnit.SECONDS
                )
                .build()

        System.out.println("selam ipne2 ${ipStr}")


        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://${ipStr}/${folderStr}/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create<ApiService>(ApiService::class.java)



    }
}

