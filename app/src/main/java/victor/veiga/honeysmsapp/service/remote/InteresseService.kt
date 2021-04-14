package victor.veiga.honeysmsapp.service.remote

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import victor.veiga.honeysmsapp.service.model.InteresseModel

interface InteresseService {

    //@FormUrlEncoded
    @POST("leads-interesse/{id}/{interesse}")
    fun interessePost(@Path(value = "id",encoded = true) id: String,
                      @Path(value = "interesse", encoded = true) interesse: Int
    ): Call<InteresseModel>

}