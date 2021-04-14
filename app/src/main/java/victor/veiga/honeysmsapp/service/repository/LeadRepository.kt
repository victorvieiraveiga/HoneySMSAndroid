package victor.veiga.honeysmsapp.service.repository

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.model.LeadModel
import victor.veiga.honeysmsapp.service.remote.CampaignService
import victor.veiga.honeysmsapp.service.remote.RetrofiClientToken
import victor.veiga.honeysmsapp.service.remote.RetrofitClient

class LeadRepository (val context: Context){

    public val mRemote = RetrofitClient.createService(
        CampaignService::class.java)

    fun loadLead (id: String, listener: APIListener<LeadModel>) {

       val call = RetrofiClientToken<CampaignService>(context).create(CampaignService::class.java).listLead(id)
        call.enqueue(object : Callback<LeadModel> {

            override fun onFailure(call: Call<LeadModel>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<LeadModel>, response: Response<LeadModel>) {
                if (response.code()!=200) {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
                response.body()?.let { listener.onSuccess(it) }
            }

        })

    }
}