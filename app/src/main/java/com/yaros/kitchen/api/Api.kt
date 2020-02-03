package com.yaros.kitchen.api

 import android.content.Context
 import com.google.gson.Gson
 import com.google.gson.JsonSyntaxException
 import com.yaros.kitchen.BuildConfig.API_URL
 import com.yaros.kitchen.models.AuthToken
 import com.yaros.kitchen.models.Base
 import com.yaros.kitchen.utils.Preferences
 import okhttp3.*
 import retrofit2.Retrofit
 import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
 import retrofit2.converter.gson.GsonConverterFactory
 import java.io.IOException
 import java.util.concurrent.TimeUnit


class Api(val context : Context) {
    fun getApi(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(OauthInterceptor("mobi","123"))
            .addInterceptor(TokenTest("fa907412-a069-11e7-aa0e-002522ec5b96","123",context))
             .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(
                100,
                TimeUnit.SECONDS
            )
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create<ApiService>(ApiService::class.java)
    }
}

