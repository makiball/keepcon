package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
//    @POST("user/login")
//    suspend fun signIn(@Body user: User) : TokenResponse

    @POST("user/login")
    suspend fun signIn(@Body user: User) : UserResponse

    @GET("user/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken : String?
    ) : TokenResponse
}