package kr.co.toplink.keepcon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.keepcon.dto.Gifticon
import kr.co.toplink.keepcon.dto.GifticonItem
import kr.co.toplink.keepcon.dto.GifticonItemList
import kr.co.toplink.keepcon.dto.MMSItem
import kr.co.toplink.keepcon.repository.gifticonitem.GifticonItemRepository
import kr.co.toplink.keepcon.repository.mms.MMSRepository

class GifticonItemViewModel(private val gifticonItemRepository: GifticonItemRepository) : ViewModel(){

    private val _gifticons = MutableLiveData<List<GifticonItem>>()
    val gifticons: LiveData<List<GifticonItem>> = _gifticons

    fun addGifticonItem(gifticonitem: GifticonItem){
        viewModelScope.launch {
            gifticonItemRepository.addGifticonItem(gifticonitem)
        }
    }

    fun getGifticonItem() {
        viewModelScope.launch {
            val gifticons =  gifticonItemRepository.getGifticonItem()
            _gifticons.value = gifticons
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