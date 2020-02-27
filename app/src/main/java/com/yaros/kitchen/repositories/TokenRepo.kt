package com.yaros.kitchen.repositories

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.utils.IpAddress
import com.yaros.kitchen.utils.UniqueId


class TokenRepo  (val tokenService: TokenService, val context : Context) {
    fun getToken(waiterId: String,password : String): String? {
        System.out.println("waiterId2 ${waiterId}")
        return tokenService.getApi()
            .login(waiterId,password,getDeviceName(), "mobile_kitchen",
                UniqueId.get(context),BuildConfig.VERSION_NAME,IpAddress(context).fromWifi())
            ?.compose(RxSchedulers.DEFAULT.applyObservable())
            ?.filter { it?.meta?.code?.contentEquals("0")!! }
            ?.map { it.data?.waiter_token }?.blockingFirst()
    }

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        var phrase = ""
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c)
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase += c
        }
        return phrase
    }
}
