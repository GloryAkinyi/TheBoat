package com.wekesamabwi.theboat.repository

import com.wekesamabwi.theboat.data.UserDao
import com.wekesamabwi.theboat.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User) {
        userDao.registerUser(user)
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
}