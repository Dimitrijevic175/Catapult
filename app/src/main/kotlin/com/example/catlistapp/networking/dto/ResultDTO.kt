package com.example.catlistapp.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResultDTO(
    val nickname: String,
    val result: Float,
    val category: Int
)