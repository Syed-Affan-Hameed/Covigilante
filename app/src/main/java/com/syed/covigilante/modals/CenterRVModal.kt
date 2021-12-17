package com.syed.covigilante.modals

data class CenterRVModal(

    val centerName:String,
    val centerAddress:String,
    val centerFromTime:String,
    val centerToTime: String,
    val fee_type: String,
    val age_limit: Int,
    val vaccineName: String,
    val availableCapacity :Int
){

}
