package kr.co.toplink.keepcon.repository.auth

import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserResponse

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
//    suspend fun signIn(user : User) : TokenResponse{
//        return remoteDataSource.signIn(user)
//    }

    suspend fun signIn(user : User) : UserResponse{
        return remoteDataSource.signIn(user)
    }

    suspend fun refreshToken(refreshToken: String) : TokenResponse {
        return remoteDataSource.refreshToken(refreshToken)
    }
}