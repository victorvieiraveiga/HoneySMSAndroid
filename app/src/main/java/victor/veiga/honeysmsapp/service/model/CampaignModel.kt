package victor.veiga.honeysmsapp.service.model

import com.google.gson.annotations.SerializedName
import java.util.*

class CampaignModel {
    @SerializedName("success")
    var success : Boolean = true

    @SerializedName("data")
    lateinit var data : Array<DataCampaign>

    public class DataCampaign  {
        @SerializedName("id")
        var id : String = ""

        @SerializedName("nome")
        var nome : String = ""

        @SerializedName("dataExecucao")
        lateinit var dataExecucao : String
    }
}