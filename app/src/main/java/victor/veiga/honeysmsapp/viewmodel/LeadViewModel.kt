package victor.veiga.honeysmsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.listener.ValidationListener
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.model.InteresseRequest
import victor.veiga.honeysmsapp.service.model.LeadModel
import victor.veiga.honeysmsapp.service.model.LoginErroModel
import victor.veiga.honeysmsapp.service.repository.InteresseRepository
import victor.veiga.honeysmsapp.service.repository.LeadRepository
import victor.veiga.honeysmsapp.view.LeadActivity
import victor.veiga.honeysmsapp.view.adapter.LeadAdapter

class LeadViewModel (application: Application) : AndroidViewModel(application) {


    private val mLeadRepository = LeadRepository(application)


    private var mList  = MutableLiveData<LeadModel?>()
    var leads : LiveData<LeadModel?> = mList

    var leadsModel = LeadModel ()



    private val mListInteresse = MutableLiveData<InteresseModel?>()
    val interesse : MutableLiveData<InteresseModel?> = mListInteresse



    fun listaLead(id: String) {

        mLeadRepository.loadLead(id, object : APIListener<LeadModel> {
            override fun onSuccess(model: LeadModel) {
                mList.value = model
            }

            override fun onFailure(str: String) {
               // mList.value = TODO()
            }

        })
    }
}