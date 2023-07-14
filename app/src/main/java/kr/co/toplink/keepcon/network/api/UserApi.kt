package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserDeleteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface UserApi {
    //네이버 로그인
    @POST("user/login/naver.php")
    suspend fun signInNaver(@Body user: User): User

    //카카오 로그인
    @POST("kakao.php")
    suspend fun signInKakao(@Body user: User): User

    @HTTP(method = "DELETE", path = "user/withdrawal", hasBody = true)
    suspend fun withdraw(@Body user: UserDeleteRequest)

    // 회원정보 변경
    @POST("user_update.php")
    suspend fun updateUser(@Body user: User): User

    // user level 받아오기
    @GET("getlevel.php")
    suspend fun getUserLv(): Int
}