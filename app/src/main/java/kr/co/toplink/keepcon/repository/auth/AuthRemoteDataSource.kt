package kr.co.toplink.keepcon.repository.auth

import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserResponse
import kr.co.toplink.keepcon.network.api.AuthApi

class AuthRemoteDataSource(private val apiClient : AuthApi) : AuthDataSource{
//    override suspend fun signIn(user: User): TokenResponse {
//        return apiClient.signIn(user)
//    }
    override suspend fun signIn(user: User): UserResponse {
        return apiClient.signIn(user)
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
        return apiClient.refreshToken(refreshToken)
    }
}