package com.yaros.kitchen.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences {

    companion object{
        val USER_PREFERENCES = "user_preferences"
        @Synchronized
        fun getPref(key: String?, initial: String?, context: Context?): String? {
            val sharedPref: SharedPreferences = getPreferences(context)
            return sharedPref.getString(key, initial)
        }

        @Synchronized
        fun savePref(key: String?, value: String?, context: Context?) {
            val sharedPref: SharedPreferences = getPreferences(context)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        private fun getPreferences(context: Context?): SharedPreferences {
            return context!!.getSharedPreferences(
                USER_PREFERENCES,
                Context.MODE_PRIVATE
            )
        }
    }
}