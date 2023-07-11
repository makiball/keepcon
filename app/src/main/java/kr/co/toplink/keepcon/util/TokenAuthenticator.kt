package kr.co.toplink.keepcon.util

import android.content.Context
import android.util.Log
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.network.api.AuthApi
import kr.co.toplink.keepcon.repository.auth.AuthRemoteDataSource
import kr.co.toplink.keepcon.repository.auth.AuthRepository
import kr.co.toplink.keepcon.repository.user.UserRemoteDataSource
import kr.co.toplink.keepcon.repository.user.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.*

class TokenAuthenticator: Authenticator {

    companion object {
        private val TAG = TokenAuthenticator::class.java.simpleName
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d(TAG, "authenticatedddd: ${response.code}")
        /*if (response.code == 401) {
            if (!ApplicationClass.sharedPreferencesUtil.refreshToken.isNullOrEmpty()) {
                val authRepo = AuthRepository(AuthRemoteDataSource(RetrofitUtil.authService))

                Log.d(TAG, "authenticate: ${ApplicationClass.sharedPreferencesUtil.refreshToken}")
                runBlocking {
                    val res = authRepo.refreshToken(ApplicationClass.sharedPreferencesUtil.refreshToken!!)
                    ApplicationClass.sharedPreferencesUtil.accessToken = res.acessToken
                    ApplicationClass.sharedPreferencesUtil.refreshToken = res.refreshToekn
                }
            }
        }*/
        val authRepo = AuthRepository(AuthRemoteDataSource(RetrofitUtil.authService))
        runBlocking {
            Log.d(TAG, "authenticatedddd: ")
            val res = authRepo.refreshToken(ApplicationClass.sharedPreferencesUtil.refreshToken!!)
            ApplicationClass.sharedPreferencesUtil.accessToken = res.acessToken
            ApplicationClass.sharedPreferencesUtil.refreshToken = res.refreshToekn
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${ApplicationClass.sharedPreferencesUtil.accessToken?:""}")
            .build()
    }
}
