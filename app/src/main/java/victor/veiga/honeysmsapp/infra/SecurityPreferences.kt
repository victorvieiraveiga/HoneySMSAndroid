package victor.veiga.honeysmsapp.infra

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager

class SecurityPreferences(val context : Context) {


    fun storeKey(key: String, value: String) {

        val mSharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getKey(key: String): String {

            val mSharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE) //context.getSharedPreferences(key, Context.MODE_PRIVATE)
            return mSharedPreferences.getString(key,"Default Value").toString() //getString("accessToken", "").toString()

    }


    }
