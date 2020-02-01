package com.yaros.kitchen.api

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yaros.kitchen.models.Base
import com.yaros.kitchen.repositories.TokenRepo
import com.yaros.kitchen.utils.Preferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
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
                val responseBody= response.body()?.string()
//                System.out.println(" senin anin ${responseBody}")
//                System.out.println("salak aq ${responseBody}")
                if(responseBody?.contentEquals("code")!!){
                    System.out.println("anini siksinler")
                }else
                    System.out.println("babani siksinler")
        /*        val jsonObject = JSONObject(responseBody)
                val meta = jsonObject.getJSONObject("meta")
                val code = meta.getString("code")*/
                if (responseBody.contains("10")) {
                    val tokenRepo = TokenRepo(TokenService())
                    val string = tokenRepo.getToken("fa907412-a069-11e7-aa0e-002522ec5b96","123")
                    System.out.println("orospu cocugu ${string}")
                   // Preferences.savePref("waiter_token","",context)
                    //return  NewToken(chain,string!!)
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
