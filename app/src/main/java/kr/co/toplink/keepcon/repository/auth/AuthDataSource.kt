package kr.co.toplink.keepcon.repository.auth

import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserResponse

interface AuthDataSource {
    //suspend fun signIn(user: User) : TokenResponse
    suspend fun signIn(user: User) : UserResponse
    suspend fun refreshToken(refreshToken : String) : TokenResponse
}