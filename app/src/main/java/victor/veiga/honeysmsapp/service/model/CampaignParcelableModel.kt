package victor.veiga.honeysmsapp.service.model

import android.os.Parcel
import android.os.Parcelable

class CampaignParcelableModel( ) : Parcelable {

    var id: String = ""
    var nome: String = ""
    var dataExecucao: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        nome = parcel.readString().toString()
        dataExecucao = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nome)
        parcel.writeString(dataExecucao)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CampaignParcelableModel> {
        override fun createFromParcel(parcel: Parcel): CampaignParcelableModel {
            return CampaignParcelableModel(parcel)
        }

        override fun newArray(size: Int): Array<CampaignParcelableModel?> {
            return arrayOfNulls(size)
        }
    }


}