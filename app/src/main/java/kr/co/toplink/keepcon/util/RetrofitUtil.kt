package kr.co.toplink.keepcon.util

import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.network.api.*

class RetrofitUtil {
    companion object {
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val gifticonService = ApplicationClass.retrofit.create(GifticonApi::class.java)
        val mapService = ApplicationClass.retrofit.create(MapApi::class.java)
        val addService = ApplicationClass.retrofit.create(AddApi::class.java)
        val fcmService = ApplicationClass.retrofit.create(FCMApi::class.java)
        val authService = ApplicationClass.refreshRetrofit.create(AuthApi::class.java)
    }
}