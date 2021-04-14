package victor.veiga.honeysmsapp.service.remote

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import victor.veiga.honeysmsapp.infra.ManagerToken
import victor.veiga.honeysmsapp.infra.SecurityPreferences

class RetrofitTokenPost <T>(context: Context, token: String){
    //val mContext: Context = context.applicationContext

   // val mSharedPreferences = SecurityPreferences(context)//= context.getSharedPreferences("accessToken", Context.MODE_PRIVATE)//SecurityPreferences(context)
    val baseUrl = "https://honeyapimobiledesenv.azurewebsites.net/api/"

   // var tokenAcesso : String = mSharedPreferences.getKey("accessToken")

    val client =  OkHttpClient.Builder()
        //.addInterceptor(OAuthInterceptor("Bearer", token.toString()))
        .addInterceptor(OAuthInterceptor2("Bearer",context,token))
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

class OAuthInterceptor2(private val tokenType: String, val context: Context, val acceessToken: String): Interceptor {
    //val mSharedPreferences = SecurityPreferences(context)
    //val mManagerToken = ManagerToken(context)
    //val acceessToken = mManagerToken.getToken()

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $acceessToken").build()

        return chain.proceed(request)
    }
}