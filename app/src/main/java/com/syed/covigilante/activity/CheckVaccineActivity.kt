package com.syed.covigilante.activity

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.syed.covigilante.R
import com.syed.covigilante.adapter.CenterRVAdapter
import com.syed.covigilante.modals.CenterRVModal
import java.lang.Exception

class CheckVaccineActivity : AppCompatActivity() {
    private lateinit var btnsearch: Button
    private lateinit var rvvaccinedetails: RecyclerView
    private lateinit var etpincode: EditText
    private lateinit var PBprogressbar: ProgressBar
    private lateinit var centerList: List<CenterRVModal>
    private lateinit var centerRVAdapter: CenterRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_vaccine)

        btnsearch = findViewById(R.id.btnsearch)
        rvvaccinedetails = findViewById(R.id.rvvaccinedetails)
        etpincode = findViewById(R.id.etpincode)
        PBprogressbar = findViewById(R.id.PBprogressbar)
        centerList = ArrayList<CenterRVModal>()



        btnsearch.setOnClickListener {
            val pinCode = etpincode.text.toString()
            this.window.decorView.clearFocus()
            val coorclick =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            coorclick.hideSoftInputFromWindow(it.windowToken,0)
            if (pinCode.length != 6) {
                Toast.makeText(this@CheckVaccineActivity, "Enter Valid Pincode", Toast.LENGTH_SHORT)
                    .show()
            } else {


                (centerList as ArrayList<CenterRVModal>).clear()
                // Setting values of Calendar Datatype
                // The following code is to get the
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        PBprogressbar.setVisibility(View.VISIBLE)

                        val datestring = "$dayOfMonth-${month + 1}-$year"

                        getvaccinecenterdetails(pinCode, datestring)

                    },
                    year, month, day

                )
                datePickerDialog.show()

            }


        }


    }


    fun getvaccinecenterdetails(pinCode: String, date: String) {
        // https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=110005&date-15-05-2021

        val url =
            "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val centerArray = response.getJSONArray("centers")
                if (centerArray.length() == 0) {
                    Toast.makeText(
                        this,
                        "There are no centers mentioned for this pincode",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                PBprogressbar.setVisibility(View.GONE)
                for (i in 0 until centerArray.length()) {
                    val center = centerArray.getJSONObject(i)
                    val centerName = center.getString("name")
                    val centerAddress = center.getString("address")
                    val centerFromTime = center.getString("from")
                    val centerToTime = center.getString("to")
                    val fee = center.getString("fee_type")
                    // the available capacity, vaccine_name, age_limit are all in the jsonarray inside the center object
                    val sessionObj = center.getJSONArray("sessions").getJSONObject(0)

                    val availableCapacity = sessionObj.getInt("available_capacity")
                    val ageLimit = sessionObj.getInt("min_age_limit")
                    val vaccineName = sessionObj.getString("vaccine")

                    // creating an object of our CenterRVAdapter Modal class
                    val centerRVObject = CenterRVModal(
                        centerName,
                        centerAddress,
                        centerFromTime,
                        centerToTime,
                        fee,
                        ageLimit,
                        vaccineName,
                        availableCapacity
                    )
                    centerList = centerList + centerRVObject
                }

                centerRVAdapter = CenterRVAdapter(centerList)
                rvvaccinedetails.layoutManager = LinearLayoutManager(this@CheckVaccineActivity)
                rvvaccinedetails.adapter = centerRVAdapter
                centerRVAdapter.notifyDataSetChanged()
//                    rvvaccinedetails.apply {
//                        adapter = centerRVAdapter
//                        layoutManager = LinearLayoutManager(this@CheckVaccineActivity) }


            } catch (e: Exception) {
                PBprogressbar.setVisibility(View.GONE)
                e.printStackTrace()
            }

        }, { error ->
            PBprogressbar.setVisibility(View.GONE)
            Toast.makeText(this, "No data Received from the server", Toast.LENGTH_SHORT).show()

        })
        queue.add(request)

    }





}