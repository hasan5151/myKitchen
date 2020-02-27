package com.yaros.kitchen.utils

import android.content.Context
import android.provider.Settings.Secure

class UniqueId {
    companion object{
        fun get(context: Context): String? {
            return Secure.getString(
                context.contentResolver,
                Secure.ANDROID_ID
            )
        }
    }
}