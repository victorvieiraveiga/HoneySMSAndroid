package victor.veiga.honeysmsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.model.CampaignModel
import victor.veiga.honeysmsapp.service.model.LoginErroModel
import victor.veiga.honeysmsapp.service.repository.CampaignRepository

class CampaignViewModel (application : Application): AndroidViewModel(application){

    private val mCampaignRepository = CampaignRepository(application)

    private val mList  = MutableLiveData<CampaignModel?>()
    var campaigns : LiveData<CampaignModel?> = mList

    fun listCampaign () {
        mCampaignRepository.loadCampaign(object : APIListener<CampaignModel>{
            override fun onSuccess(model: CampaignModel) {
                mList.value = model
            }

            override fun onFailure(str: String) {
                mList.value = TODO()
            }

        })
    }

}