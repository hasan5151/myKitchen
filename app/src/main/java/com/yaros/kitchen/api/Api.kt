package com.yaros.kitchen.api

 import android.util.Base64
 import com.yaros.kitchen.BuildConfig.API_URL
 import com.yaros.kitchen.BuildConfig.AUTH_PASS
 import okhttp3.Interceptor
 import okhttp3.OkHttpClient
 import retrofit2.Retrofit
 import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
 import retrofit2.converter.gson.GsonConverterFactory
 import java.util.concurrent.TimeUnit


class Api {
    companion object {
        fun getApi(): ApiService? {
            val client = OkHttpClient.Builder()
                .addInterceptor(headerAuthInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(
                    100,
                    TimeUnit.SECONDS
                ) //    .retryOnConnectionFailure(false)
                .build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create<ApiService>(ApiService::class.java)
        }

        private val headerAuthInterceptor =
            Interceptor { chain: Interceptor.Chain ->
                chain.call().cancel()
                val original = chain.request()
                chain.proceed(original).close()
                chain.proceed(original)
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("Authorization", "Bearer ${AUTH_PASS}")
                val request = requestBuilder
                    .build()
                chain.proceed(request)
            }

    }


    fun String.decode(): String {
        return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
    }

    fun String.encode(): String {
        return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
    }

}