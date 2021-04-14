package victor.veiga.honeysmsapp.service.repository

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.model.CampaignModel
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.model.InteresseRequest
import victor.veiga.honeysmsapp.service.remote.*
import victor.veiga.honeysmsapp.view.LeadActivity

class InteresseRepository (val context: Context){

    fun interessePost (interesse: InteresseRequest ,  token: String, listener: APIListener<Boolean>) {

        val call = RetrofitTokenPost<InteresseService>(context,token).create(InteresseService::class.java).interessePost(interesse.id, interesse.interesse)
        call.enqueue(object : Callback<InteresseModel>{

            override fun onFailure(call: Call<InteresseModel>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

            override fun onResponse(
                call: Call<InteresseModel>,
                response: Response<InteresseModel>
            ) {
                if (response.code() != 200) {
                    val validation = "Erro Post interesse"
                    listener.onFailure(validation)
                }

                response.body()?.let { listener.onSuccess(it.success) }
            }

        })

    }
}