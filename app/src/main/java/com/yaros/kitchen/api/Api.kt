package com.yaros.kitchen.api

 import android.content.Context
 import android.os.Build
 import android.provider.Settings
 import android.util.Log
 import com.yaros.kitchen.BuildConfig
 import com.yaros.kitchen.BuildConfig.API_URL
 import com.yaros.kitchen.models.AuthToken
 import com.yaros.kitchen.utils.Preferences
 import io.reactivex.android.schedulers.AndroidSchedulers
 import io.reactivex.schedulers.Schedulers
 import okhttp3.*
 import retrofit2.Retrofit
 import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
 import retrofit2.converter.gson.GsonConverterFactory
 import java.io.IOException
 import java.util.concurrent.TimeUnit


class Api(val user : String,val password : String,val context : Context) {

    val SUCCESS = "SUCCESS"
    val FAILED= "FAILED"

    fun getApi(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(OauthInterceptor(user,password))
//            .addInterceptor(AuthInterceptor("d25b8e59-55d8-41ac-b658-e223a62f2991"))
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


    private val authenticator =
        label@ Authenticator { route: Route?, response: Response ->
            System.out.println("selam sikeruu ha ${response.body()?.string()}")
        //    if (response.code() == 401) {
                val token = getApi()?.loginWaiter(
                    user, password, Build.MODEL,
                    Settings.Secure.getString(
                        context.contentResolver!!,
                        Settings.Secure.ANDROID_ID
                    ), BuildConfig.VERSION_NAME
                )
                token?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        {
                            run {
                                when (it?.meta?.status) {
                                    SUCCESS -> {
                                        saveToken(it.data)
                                    }
                                    FAILED -> {
                                        goToLoginPage()
                                    }
                                }
                            }
                        },
                        { t -> t.printStackTrace() })
        //    }
       //     }
            null
        }

    private fun saveToken(data: AuthToken){
        Preferences.saveOauth(context,data)
    }

    private fun goToLoginPage(){
        //TODO go to login page
    }
}