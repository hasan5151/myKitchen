package com.yaros.kitchen.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val waiterToken: String?) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (waiterToken != null) {
            val url = chain.request().url()
                .newBuilder()
                .addQueryParameter("waiter_token", waiterToken)
                .build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        } else chain.proceed(chain.request())
    }




}