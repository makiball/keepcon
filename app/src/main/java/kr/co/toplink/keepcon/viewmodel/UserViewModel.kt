package kr.co.toplink.keepcon.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserDeleteRequest
import kr.co.toplink.keepcon.repository.user.UserRepository
import kr.co.toplink.keepcon.ui.common.Event
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userLv = MutableLiveData<Event<Int>>()
    val userLv: LiveData<Event<Int>> = _userLv

    fun signInKakao(user: User) {
        viewModelScope.launch {
            val user = userRepository.signInKakao(user)
            _user.value = user
        }
    }

    fun withdraw(user: UserDeleteRequest) {
        viewModelScope.launch {
            userRepository.withdraw(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            _user.value = userRepository.updateUser(user)
        }
    }

    fun getUserLv(){
        viewModelScope.launch {
            _userLv.value = Event(userRepository.getUserLv())
        }
    }
}