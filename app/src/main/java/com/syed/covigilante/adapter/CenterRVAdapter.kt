package com.syed.covigilante.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syed.covigilante.R
import com.syed.covigilante.modals.CenterRVModal

class CenterRVAdapter(private val centerList: List<CenterRVModal>): RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvcenterName:TextView=itemView.findViewById(R.id.tvcentername)
        val tvcenterlocation:TextView=itemView.findViewById(R.id.tvcenterlocation)
        val tvcentertimings:TextView=itemView.findViewById(R.id.tvcentertimings)
        val tvcvaccinename:TextView=itemView.findViewById(R.id.tvvaccinename)
        val tvvaccinefees:TextView=itemView.findViewById(R.id.tvvaccinefees)

        val tvagelimit:TextView=itemView.findViewById(R.id.tvagelimit)

        val tvvaccineavailability:TextView=itemView.findViewById(R.id.tvvaccineavailability)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.vaccine_pincode_details_rv_item,parent,false)
        return CenterRVViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center= centerList[position]

        holder.tvcenterName.text=center.centerName
        holder.tvcenterlocation.text=center.centerAddress
        holder.tvcentertimings.text= "From: ${center.centerFromTime}  To: ${center.centerToTime}"
        holder.tvcvaccinename.text=center.vaccineName
        holder.tvvaccinefees.text=center.fee_type
        holder.tvagelimit.text=" Age Limit: ${center.age_limit} " // no need to use toString() here because we are using the string template
        holder.tvvaccineavailability.text="Availability: ${center.availableCapacity}"

    }

    override fun getItemCount(): Int {
        return centerList.size
    }


}