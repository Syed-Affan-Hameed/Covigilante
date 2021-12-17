package com.syed.covigilante.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.syed.covigilante.R
import com.syed.covigilante.adapter.CenterRVAdapter
import com.syed.covigilante.adapter.StateRVAdapter
import com.syed.covigilante.modals.StateModal


class MainActivity : AppCompatActivity() {

    // Global World variables
    lateinit var txtwwinfo :TextView
    lateinit var txtcases :TextView
    lateinit var txtnumberofcases :TextView
    lateinit var txtrecovered :TextView
    lateinit var txtrecoveries:TextView
    lateinit var txtmortality :TextView
    lateinit var txtnumberofdeaths:TextView
    // Global Indian Variables
    lateinit var txtwwinfoindia : TextView
    lateinit var txtcasesindia :TextView
    lateinit var txtnumberofcasesindia:TextView
    lateinit var txtrecoveredindia:TextView
    lateinit var txtrecoveriesindia :TextView
    lateinit var txtmortalityindia :TextView
    lateinit var txtnumberofdeathsindia :TextView
    lateinit var pbprogressbar: ProgressBar
  //  lateinit var stateName:TextView  // id in the state_recycler_view file :- txtwwinfostaterv

    lateinit var stateinfoList: List<StateModal>
    lateinit var RVstates: RecyclerView
    lateinit var btncheckvaccineavailability:Button
//    lateinit var stateCases: TextView // txtnumberofcasesstaterv
//    lateinit var staterecoveries: TextView //txtrecoveriesstaterv
//    lateinit var statedeaths: TextView //txtnumberofdeathsstaterv
    lateinit var stateRVAdapter: StateRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtwwinfo=findViewById(R.id.txtwwinfo)
        txtnumberofcases=findViewById(R.id.txtnumberofcases)
        txtrecovered=findViewById(R.id.txtrecovered)
        txtrecoveries=findViewById(R.id.txtrecoveries)
        txtmortality=findViewById(R.id.txtmortality)
        txtnumberofdeaths=findViewById(R.id.txtnumberofdeaths)
        txtwwinfoindia=findViewById(R.id.txtwwinfoindia)
        txtcasesindia =findViewById(R.id.txtcasesindia)
        txtnumberofcasesindia=findViewById(R.id.txtnumberofcasesindia)
        txtrecoveredindia=findViewById(R.id.txtrecoveredindia)
        txtrecoveriesindia=findViewById(R.id.txtrecoveriesindia)
        txtmortalityindia=findViewById(R.id.txtmortalityindia)
        txtnumberofdeathsindia=findViewById(R.id.txtnumberofdeathsindia)
        RVstates=findViewById(R.id.RVstates)
        txtcases=findViewById(R.id.txtcases)
        btncheckvaccineavailability=findViewById(R.id.btncheckvaccineavailability)
        pbprogressbar=findViewById(R.id.pbprogressbar)
//        stateName=findViewById(R.id.txtwwinfostaterv)
//        staterecoveries=findViewById(R.id.txtrecoveriesstaterv)
//        stateCases=findViewById(R.id.txtnumberofcasesstaterv)
//        statedeaths=findViewById(R.id.txtnumberofdeathsstaterv)


        stateinfoList= ArrayList<StateModal>() // constructor is important

        getStateInfo()
        getWorldInfo()


        btncheckvaccineavailability.setOnClickListener {
            intent= Intent(this@MainActivity,CheckVaccineActivity::class.java)
            startActivity(intent)

        }




    }
    // Setting up methods for the fetching of data from the API(without Auth)

    // Method for Getting data for the states

    private fun getStateInfo(){
        val url= "https://api.rootnet.in/covid19-in/stats/latest"  // URL is always a string
        // to get the data from the api using Volley first we have to create a request queue
        pbprogressbar.setVisibility(View.VISIBLE)
        val queue = Volley.newRequestQueue(this@MainActivity)

        val request= JsonObjectRequest(Request.Method.GET,url ,null,{   response->
        try {
            val dataObj = response.getJSONObject("data")
            val summaryObj = dataObj.getJSONObject("summary") // these are keys so:- String
            val cases: Int = summaryObj.getInt("confirmedCasesIndian")
            val recoveries: Int = summaryObj.getInt("discharged")
            val mortality: Int = summaryObj.getInt("deaths")

            txtnumberofcasesindia.text = cases.toString()
            txtrecoveriesindia.text = recoveries.toString()
            txtnumberofdeathsindia.text = mortality.toString()

            // After this we get the covid  stats for each state using regional JSON Array from the API which is contained under the dataObj

            val regional = dataObj.getJSONArray("regional")
            // the list is to long so run a for loop and use the length of the JsonArray as the limit
            for (i in 0 until regional.length()) {
                val regionalObj =
                    regional.getJSONObject(i) // Here regional is JsonArray and regional.getJsonObject(i) is object within array, we write i because the jsonArray doesnt have all of its Jsonobjects named so they are accesed through their indexes

                val stateName = regionalObj.getString("loc")
                val stateName1: String = if (stateName=="Bihar****") "Bihar" else stateName
                val stateName2: String = if (stateName1=="Kerala***") "Kerala" else stateName1
                // See the data type carefully

                val Cases = regionalObj.getInt("totalConfirmed")
                val Recoveries = regionalObj.getInt("discharged")
                val Deaths = regionalObj.getInt("deaths")

                // add this data to the StateModals object

                val stateholder = StateModal(stateName2, Recoveries, Deaths, Cases)
                stateinfoList += stateholder   // stateinfoList is behaving like immutable list so +=  because add() not available for immutable lists

            }
                // to set this data to our recycler view pass this list to our Adapter class
                //setting up our recycler View


                stateRVAdapter= StateRVAdapter(stateinfoList)
                RVstates.layoutManager=LinearLayoutManager(this@MainActivity)
            RVstates.adapter=stateRVAdapter
            pbprogressbar.setVisibility(View.GONE)

        }
        catch (e:Exception){
            pbprogressbar.setVisibility(View.GONE)
            e.printStackTrace()

        }


        },
        { error->
            pbprogressbar.setVisibility(View.GONE)
            Toast.makeText(this, "API did not Respond",Toast.LENGTH_SHORT).show()

        })
        queue.add(request)

    }

    // Here is template for Volley GET copy this and use thank me later;)
//    val request= JsonObjectRequest(Request.Method.GET,url ,null,{   response->
//
//    },
//        { error->
//
//
//        })

    private fun getWorldInfo(){
            val url="https://corona.lmao.ninja/v3/covid-19/all"

            val queue=Volley.newRequestQueue(this)

        val request1=JsonObjectRequest(Request.Method.GET,url,null,{ response->

            // always put this in the try catch to handle JSON Exceptions
            try {
                val casesToday = response.getInt("cases")
                val deathsToday = response.getInt("deaths")
                val recoveriesToday = response.getInt("recovered")

                txtnumberofcases.text = casesToday.toString()
                txtrecoveries.text = recoveriesToday.toString()
                txtnumberofdeaths.text = deathsToday.toString()

            }
            catch (e1:Exception){
                e1.printStackTrace()  // errors like wrong key name and so on...

            }
        },{ error->
            Toast.makeText(this, "API did not Respond",Toast.LENGTH_SHORT).show()
        })
        queue.add(request1)


    }

}