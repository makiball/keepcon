package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.BuildConfig
import kr.co.toplink.keepcon.network.response.FCMResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FCMApi {
    @Headers("Authorization: key=${BuildConfig.FIREBASE_GOOGLE_CLOUD_KEY}", "Content-Type:application/json")
    @POST("fcm/send.php")
    suspend fun sendNotification(@Body body: FCMResponse)

    @POST("token.php")
    suspend fun uploadToken(@Query("token") token: String): String
    @POST("broadcast.php")
    suspend fun broadCast(@Query("title") title: String, @Query("body") body: String): Int
    @POST("sendMessageTo.php")
    suspend fun sendMessageTo(@Query("token")token:String, @Query("title")title:String, @Query("body")body:String)
}