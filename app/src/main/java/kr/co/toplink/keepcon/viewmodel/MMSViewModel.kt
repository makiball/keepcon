package kr.co.toplink.keepcon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.co.toplink.keepcon.dto.MMSItem
import kr.co.toplink.keepcon.repository.mms.MMSRepository
import kotlinx.coroutines.launch

class MMSViewModel(private val mmsRepository: MMSRepository) : ViewModel(){
    fun addMMSItem(item: MMSItem){
        viewModelScope.launch {
            mmsRepository.addMMSItem(item)
        }
    }

    fun getMMSItems(){
        viewModelScope.launch {
            mmsRepository.getMMSItems()
        }
    }

    fun selectDate(phoneNumber: String){
        viewModelScope.launch {
            mmsRepository.selectDate(phoneNumber)
        }
    }

    fun updateDate(phoneNumber: String, beforeDate : String){
        viewModelScope.launch {
            mmsRepository.updateDate(phoneNumber, beforeDate)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            mmsRepository.deleteAll()
        }
    }
}