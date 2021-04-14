package victor.veiga.honeysmsapp.infra

import android.content.Context
import android.util.Log

class ManagerToken (val context: Context) {

    private val mSharedPreferences = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE)

    fun storeToken( value: String) {

        mSharedPreferences.edit().putString("accessToken", value).apply()
    }

    fun getToken(): String {
        val token: String
        try {
             token = mSharedPreferences.getString("accessToken", "default value").toString()
            return  token
        }catch (e:  NullPointerException ) {
            Log.d("", "")
            return  e.toString()
        }

    }


}