package com.example.s8089387assignment2.data.model

// represents a single animal entity returned by the dashboard endpoint
data class AnimalEntity(
    val species: String,
    val scientificName: String,
    val habitat: String,
    val diet: String,
    val conservationStatus: String,
    val averageLifespan: Int,
    val description: String
)

data class DashboardResponse(
    val entities: List<AnimalEntity>,
    val entityTotal: Int
)