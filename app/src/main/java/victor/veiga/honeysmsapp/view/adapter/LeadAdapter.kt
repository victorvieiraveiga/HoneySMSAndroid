package victor.veiga.honeysmsapp.view.adapter

import android.app.Application
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lead.*
import kotlinx.android.synthetic.main.lead_item.view.*
import victor.veiga.honeysmsapp.R
import victor.veiga.honeysmsapp.service.listener.APIListener
import victor.veiga.honeysmsapp.service.model.InteresseModel
import victor.veiga.honeysmsapp.service.model.InteresseRequest
import victor.veiga.honeysmsapp.service.model.LeadModel
import victor.veiga.honeysmsapp.view.LeadActivity
import victor.veiga.honeysmsapp.viewmodel.InteresseViewModel
import victor.veiga.honeysmsapp.viewmodel.InteresseViewModelNovo


class LeadAdapter (var listLead: LeadModel, application: Application): RecyclerView.Adapter<LeadAdapter.LeadViewHolder>() {


    var mInteresseViewModel = InteresseViewModelNovo(application)
    private var mLeadActivity = LeadActivity(application)

    var mListLead = MutableLiveData<LeadModel>()


public fun RecyclerViewAdapter(items: LeadModel) {

    this.listLead = items

    }
    public fun update(modelList:LeadModel){
        listLead = modelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadAdapter.LeadViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lead_item, parent, false)
        return LeadViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.binData(listLead.data[position])

        if (listLead.data[position].interesse == 1 ) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E3FFB3"))
            update(listLead)
        }
        if (listLead.data[position].interesse == 2) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFC7B9"))
            update(listLead)
        }
        if (listLead.data[position].interesse == 3) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ABF4FF"))
            update(listLead)
        }

    }

    override fun getItemCount(): Int {
        return  listLead.data.size
    }


    inner class LeadViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener,

        PopupMenu.OnMenuItemClickListener {

        var mTextNome : TextView = itemView.textNome
        var mTextTelefone : TextView = itemView.textTelefone
        var mMenuPop : ImageView = itemView.menuMore



        fun binData (lead : LeadModel.DataLead) {

            mTextNome.text = lead.nome
            mTextTelefone.text = lead.telefone
            mMenuPop.setOnClickListener (this)


        }

        override fun onClick(v: View) {
            showPopMenu(v)

        }

        fun showPopMenu (v: View) {
            val popMenu = PopupMenu(v.context,v)
            popMenu.inflate(R.menu.pop_menu)
            popMenu.setOnMenuItemClickListener (this)
            popMenu.show()
        }

//        fun updateList(onComplete: () -> Unit) {
//            val idCampanha = mSharedPreferences.getKey("idCampanhaSelected")
//            mInteresseViewModel.makeApiCall(idCampanha,object : APIListener<LeadModel>{
//                override fun onSuccess(model: LeadModel) {
//                    if(model.success == true) {
//                        //LeadAdapter(model,application)
//                        RecyclerViewAdapter(model)
//                        notifyDataSetChanged()
//                    }
//                }
//                override fun onFailure(str: String) {
//                    Log.d("Erro Interesse", "Erro Update")
//                }
//
//            })
//            onComplete()
//        }


        override fun onMenuItemClick(item: MenuItem?): Boolean {

            //val accesToken = mSharedPreferences.getKey("accessToken")
            var interesse = InteresseRequest ()
            interesse.id = listLead.data[adapterPosition].id
            interesse.interesse

            val onComplete: () -> Unit = {
                mInteresseViewModel.leads.value?.let {
                    update(it)
                    notifyDataSetChanged()
                }
            }

            val itemMenu = when(item!!.itemId) {

                R.id.ntInteresse -> {
                    //Faz o post via api
                    val interesseRequest = InteresseRequest()
                    interesseRequest.id = listLead.data[adapterPosition].id
                    interesseRequest.interesse = 2
                    mInteresseViewModel.updateInteresse(interesseRequest, onComplete)
                }
                    //  notifyDataSetChanged()

                //R.id.ntInteresse -> mLeadActivity.handlePostInteresse(listLead.data[adapterPosition].id,2,accesToken)
                R.id.ntInteresseCampanha -> {
                    val interesseRequest = InteresseRequest()
                    interesseRequest.id = listLead.data[adapterPosition].id
                    interesseRequest.interesse = 3
                    mInteresseViewModel.updateInteresse(interesseRequest, onComplete)
                }

                //R.id.ntInteresseCampanha -> mLeadActivity.handlePostInteresse(listLead.data[adapterPosition].id,3,accesToken)
                    //Log.d("Interesse","Não Tenho Interesse na Campanha ${listLead.data[adapterPosition].nome}")

                R.id.tInteresse ->  {
                    val interesseRequest = InteresseRequest()
                    interesseRequest.id = listLead.data[adapterPosition].id
                    interesseRequest.interesse = 1
                    mInteresseViewModel.updateInteresse(interesseRequest, onComplete)
                }
                // R.id.tInteresse -> mLeadActivity.handlePostInteresse(listLead.data[adapterPosition].id,1,accesToken)
                    //Log.d("Interesse","Tenho Interesse ${listLead.data[adapterPosition].nome}")
                else -> return false
            }
            notifyDataSetChanged()
            return true
        }
    }



}
