package com.yaros.kitchen.api

import android.content.Context
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.yaros.kitchen.repositories.TokenRepo
import com.yaros.kitchen.utils.Preferences
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http2.Header
import java.io.IOException

class TokenInterceptor(val waiterId: String, val password : String, val context: Context) : Interceptor {
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
        return  chain.proceed(refreshResponse.request())
    }

    private fun Interceptor.Chain.refreshToken(chain: Interceptor.Chain): Response {
        val string = Preferences.getPref("waiter_token","",context)
        System.out.println("textxx ${string}")
        val request = NewToken(chain,string!!).request()
        System.out.println("testx ${request}")
        val response = proceed(request)
        if (response.code()==406){
            //TODO session ended
        }else if (response.code() == 500){
            return  NewToken(chain,string)
        }
        else
            try {
                val responseBody= response.body()?.string()

                System.out.println("test ${responseBody}")
                if (responseBody!!.contains("Сессия не найдена")) {
                    val tokenRepo = TokenRepo(TokenService(context),context)
                    System.out.println(" waiterId ${waiterId}")
                    val string = tokenRepo.getToken(waiterId,password)
                    System.out.println("test2 ${string}")
                    Preferences.savePref("waiter_token",string,context)

                    val string2 = Preferences.getPref("waiter_token","",context)
                    return  NewToken(chain,string!!)

                    //refresh token
                } else{
             /*       val string = Preferences.getPref("waiter_token","",context)
                    System.out.println("test2 ${string}")
                    return  NewToken(chain,string!!)*/

                }
            }catch(e: Throwable) {
                e.printStackTrace()
            }
            /*if (response.code() == 401) {
                //
            }else
           //
           */

        return response
    }
}
