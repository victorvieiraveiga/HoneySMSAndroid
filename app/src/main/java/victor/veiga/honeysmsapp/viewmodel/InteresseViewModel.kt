package victor.veiga.honeysmsapp.viewmodel

import android.app.Application
import android.content.Context
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
import victor.veiga.honeysmsapp.view.LeadActivity
import victor.veiga.honeysmsapp.view.adapter.LeadAdapter

class InteresseViewModel ( application: Application): AndroidViewModel(application) {

    lateinit var mSharedPreferences : SecurityPreferences
    private val mInteresseRepositoryRepository = InteresseRepository(application)
    private val mLeadRepository = LeadRepository(application)
    //private val mViewModelInteresse = InteresseViewModel()
    private lateinit var  mLead: LeadActivity

    private val mListInteresse  = MutableLiveData<ValidationListener?>()
    var interesse : LiveData<ValidationListener?> = mListInteresse

    private val mListLead  = MutableLiveData<LeadModel?>()
    var listaLead : LiveData<LeadModel?> = mListLead

    lateinit var reciclerLead : MutableLiveData<LeadModel>

   // private var reciclerAdapeter= LeadAdapter(, application)

    init {
        reciclerLead = MutableLiveData()
        mSharedPreferences = SecurityPreferences(application)

        //mLead = LeadActivity()
    }

    fun getRecyclerObserver(application: Application) : MutableLiveData<LeadModel>{
        reciclerLead = MutableLiveData()
        return reciclerLead
    }

    fun makeApiCall (idCampanha: String,param: APIListener<LeadModel>) {
        mLeadRepository.loadLead(idCampanha, object : APIListener<LeadModel>{
            override fun onSuccess(model: LeadModel) {

                mListLead.value = model
               //eciclerLead.value = model
                //reciclerAdapeter = LeadAdapter(model)
               //reciclerAdapeter.notifyDataSetChanged()

            }

            override fun onFailure(str: String) {
                //mListLead(str)
            }

        })
    }
    fun observe() {
        mLead.observe()
    }

//    fun postInteresse (idLead: String, interesseInfo: Int, onComplete:  () -> Unit) {
//
//       val interesse = InteresseRequest()
//       interesse.id  = idLead
//        interesse.interesse = interesseInfo
//        var mSecurityPreferences = SecurityPreferences(getApplication())
//
//        var token = mSecurityPreferences.getKey("accessToken")
//
//        mInteresseRepositoryRepository.interessePost(interesse,token, object : APIListener<InteresseModel> {
//
//            override fun onSuccess(model: InteresseModel) {
//                mListInteresse.value = ValidationListener()
//
//                val idCampanha = mSecurityPreferences.getKey("idCampanhaSelected")
//
//                mLeadRepository.loadLead(idCampanha, object : APIListener<LeadModel>{
//                    override fun onSuccess(model: LeadModel) {
//
//                        mListLead.value = model
//                        //eciclerLead.value = model
//                        //reciclerAdapeter = LeadAdapter(model)
//                        //reciclerAdapeter.notifyDataSetChanged()
//                    }
//
//                    override fun onFailure(str: String) {
//                        //mListLead(str)
//                    }
//
//                })
////                makeApiCall(idCampanha, object : APIListener<LeadModel> {
////                    override fun onSuccess(model: LeadModel) {
////                        reciclerLead.value = model
////                    }
////
////                    override fun onFailure(str: String) {
////                        TODO("Not yet implemented")
////                    }
////
////                })
//                //mLead.createData()
//            }
//            override fun onFailure(str: String) {
//                mListInteresse.value = ValidationListener(str)
//            }
//        })
//        onComplete()
//    }

    //fun listCampaign () {
    //    mInteresseRepositoryRepository.interessePost(interesse: InteresseRequest)

//            .loadCampaign(object : APIListener<CampaignModel> {
//            override fun onSuccess(model: CampaignModel) {
//                mList.value = model
//            }
//
//            override fun onFailure(str: String) {
//                mList.value = TODO()
//            }
//
//        })
        // }
}