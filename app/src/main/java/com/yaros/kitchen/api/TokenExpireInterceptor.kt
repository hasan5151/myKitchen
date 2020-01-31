package com.yaros.kitchen.api

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yaros.kitchen.models.Base
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenExpireInterceptor () : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val refreshResponse = chain.refreshToken(chain.request())
        val request = chain.request()
        return     chain.proceed(request)
    }

    private fun Interceptor.Chain.refreshToken(request: Request): Response {
        val response = proceed(request)
        val gson = Gson()
        if (response.code()==406){

         //TODO session ended

        }
        else
        try {
            val base = gson.fromJson(response.body()?.string(), Base::class.java)
            if (base.meta.code.contentEquals("10")) {
                //refresh token
            }
        }catch(e: JsonSyntaxException) {
            e.printStackTrace()
        }


/*
         if (response.code() == 401) {
            System.out.println(" selam this is 401")
        }else
            System.out.println(" selam this is else")*/
        return response
    }
}
