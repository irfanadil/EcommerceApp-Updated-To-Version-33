package com.ecommerce.testapp

import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val testAPI: TestAPI){

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val loggedInUser = testAPI.login(LoginRequest(username, password))
            Result.Success(loggedInUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun fetchUserList(): Result<UserListResult> {
        return try {
             Result.Success(testAPI.fetchUserList())
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}