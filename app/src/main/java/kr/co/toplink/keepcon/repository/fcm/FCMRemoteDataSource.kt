package kr.co.toplink.keepcon.repository.fcm

import kr.co.toplink.keepcon.network.api.FCMApi
import kr.co.toplink.keepcon.network.response.FCMResponse

class FCMRemoteDataSource(private val apiClient:FCMApi): FCMDataSource {
    override suspend fun sendNotification(body: FCMResponse) {
        return apiClient.sendNotification(body)
    }

    override suspend fun uploadToken(token: String): String {
        return apiClient.uploadToken(token)
    }

    override suspend fun broadCast(title: String, body: String): Int {
        return apiClient.broadCast(title, body)
    }

    override suspend fun sendMessageTo(token: String, title: String, body: String) {
        return apiClient.sendMessageTo(token, title, body)
    }
}