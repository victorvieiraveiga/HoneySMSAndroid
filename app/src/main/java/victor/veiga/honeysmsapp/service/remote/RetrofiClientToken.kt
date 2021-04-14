package victor.veiga.honeysmsapp.service.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import android.content.Context
import retrofit2.converter.gson.GsonConverterFactory
import victor.veiga.honeysmsapp.infra.ManagerToken
import victor.veiga.honeysmsapp.infra.SecurityPreferences

class RetrofiClientToken<T>(context: Context) {
    //val mSharedPreferences = SecurityPreferences(context)
    val baseUrl = "https://honeyapimobiledesenv.azurewebsites.net/api/"
    //val token = mSharedPreferences.getToken("accessToken")

    val client =  OkHttpClient.Builder()
        //.addInterceptor(OAuthInterceptor("Bearer", token.toString()))
        .addInterceptor(OAuthInterceptor("Bearer",context))
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }
}

class OAuthInterceptor(private val tokenType: String, val context: Context): Interceptor {
    //val mSharedPreferences = SecurityPreferences(context)
    val mManagerToken = ManagerToken(context)
    val acceessToken = this.mManagerToken.getToken()

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $acceessToken").build()

        return chain.proceed(request)
    }
}