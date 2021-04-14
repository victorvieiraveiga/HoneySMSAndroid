package victor.veiga.honeysmsapp.service.model

import com.google.gson.annotations.SerializedName

class LoginErroModel {

        @SerializedName("success")
        var success: Boolean = true

        @SerializedName("errors")
        lateinit var errors: ArrayList<String>

}