package kr.co.toplink.keepcon.repository.fcm

import kr.co.toplink.keepcon.network.response.FCMResponse

interface FCMDataSource {
    suspend fun sendNotification(body: FCMResponse)

    suspend fun uploadToken(token: String): String
    suspend fun broadCast(title: String, body: String): Int
    suspend fun sendMessageTo(token: String, title: String, body: String)
}