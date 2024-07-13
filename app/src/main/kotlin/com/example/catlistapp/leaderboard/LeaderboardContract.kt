package com.example.catlistapp.leaderboard

import com.example.catlistapp.networking.dto.ResultDTO

interface LeaderboardContract {

    data class LeaderboardState(
        val isLoading: Boolean = false,
        val error: DetailsError? = null,
        val results: List<ResultDTO> = emptyList(),
        val nick: String = ""
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}