package com.yaros.kitchen.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yaros.kitchen.repositories.TokenRepo
import com.yaros.kitchen.utils.Preferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenInterceptor(val waiterId: String, val password : String, val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("waiter_token", Preferences.getPref("waiter_token","",context))
            .build()

        val request = chain.request().newBuilder().url(url).build()
        val response = chain.proceed(request)
/*
        if (response.code()==404) {
            val tokenRepo = TokenRepo(TokenService(context),context)
            val string = tokenRepo.getToken(waiterId,password)
            System.out.println("test2 ${string}")
            Preferences.savePref("waiter_token",string,context)
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("waiter_token", Preferences.getPref("waiter_token","",context))
                .build()
            val request = chain.request().newBuilder().url(url).build()
            return chain.proceed(request)
        }*/

/*        val res =response.body()?.string()
        System.out.println("teset ${request.url()}")
        System.out.println("teset ${res}")*/

        if (response.code()==401) {
            val tokenRepo = TokenRepo(TokenService(context),context)
            val string = tokenRepo.getToken(waiterId,password)
            System.out.println("test2 ${string}")
            Preferences.savePref("waiter_token",string,context)
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("waiter_token", Preferences.getPref("waiter_token","",context))
                .build()
            val request = chain.request().newBuilder().url(url).build()
            return chain.proceed(request)
        }
        return  response
    }

}