package com.yaros.kitchen.api

import android.content.Context
import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.models.Base
import com.yaros.kitchen.repositories.TokenRepo
import com.yaros.kitchen.utils.Preferences
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class TokenTest(val waiterId: String, val password : String, val context: Context) : Interceptor {
    fun NewToken(chain: Interceptor.Chain, @NonNull accessToken: String): Response {
        return run {
            val url = chain.request().url()
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
        val request = chain.request()
        return     chain.proceed(refreshResponse.request())
    }

    private fun Interceptor.Chain.refreshToken(chain: Interceptor.Chain): Response {
        val string = Preferences.getPref("waiter_token","",context)
        System.out.println("anan2x ${string}")

//        val response = proceed(chain.request())
        val response = proceed(NewToken(chain,string!!).request())
        val gson = Gson()
        if (response.code()==406){
            //TODO session ended
        }
        else
            try {
                val responseBody= response.body()?.string()
                System.out.println("anan ${responseBody}")
  /*              System.out.println("salak aq ${responseBody}")
                val jsonObject = JSONObject(responseBody)
                val meta = jsonObject.getJSONObject("meta")
                val code = meta.getString("code")*/
                if (responseBody!!.contains("Сессия не найдена")) {
                    val tokenRepo = TokenRepo(TokenService())
                     val string = tokenRepo.getToken(waiterId,password)
                    System.out.println("anan ${string}")
                     Preferences.savePref("waiter_token","",context)
                     return  NewToken(chain,string!!)

                    //refresh token
                } else{
                    val string = Preferences.getPref("waiter_token","",context)
                    System.out.println("anan2 ${string}")
                    return  NewToken(chain,string!!)

                }
            }catch(e: JsonSyntaxException) {
                e.printStackTrace()
            }


/*
         if (response.code() == 401) {
            System.out.println(" selam this is 401")
        }else
            System.out.println(" selam this is else")*/
        System.out.println("anan3 ")
        return response
    }
}
