package com.yaros.kitchen.api

import com.yaros.kitchen.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TokenService {
    fun getApi(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(OauthInterceptor("mobi","123"))
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(
                100,
                TimeUnit.SECONDS
            )
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create<ApiService>(ApiService::class.java)
    }
}