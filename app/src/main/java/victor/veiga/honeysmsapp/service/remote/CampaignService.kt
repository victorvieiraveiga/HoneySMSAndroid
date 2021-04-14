package victor.veiga.honeysmsapp.service.remote

import retrofit2.Call
import retrofit2.http.*
import victor.veiga.honeysmsapp.service.model.CampaignModel
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.model.LeadModel


interface CampaignService {
    @Headers("Content-Type: application/json")

    @GET("campanhas-vendedor")
    fun listCampaign(): Call<CampaignModel>

    @GET("leads-campanha/{id}")
    fun listLead(@Path(value="id",encoded = true)id:String): Call<LeadModel>

//    //@FormUrlEncoded
//    @POST("leads-interesse")
//    fun interessePost(
//        @Field("id") id: String,
//        @Field("interesse") interese: Int
//    ): Call<Boolean>

    //@FormUrlEncoded
    @POST("leads-interesse/{id}/{interesse}")
    fun interessePost(@Path(value = "id",encoded = true) id: String,
                      @Path(value = "interesse", encoded = true) interesse: Int
                      ): Call<InteresseModel>

}



//@GET ("Task/{id}")
//fun load (@Path(value="id", encoded=true) id: Int): Call<Model>