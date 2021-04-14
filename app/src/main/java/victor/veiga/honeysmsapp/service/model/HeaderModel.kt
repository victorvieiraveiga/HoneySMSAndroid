package victor.veiga.honeysmsapp.service.model

import com.google.gson.annotations.SerializedName

class HeaderModel {

    @SerializedName("success")
    var success : Boolean = true

    @SerializedName("data")
    lateinit var data : Data


    public class Data  {
        @SerializedName("accessToken")
        var accessToken : String = ""
        @SerializedName("expiresIn")
        var expiresIn : Int = 0

        @SerializedName("userToken")
        lateinit var userToken : UserToken
    }

    public class UserToken {
        @SerializedName("id")
        var id : String = ""
        @SerializedName("email")
        var email: String = ""
        @SerializedName("claims")
        lateinit var claims : Array<Claims>
    }
    public class Claims {
        @SerializedName("value")
        var value : String = ""
        @SerializedName("type")
        var type : String = ""
    }

}



