package com.yaros.kitchen.api

import okhttp3.Interceptor

class Api {


    private val headerAuthInterceptor =
        label@ Interceptor { chain: Interceptor.Chain ->
            // try {
                chain.call().cancel()
                val original = chain.request()
                chain.proceed(original).close()
                return@label chain.proceed(original)
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Authorization", "Bearer ${AUTH_PASS}")
            val request = requestBuilder
                .build()
            chain.proceed(request)
        }
}