package kr.co.toplink.keepcon.repository.fcm

import kr.co.toplink.keepcon.network.response.FCMResponse

class FCMRepository(private val remoteDataSource:FCMRemoteDataSource) {
    suspend fun sendNotification(body: FCMResponse){
        return remoteDataSource.sendNotification(body)
    }

    suspend fun uploadToken(token: String): String{
        return remoteDataSource.uploadToken(token)
    }

    suspend fun broadCast(title: String, body: String): Int {
        return remoteDataSource.broadCast(title, body)
    }

    suspend fun sendMessageTo(token: String, title: String, body: String){
        return remoteDataSource.sendMessageTo(token, title, body)
    }
}