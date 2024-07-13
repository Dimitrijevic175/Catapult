package com.example.catlistapp.leaderboard.response

import kotlinx.serialization.Serializable


@Serializable
data class LeaderboardResponse(
    val result: LeaderboardApiModel,
    val ranking: Int
)

@Serializable
data class LeaderboardApiModel(
    val nickname: String = "",
    val result: Float = 0f
)
