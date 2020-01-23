package com.yaros.kitchen.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.yaros.kitchen.models.Token

class Preferences {

    companion object{
        val USER_PREFERENCES = "user_preferences"
        val OAUTH = "auth"

        @Synchronized
        fun saveOauth(context: Context?, oauth: Token?) {
            val gson = Gson()
            val json = gson.toJson(oauth)
            Preferences.savePref(
                OAUTH,
                json,
                context
            )
        }

        @Synchronized
        fun getOauth(context: Context): Token? { //        SharedPreferences sharedPref = getPreferences(context);
            val sharedPref = context.getSharedPreferences(
                USER_PREFERENCES,
                Context.MODE_PRIVATE
            )
            val gson = Gson()
            val json =
                sharedPref.getString(OAUTH, null)
            return gson.fromJson<Token>(json, Token::class.java)
        }


        @Synchronized
        fun savePref(
            key: String?,
            value: String?,
            context: Context?
        ) {
            val sharedPref: SharedPreferences =
               getPreferences(context)
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