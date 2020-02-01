package com.yaros.kitchen.repositories

import android.os.Build
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService

class TokenRepo  (val tokenService: TokenService) {
    fun getToken(waiterId: String,password : String): String? {
        return tokenService.getApi()
            .login(waiterId,password,"mobile_kitchen", "1")
            ?.compose(RxSchedulers.DEFAULT.applyObservable())
            ?.filter { it?.meta?.code?.contentEquals("0")!! }
            ?.map { it.data?.waiter_token }?.blockingFirst()
    }
}
