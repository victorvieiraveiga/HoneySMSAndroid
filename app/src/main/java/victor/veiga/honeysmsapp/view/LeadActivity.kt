package victor.veiga.honeysmsapp.view

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.content.ContentProviderOperation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lead.*
import kotlinx.android.synthetic.main.lead_item.view.*
import victor.veiga.honeysmsapp.R
import victor.veiga.honeysmsapp.databinding.ActivityLeadBinding
import victor.veiga.honeysmsapp.infra.MyButton
import victor.veiga.honeysmsapp.infra.MySwipeHelper
import victor.veiga.honeysmsapp.infra.SecurityPreferences
import victor.veiga.honeysmsapp.service.listener.MyButtonClickListener
import victor.veiga.honeysmsapp.service.model.CampaignParcelableModel
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.repository.InteresseRepository
import victor.veiga.honeysmsapp.view.adapter.LeadAdapter
import victor.veiga.honeysmsapp.viewmodel.InteresseViewModel
import victor.veiga.honeysmsapp.viewmodel.InteresseViewModelNovo
import java.security.Permission
import java.security.PermissionCollection
import java.security.Permissions


class LeadActivity (application: Application?=null): AppCompatActivity() {

    private lateinit var mViewModel: InteresseViewModelNovo
    private lateinit var mInteresseViewModel: InteresseViewModel


    private  lateinit  var mRepository : InteresseRepository
    private lateinit var binding : ActivityLeadBinding
    private val TAG = "CONTACT_ADD_TAG"
    private lateinit var contactPermission: Array<String>
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    var dataCampaign: CampaignParcelableModel? = null


    private val mList  = MutableLiveData<InteresseModel?>()
    var campaigns : LiveData<InteresseModel?> = mList

   lateinit var textViewToken: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mSecurityPreferences = SecurityPreferences(this)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "New Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        //actionbar.setDisplayHomeAsUpEnabled(true)
        if (supportActionBar != null) {
            supportActionBar?.setBackgroundDrawable(getResources().getDrawable(R.color.title_color))
            supportActionBar?.title = "Olá, ${mSecurityPreferences.getKey("userEmail")}"
        }

        // Request code for READ_CONTACTS. It can be any number > 0.
        //val PERMISSIONS_REQUEST_READ_CONTACTS : Int = 100;


        //val REQUEST_RUNTIME_PERMISSION = 123
//        val permissons = arrayOf(
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.WRITE_CONTACTS,
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_CALL_LOG
//        )
        dataCampaign = intent.getParcelableExtra("Campaign")



        mViewModel =  ViewModelProvider(this).get(InteresseViewModelNovo::class.java)
        mInteresseViewModel=ViewModelProvider (this).get(InteresseViewModel::class.java)

        //pegar token
        var mSharedPreferences  = this.getSharedPreferences("accessToken", Context.MODE_PRIVATE)
        var accesToken = mSharedPreferences.getString("accessToken","Default Value")


        val nomeCamp : String = dataCampaign?.nome.toString()
        val dataExeCamp: String = dataCampaign?.dataExecucao.toString()

        setContentView(R.layout.activity_lead)

        //Save Contacts /////////////////
        binding = ActivityLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contactPermission = arrayOf(Manifest.permission.WRITE_CONTACTS)
        binding
        //End



        textHeader.text ="Lead ${dataExeCamp} | ${nomeCamp}"

        observe()
        observeInteresse()
        //loadLeads(idCampaign.toString())
        if (dataCampaign != null) {
            //armazena camanha selecionada no dispositivo
            mSecurityPreferences.storeKey("idCampanhaSelected",this.dataCampaign!!.id.toString())
            loadLeads(this.dataCampaign!!.id.toString())
        }




       // add Swipe
        val swipe = object : MySwipeHelper(this, reciclerLead,350) {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun initiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton(this@LeadActivity, "Salvar", 60,0, getColor(R.color.BlueContact), object : MyButtonClickListener{
                    override fun onClick(pos: Int) {
                        Toast.makeText(this@LeadActivity, "Salvar contato", Toast.LENGTH_SHORT).show()
                        //save contacts

                        val name : String = viewHolder.itemView.textNome.text.toString()
                        val phone: String = viewHolder.itemView.textTelefone.text.toString()


                        saveContact(name,phone)
                    }

                }))

                buffer.add(MyButton(this@LeadActivity, ("WhatsApp"), 60,0, getColor(R.color.GreenWhats), object : MyButtonClickListener{
                    override fun onClick(pos: Int) {
                        Toast.makeText(this@LeadActivity, "WhatsUp", Toast.LENGTH_LONG).show()
                        CallWhatApp(viewHolder.itemView.textTelefone.text.toString())
                    }

                }))
            }
        }

        checkForPermission(android.Manifest.permission.WRITE_CONTACTS, "Contacts",PERMISSIONS_REQUEST_READ_CONTACTS)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkForPermission (permission: String, name: String, requesteCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission,name,requesteCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission),requesteCode)
            }
        }

    }

    private fun showDialog (permission: String,name: String,requesteCode: Int) {
        val buider = AlertDialog.Builder(this)
        buider.apply {
            setMessage("Permission to acesses you $name is required to access this app")
            setTitle("Permission Required")
            setPositiveButton("Ok") {
                dialog, which ->  ActivityCompat.requestPermissions(this@LeadActivity, arrayOf(permission), requesteCode)
            }
        }
        val dialog = buider.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun innerCheck (name: String) {
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> innerCheck("Contacts")
        }
    }


    public fun observe () {
        mViewModel.leads.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {
                    reciclerLead.adapter = LeadAdapter(it,application)
                    reciclerLead.layoutManager = LinearLayoutManager(this)
                    val adapter = reciclerLead.adapter
                    adapter!!.notifyDataSetChanged()
                }else {
                    Toast.makeText(this, "Não ha campanhas.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Não ha campanhas.", Toast.LENGTH_SHORT).show()
            }
        })
    }

private fun observeInteresse() {
    mViewModel.interesse.observe(this, androidx.lifecycle.Observer {

        if(it.success){

            reciclerLead.layoutManager = LinearLayoutManager(application)
            val adapter = reciclerLead.adapter
            adapter!!.notifyDataSetChanged()

        } else {
            Toast.makeText(this, "Erro Post Interesse.", Toast.LENGTH_SHORT).show()
        }
    })
}

    private fun loadLeads(id: String) {
        mViewModel.getLead(id)
    }


    fun CallWhatApp ( phone: String) {

        val installed: Boolean = isAppInstalled("com.whatsapp")
        val message: String = ""

        if (installed)
        {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phone + "&text=" + message))
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isAppInstalled(s:String):Boolean {
        val packageManager = getPackageManager()
        var is_installed:Boolean
        try
        {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES)
            is_installed = true
        }
        catch (e:PackageManager.NameNotFoundException) {
            is_installed = false
            e.printStackTrace()
        }
        return is_installed
    }

    private fun saveContact(name: String, phone: String) {


        val ops = ArrayList<ContentProviderOperation>()

        ops.add(
            ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI
            )
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        //------------------------------------------------------ Names

        //------------------------------------------------------ Names
        if (name != null) {
            ops.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI
                )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name
                    ).build()
            )
        }

        //------------------------------------------------------ Mobile Number

        //------------------------------------------------------ Mobile Number
        if (phone != null) {
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )
        }

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Exception: " + e.message, Toast.LENGTH_SHORT).show()
        }

    }
}