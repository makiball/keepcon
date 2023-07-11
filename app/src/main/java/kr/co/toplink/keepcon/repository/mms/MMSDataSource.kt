package kr.co.toplink.keepcon.repository.mms

import kr.co.toplink.keepcon.dto.MMSItem

interface MMSDataSource {
    //add
    suspend fun addMMSItem(mmsItem : MMSItem)
    //get
    suspend fun getMMSItems() : List<MMSItem>
    // select date
    suspend fun selectDate(phoneNumber: String): String
    // update bitmap
    suspend fun updateDate(phoneNumber: String, beforeDate : String)
    // delete
    suspend fun deleteAll()
}