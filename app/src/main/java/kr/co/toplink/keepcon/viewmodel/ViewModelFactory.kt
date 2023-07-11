package kr.co.toplink.keepcon.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.repository.add.AddRemoteDataSource
import kr.co.toplink.keepcon.repository.add.AddRepository
import kr.co.toplink.keepcon.repository.fcm.FCMRemoteDataSource
import kr.co.toplink.keepcon.repository.fcm.FCMRepository
import kr.co.toplink.keepcon.repository.gifticon.GifticonRemoteDataSource
import kr.co.toplink.keepcon.repository.gifticon.GifticonRepository
import kr.co.toplink.keepcon.repository.map.MapRemoteDataSource
import kr.co.toplink.keepcon.repository.map.MapRepository
import kr.co.toplink.keepcon.repository.user.UserRemoteDataSource
import kr.co.toplink.keepcon.repository.user.UserRepository
import kr.co.toplink.keepcon.ui.edit.EditViewModel
import kr.co.toplink.keepcon.util.RetrofitUtil

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                val userRepo = UserRepository(UserRemoteDataSource(RetrofitUtil.userService))
                UserViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(GifticonViewModel::class.java) -> {
                val gifticonRepo =
                    GifticonRepository(GifticonRemoteDataSource(RetrofitUtil.gifticonService))
                GifticonViewModel(gifticonRepo) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                val mapRepo =
                    MapRepository(MapRemoteDataSource(RetrofitUtil.mapService))
                val gifticonRepo =
                    GifticonRepository(GifticonRemoteDataSource(RetrofitUtil.gifticonService))

                MapViewModel(gifticonRepo, mapRepo) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                val addRepo = AddRepository(AddRemoteDataSource(RetrofitUtil.addService))
                AddViewModel(addRepo) as T
            }
            modelClass.isAssignableFrom(FCMViewModel::class.java) -> {
                val fcmRepo = FCMRepository(FCMRemoteDataSource(RetrofitUtil.fcmService))
                FCMViewModel(fcmRepo) as T
            }
            modelClass.isAssignableFrom(EditViewModel::class.java) -> {
                EditViewModel() as T
            }
            modelClass.isAssignableFrom(PopupViewModel::class.java) -> {
                val gifticonRepo =
                    GifticonRepository(GifticonRemoteDataSource(RetrofitUtil.gifticonService))
                PopupViewModel(gifticonRepo) as T
            }
            modelClass.isAssignableFrom(MMSViewModel::class.java) ->{
                MMSViewModel(ApplicationClass().provideMMSRepository(context)) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel: ${modelClass.name}")
            }
        }
    }
}