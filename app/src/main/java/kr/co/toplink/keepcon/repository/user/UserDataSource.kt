package kr.co.toplink.keepcon.repository.user

import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.dto.UserDeleteRequest

interface UserDataSource {
    suspend fun signInNaver(user: User): User
    suspend fun signInKakao(user: User): User
    suspend fun withdraw(user: UserDeleteRequest)
    suspend fun updateUser(user: User): User
    suspend fun getUserLv(): Int
}