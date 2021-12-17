package com.syed.covigilante.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syed.covigilante.R
import com.syed.covigilante.modals.StateModal


class StateRVAdapter(private val stateinfolist:List<StateModal>): RecyclerView.Adapter<StateRVAdapter.StateRVViewHolder>() {
// itemView is the name of the view that recyclerView takes in for its functionality
    class StateRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val stateNameTV: TextView =itemView.findViewById(R.id.txtwwinfostaterv)
        val casesTV : TextView=itemView.findViewById(R.id.txtnumberofcasesstaterv)
        val mortalityTV : TextView =itemView.findViewById(R.id.txtnumberofdeathsstaterv)
        val recoveredTV :TextView=itemView.findViewById(R.id.txtrecoveriesstaterv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateRVViewHolder {
        // we have to inflate our layout file

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.state_recyclerview_item,parent,false)
        return StateRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StateRVViewHolder, position: Int) {
    // used to set data to each of the textViews
        val stateData=stateinfolist[position] // the incremental position of the list
        holder.stateNameTV.text=stateData.state
        holder.casesTV.text=stateData.cases.toString()
        holder.mortalityTV.text=stateData.deaths.toString()
        holder.recoveredTV.text=stateData.recovered.toString()


    }

    override fun getItemCount(): Int {
        // just returns the size of the State List
        return stateinfolist.size
    }

}