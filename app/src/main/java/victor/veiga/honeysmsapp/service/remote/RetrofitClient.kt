package victor.veiga.honeysmsapp.service.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        lateinit var retrofit : Retrofit
        val baseUrl = "https://honeyapimobiledesenv.azurewebsites.net/api/"

        private fun getRetrofitInstance() : Retrofit {

            val httpClient = OkHttpClient.Builder()

            if (!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
        fun <S> createService(serviceClass: Class<S>) : S {
            return getRetrofitInstance()
                .create(serviceClass)
        }
    }

}