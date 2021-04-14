package victor.veiga.honeysmsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import victor.veiga.honeysmsapp.infra.ManagerToken
import victor.veiga.honeysmsapp.infra.SecurityPreferences
import victor.veiga.honeysmsapp.service.model.HeaderModel
import victor.veiga.honeysmsapp.service.remote.LoginRequest
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.listener.ValidationListener
import victor.veiga.honeysmsapp.service.model.LoginErroModel
import victor.veiga.honeysmsapp.service.repository.PersonRepository

class LoginViewModel  (application: Application) : AndroidViewModel(application)  {

    private  val mSecurityPreferences = SecurityPreferences (application)
    private val mPersonRepository = PersonRepository()

    private val mLogin  = MutableLiveData<ValidationListener>()
    var login : LiveData<ValidationListener> = mLogin

    private val mLoggedUser  = MutableLiveData<Boolean>()
    var loggedUser : LiveData<Boolean> = mLoggedUser

    private val mManagerToken = ManagerToken(application)

    //Faz login usando API
    fun doLogin(loginRequest: LoginRequest) {
        mPersonRepository.login(loginRequest, object : APIListener<HeaderModel>{
            override fun onSuccess(model: HeaderModel) {

                //TODO Buscar o retorno da api e salvar no dispositivo
               // mSecurityPreferences.storeToken("accessToken", model.data.accessToken)
                mSecurityPreferences.storeKey("userEmail", model.data.userToken.email)
                mManagerToken.storeToken(model.data.accessToken)
                mLogin.value =  ValidationListener()

            }

            override fun onFailure(str: String) {
                mLogin.value = ValidationListener(str)

            }
        })

    }

    //Verifica se usuario esta logado
//    fun verifyLoggedUser () {
//        val token = mSecurityPreferences.getToken("accessToken")
//        var logged : Boolean = false
//
//        if (token != "") {
//            logged = true
//        }
//
//        mLoggedUser.value = logged
//    }

}