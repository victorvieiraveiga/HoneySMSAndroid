package victor.veiga.honeysmsapp.service.model

import com.google.gson.annotations.SerializedName

class LeadModel {
    @SerializedName("success")
    var success : Boolean = true

    @SerializedName("data")
    lateinit var data : Array<DataLead>

    public class DataLead  {
        @SerializedName("id")
        var id : String = ""

        @SerializedName("nome")
        var nome : String = ""

        @SerializedName("telefone")
        var telefone : String = ""

        @SerializedName("interesse")
        var interesse : Int = 0
    }
}