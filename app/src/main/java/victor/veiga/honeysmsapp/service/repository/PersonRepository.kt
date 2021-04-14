package victor.veiga.honeysmsapp.service.repository

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import victor.veiga.honeysmsapp.service.model.HeaderModel
import victor.veiga.honeysmsapp.service.remote.LoginRequest
import victor.veiga.honeysmsapp.service.remote.PersonService
import victor.veiga.honeysmsapp.service.remote.RetrofitClient
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.model.LoginErroModel

class PersonRepository {

     public val mRemote = RetrofitClient.createService(
         PersonService::class.java)



   // fun login (email: String, password: String, listener: APIListener) {
   fun login (loginRequest: LoginRequest, listener: APIListener<HeaderModel>) {
       //val call : Call<HeaderModel> = mRemote.login(email,password)
           val call: Call<HeaderModel> = mRemote.login(loginRequest)

           call.enqueue(object : Callback<HeaderModel> {

               override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                   listener.onFailure(t.message.toString())
               }

               override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                   if (response.code() != 200) {
//                       val validation =
//                           Gson().fromJson(response.errorBody()!!.string(), String()::class.java)
                       val validation = "Usuário ou Senha incorretos"
                       listener.onFailure(validation)
                   }

                   response.body()?.let { listener.onSuccess(it) }
               }



           })
   }

//   public fun validaLogin (loginRequest: LoginRequest, listener: APIListener<LoginErroModel>)  {
//
//        var erro: Boolean = false
//        val call  = mRemote.validaLogin(loginRequest)
//
//        call.enqueue(object : Callback<LoginErroModel>{
//
//            override fun onFailure(call: Call<LoginErroModel>, t: Throwable) {
//                val s = ""
//                listener.onFailure(t.message.toString())
//                erro = true
//            }
//
//            override fun onResponse(
//                call: Call<LoginErroModel>,
//                response: Response<LoginErroModel>
//            ) {
//                if (response.code()!=200) {
//                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
//                    listener.onFailure(validation)
//                    erro = false
//                }
//
//
//                response.body()?.let { listener.onSuccess(it) }
//                erro = false
//
//            }
//
//        })
//    }
}