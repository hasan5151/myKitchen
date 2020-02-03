package com.yaros.kitchen.api

import android.content.Context
import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yaros.kitchen.repositories.TokenRepo
import com.yaros.kitchen.utils.Preferences
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class TokenTest(val waiterId: String, val password : String, val context: Context) : Interceptor {
    fun NewToken(chain: Interceptor.Chain, @NonNull accessToken: String): Response {
        return run {
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("waiter_token", accessToken)
                .build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val refreshResponse = chain.refreshToken(chain)
 //       val request = chain.request()
        return     chain.proceed(refreshResponse.request())
    }

    private fun Interceptor.Chain.refreshToken(chain: Interceptor.Chain): Response {
        val string = Preferences.getPref("waiter_token","",context)
        System.out.println("textxx ${string}")
//        val response = proceed(chain.request())
        val request = NewToken(chain,string!!).request()
        System.out.println("testx ${request}")
        val response = proceed(request) // ilk request e query ekle
        val gson = Gson()
        if (response.code()==406){
            //TODO session ended
        }
        else
            try {
                val responseBody= response.body()?.string()
                System.out.println("test ${responseBody}")
                if (responseBody!!.contains("Сессия не найдена")) {
                    val tokenRepo = TokenRepo(TokenService())
                     val string = tokenRepo.getToken(waiterId,password)
                    System.out.println("anan ${string}")
                     Preferences.savePref("waiter_token",string,context)

                    val string2 = Preferences.getPref("waiter_token","",context)
                    return  NewToken(chain,string!!)

                    //refresh token
                } else{
                    val string = Preferences.getPref("waiter_token","",context)
                    System.out.println("test2 ${string}")
                    return  NewToken(chain,string!!)

                }
            }catch(e: Throwable) {
                e.printStackTrace()
            }


/*
         if (response.code() == 401) {
           //
        }else
           //
           */

        return response
    }
}
