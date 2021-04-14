package victor.veiga.honeysmsapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import victor.veiga.honeysmsapp.R
import victor.veiga.honeysmsapp.infra.SecurityPreferences
import victor.veiga.honeysmsapp.service.remote.LoginRequest
import victor.veiga.honeysmsapp.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel
    private lateinit var loadinDialog: LoadingDialog

    val mSecurity = SecurityPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         loadinDialog = LoadingDialog(this)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val PERMISSION_REQUEST_CODE = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.GET_ACCOUNTS)
                == PackageManager.PERMISSION_DENIED
            ) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it")
                val permissions =
                    arrayOf<String>(Manifest.permission.SEND_SMS)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        }

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(R.layout.activity_login)

        //grava sua propriedade do botão switch
        buttonSwitch.setOnCheckedChangeListener{buttonView, onSwitch ->
            if (onSwitch) {
                mSecurity.storeKey("switch", "On")

            } else {
                mSecurity.storeKey("switch", "Off")
            }
        }

        loadStatusSwitch ()

        setListeners()
        observe()

        //verifica se usuario esta logado
        //verifyLoggedUser()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.buttonLogin) {
            loadinDialog.startLoadingDialog()
            handleLogin()
        }
    }


    private fun setListeners() {
        buttonLogin.setOnClickListener(this)
    }

    //Verifica se usuario esta logado
//    private fun verifyLoggedUser() {
//        mViewModel.verifyLoggedUser()
//    }

    //observa viewModel
    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {

                startActivity(Intent(this, CampaingActivity::class.java))
                loadinDialog.dismissDialog()
            } else {
                val message = it.failure()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.loggedUser.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, CampaingActivity::class.java))
            }
        })
    }

    //Autentica usuario
    private fun handleLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        //valida email
        if (email != "") {
            if (password != "") {
                //grava user e senha
                switchManagerStore()
                var login: LoginRequest =
                    LoginRequest(email, password)
                //mViewModel.validaLogin(login)
                mViewModel.doLogin(login)

            } else {
                Toast.makeText(this, "informe uma senha.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "informe um login.", Toast.LENGTH_SHORT).show()
        }
        //valida senha


    }

    //Se o botão swicht estiver ligado salvar no dispositivo o login e senha. Senão grava vazio.
    fun switchManagerStore() {

        val user = mSecurity.getKey("user")
        if (user == "Default Value") {
            if (buttonSwitch.isChecked ) {
                mSecurity.storeKey("user", editEmail.text.toString())
                mSecurity.storeKey("password", editPassword.text.toString())
            } else {
                mSecurity.storeKey("user", "")
                mSecurity.storeKey("password", "")
            }
        }
    }

    fun loadStatusSwitch () {

        val status = mSecurity.getKey("switch").toString()
        var login = mSecurity.getKey("user").toString()
        val password = mSecurity.getKey("password").toString()

        if ( status != "") {
            if (status == "On") {
                buttonSwitch.setChecked(true)
                editEmail.setText(login)
                editPassword.setText(password)
            }else {
                buttonSwitch.setChecked(false)
                editEmail.setText("")
                editPassword.setText("")
            }

        }else {
            buttonSwitch.setChecked(false)
        }
    }
}
