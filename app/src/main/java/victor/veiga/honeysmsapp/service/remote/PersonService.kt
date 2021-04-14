package victor.veiga.honeysmsapp.service.remote

import retrofit2.Call
import retrofit2.http.*
import victor.veiga.honeysmsapp.service.model.HeaderModel
import victor.veiga.honeysmsapp.service.model.LoginErroModel
import victor.veiga.honeysmsapp.service.remote.LoginRequest


interface PersonService {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body loginRequest : LoginRequest): Call<HeaderModel>

//    @Headers("Content-Type: application/json")
//    @POST("login")
//    fun validaLogin(@Body loginRequest : LoginRequest): Call<LoginErroModel>

}



