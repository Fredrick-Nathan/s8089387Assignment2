package com.example.s8089387assignment2.data.repository

import com.example.s8089387assignment2.data.model.DashboardResponse
import com.example.s8089387assignment2.data.remote.NitApiService
import javax.inject.Inject

// wraps the dashboard API call so the ViewModel doesn't talk to Retrofit directly
class DashboardRepository @Inject constructor(
    private val apiService: NitApiService
) {
    // fetches the list of entities for the given keypass
    suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}