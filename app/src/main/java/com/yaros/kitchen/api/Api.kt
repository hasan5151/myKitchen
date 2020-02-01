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

    val SUCCESS = "SUCCESS"
    val FAILED= "FAILED"

    fun getApi(): ApiService {
        val client = OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor("29d2e36a-cb7a-41e4-90e3-552d66a7bff4"))
//            .addInterceptor(AuthInterceptor(Preferences.getPref("waiter_token","",context)))
            .addInterceptor(OauthInterceptor("mobi","123"))
            .addInterceptor(TokenTest("fa907412-a069-11e7-aa0e-002522ec5b96","123",context))
//            .addInterceptor(TokenExpireInterceptor())
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

    private fun saveToken(data: AuthToken){
        Preferences.saveOauth(context,data)
    }

    private fun goToLoginPage(){
        //TODO go to login page
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
                    val client = OkHttpClient()

  /*                  val urlBuilder =
                        HttpUrl.parse("https://api.github.help")!!.newBuilder()
                     urlBuilder.addQueryParameter("user", "vogella")
                    val url = urlBuilder.build().toString()
*/

                    val request = Request.Builder()
                        .header("Authorization", "your token")
                        .url(API_URL+"hs/MobileKitchen?cmd=login_waiter&password=123&waiter_id=fa907412-a069-11e7-aa0e-002522ec5b96&app_id=mobile_kitchen&version=9")
                         .build()

                    val response = client.newCall(request).execute()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(
                            call: Call,
                            e: IOException
                        ) {
                            e.printStackTrace()
                        }

                        @Throws(IOException::class)
                        override fun onResponse(
                            call: Call,
                            response: Response
                        ) {
                            if (!response.isSuccessful) {
                                throw IOException("Unexpected code $response")
                            } else { // do something wih the result

                                System.out.println("selam")
                            }
                        }
                    })



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

