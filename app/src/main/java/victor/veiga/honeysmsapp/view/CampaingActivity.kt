package victor.veiga.honeysmsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_campaing.*
import victor.veiga.honeysmsapp.R
import victor.veiga.honeysmsapp.R.color.title_color
import victor.veiga.honeysmsapp.infra.Helper
import victor.veiga.honeysmsapp.infra.SecurityPreferences
import victor.veiga.honeysmsapp.service.model.CampaignModel
import victor.veiga.honeysmsapp.service.model.CampaignParcelableModel
import victor.veiga.honeysmsapp.view.adapter.CampaignAdapter
import victor.veiga.honeysmsapp.viewmodel.CampaignViewModel


class CampaingActivity : AppCompatActivity() , CampaignAdapter.OnItemClickListener{

    private lateinit var mViewModel: CampaignViewModel
    lateinit var id: CampaignModel
    val helper = Helper()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //actionbar
//        val actionbar = supportActionBar
//        //set actionbar title
//        actionbar!!.title = "New Activity"
//        //set back button
//        actionbar.setDisplayHomeAsUpEnabled(true)
//        //actionbar.setDisplayHomeAsUpEnabled(true)

        var mSecurityPreferences = SecurityPreferences(this)
        supportActionBar?.title = "Olá, ${mSecurityPreferences.getKey("userEmail")}"


        mViewModel =  ViewModelProvider(this).get(CampaignViewModel::class.java)

        //val actionBar = supportActionBar
        //actionBar!!.title = "Campanhas"

        setContentView(R.layout.activity_campaing)
        observe()
        loadCampaign()
        if (supportActionBar != null) {
            supportActionBar?.setBackgroundDrawable(getResources().getDrawable(R.color.title_color))
        }

    }

    private fun observe () {
        mViewModel.campaigns.observe(this, Observer {
            if (it != null) {
                if (it.success) {
                    id = it
                    reciclerCampaign.adapter = CampaignAdapter(it,this)
                    reciclerCampaign.layoutManager = LinearLayoutManager(this)
                }else {
                    Toast.makeText(this, "Não ha campanhas.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Não ha campanhas.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCampaign() {
        mViewModel.listCampaign()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //Chama tela de lead
    override fun onItemClick(position: Int) {

        val dataCampaignParc = CampaignParcelableModel ()

        dataCampaignParc.dataExecucao =  helper.DateFormat(id.data[position].dataExecucao)
        dataCampaignParc.id = id.data[position].id
        dataCampaignParc.nome = id.data[position].nome

        val intent = Intent(this, LeadActivity()::class.java)
        intent.putExtra("Campaign", dataCampaignParc)
        startActivity(intent)
    }

}


