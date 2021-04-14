package victor.veiga.honeysmsapp.service.listener

import victor.veiga.honeysmsapp.service.model.CampaignModel
import victor.veiga.honeysmsapp.service.model.HeaderModel
import victor.veiga.honeysmsapp.service.model.LoginErroModel

interface APIListener<T> {
    fun onSuccess (model: T)
    fun onFailure(str: String)
}

//interface CampaignListener {
//    fun onSuccess (model: CampaignModel)
//    fun onFailure(str: String)
//}