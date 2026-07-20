package com.example.s8089387assignment2.data.repository

import com.example.s8089387assignment2.data.model.LoginRequest
import com.example.s8089387assignment2.data.model.LoginResponse
import com.example.s8089387assignment2.data.remote.NitApiService
import javax.inject.Inject

// wraps the login API call so the ViewModel doesn't talk to Retrofit directly
class LoginRepository @Inject constructor(
    private val apiService: NitApiService
) {
    // makes the login request and returns the parsed response, or throws on failure
    suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(username, password))
    }
}