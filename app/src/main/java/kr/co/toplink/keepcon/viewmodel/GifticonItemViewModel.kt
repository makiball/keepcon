package kr.co.toplink.keepcon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.keepcon.dto.GifticonItem
import kr.co.toplink.keepcon.dto.MMSItem
import kr.co.toplink.keepcon.repository.gifticonitem.GifticonItemRepository
import kr.co.toplink.keepcon.repository.mms.MMSRepository

class GifticonItemViewModel(private val gifticonItemRepository: GifticonItemRepository) : ViewModel(){
    fun addGifticonItem(gifticonitem: GifticonItem){
        viewModelScope.launch {
            gifticonItemRepository.addGifticonItem(gifticonitem)
        }
    }

    fun getGifticonItem() {
        viewModelScope.launch {
            gifticonItemRepository.getGifticonItem()
        }
    }

    fun selectGifticonItem(barcodeNum: String) {
        viewModelScope.launch {
            gifticonItemRepository.selectGifticonItem(barcodeNum)
        }
    }

    fun deleteGifticonItem(barcodeNum: String) {
        viewModelScope.launch {
            gifticonItemRepository.deleteGifticonItem(barcodeNum)
        }
    }

    fun gifticondeleteAll() {
        viewModelScope.launch {
            gifticonItemRepository.gifticondeleteAll()
        }
    }
}