package kr.co.toplink.keepcon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.co.toplink.keepcon.network.response.FCMResponse
import kr.co.toplink.keepcon.repository.fcm.FCMRepository
import kotlinx.coroutines.launch

class FCMViewModel(private val fcmRepository: FCMRepository): ViewModel() {
    var token = ""
        private set

    fun setToken(_token: String){
        token = _token
    }

    fun sendNotification(body: FCMResponse){
        viewModelScope.launch {
            fcmRepository.sendNotification(body)
        }
    }

    fun uploadToken(token: String){
        viewModelScope.launch {
            fcmRepository.uploadToken(token)
        }
    }

    fun broadCast(title: String, body: String){
        viewModelScope.launch {
            fcmRepository.broadCast(title, body)
        }
    }

    fun sendMessageTo(token: String, title: String, body: String){
        viewModelScope.launch {
            fcmRepository.sendMessageTo(token, title, body)
        }
    }
}