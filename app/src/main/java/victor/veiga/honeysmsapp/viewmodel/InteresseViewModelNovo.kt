package victor.veiga.honeysmsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import victor.veiga.honeysmsapp.infra.SecurityPreferences
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.listener.ValidationListener
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.model.InteresseRequest
import victor.veiga.honeysmsapp.service.model.LeadModel
import victor.veiga.honeysmsapp.service.repository.InteresseRepository
import victor.veiga.honeysmsapp.service.repository.LeadRepository

class InteresseViewModelNovo (application : Application): AndroidViewModel(application) {

    private val mSharedSecurity = SecurityPreferences(application)

    private val mInteresseRepository = InteresseRepository(application)
    private val mLeadRepository = LeadRepository(application)

    private val mLeadList = MutableLiveData<LeadModel>()
    var leads : LiveData<LeadModel> = mLeadList

    private val mValidation = MutableLiveData<ValidationListener> ()
    var validation : LiveData<ValidationListener> = mValidation

    private val mInteresseList = MutableLiveData <InteresseModel>()
    var interesse : LiveData <InteresseModel> = mInteresseList


    fun getLead (idCampanha: String) {
        mLeadRepository.loadLead(idCampanha, object : APIListener<LeadModel> {
            override fun onSuccess(model: LeadModel) {
                mLeadList.value = model
            }

            override fun onFailure(str: String) {
                // mList.value = TODO()
            }

        })
    }

    fun listLead(onComplete: (() -> Unit)? = null) {
        var token = mSharedSecurity.getKey("accessToken")
        var idCampanha = mSharedSecurity.getKey("idCampanhaSelected")
         mLeadRepository.loadLead(idCampanha, object : APIListener<LeadModel>{
             override fun onSuccess(model: LeadModel) {
                 mLeadList.value = model
                 onComplete?.invoke()
             }

             override fun onFailure(str: String) {
                 mLeadList.value = null
             }

         })
    }

    fun updateInteresse(interesse: InteresseRequest, onComplete: (() -> Unit)? = null) {

        var token = mSharedSecurity.getKey("accessToken")

        if (interesse.id != null)  {
            mInteresseRepository.interessePost(interesse, token, object : APIListener<Boolean> {

                override fun onSuccess(model: Boolean) {
                    listLead(onComplete)
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }




            })

        }

    }


}