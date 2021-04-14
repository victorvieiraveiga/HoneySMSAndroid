package victor.veiga.honeysmsapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.campaign_item.view.*
import victor.veiga.honeysmsapp.R
import victor.veiga.honeysmsapp.infra.Helper

import victor.veiga.honeysmsapp.service.model.CampaignModel

import java.text.SimpleDateFormat
import java.util.*

class CampaignAdapter(val cModelList: CampaignModel, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {

    var mHelper = Helper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.campaign_item, parent, false)

        return CampaignViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        holder.bindData(cModelList.data[position])
    }

    override fun getItemCount(): Int {
        return cModelList.data.size
    }

    inner class CampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var mTextData: TextView = itemView.textData//itemView.findViewById(R.id.textData)
        var mTextNome: TextView = itemView.textNome//itemView.findViewById(R.id.textNome)


        init {
            itemView.setOnClickListener(this)
        }


        fun bindData(campaing: CampaignModel.DataCampaign) {
            this.mTextData.text =
                mHelper.DateFormat(campaing.dataExecucao)
            this.mTextNome.text = campaing.nome.toString()
        }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}