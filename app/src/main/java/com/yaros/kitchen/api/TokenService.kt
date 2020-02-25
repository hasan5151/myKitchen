package com.yaros.kitchen.api

import android.content.Context
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.utils.Preferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TokenService(val context : Context) {
    fun getApi(): ApiService {
        val ipStr= Preferences.getPref("ip","-1",context)
        val folderStr= Preferences.getPref("folder","-1",context)
        val loginStr= Preferences.getPref("loginStr","-1",context)
        val passwordStr= Preferences.getPref("passwordStr","-1",context)


        val client = OkHttpClient.Builder()
            .addInterceptor(OauthInterceptor(loginStr,passwordStr))
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(
                100,
                TimeUnit.SECONDS
            )
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://${ipStr}/${folderStr}/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create<ApiService>(ApiService::class.java)
    }
}