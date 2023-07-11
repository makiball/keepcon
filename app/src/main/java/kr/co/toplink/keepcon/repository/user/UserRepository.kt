package kr.co.toplink.keepcon.repository.user

import kr.co.toplink.keepcon.dto.TokenResponse
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserDeleteRequest

class UserRepository(private val remoteDataSource: UserRemoteDataSource) {
    suspend fun signInKakao(user: User): User {
        return remoteDataSource.signInKakao(user)
    }

    suspend fun withdraw(user: UserDeleteRequest) {
        return remoteDataSource.withdraw(user)
    }

    suspend fun updateUser(user: User): User {
        return remoteDataSource.updateUser(user)
    }

    suspend fun getUserLv(): Int{
        return remoteDataSource.getUserLv()
    }
}