package com.example.s8089387assignment2.data.model

import com.google.gson.JsonObject

data class DashboardResponse(
    val entities: List<JsonObject>,
    val entityTotal: Int
)