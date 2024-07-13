package com.example.catlistapp.leaderboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.repository.CatsRepository
import com.example.catlistapp.profile.datastore.ProfileDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catsRepository: CatsRepository,
    private val usersDataStore: ProfileDataStore
) : ViewModel() {

    private val categ: Int = savedStateHandle.category
    private val _leaderboardState = MutableStateFlow(LeaderboardContract.LeaderboardState())
    val leaderboardState = _leaderboardState.asStateFlow()

    private fun setLeaderboardState (update: LeaderboardContract.LeaderboardState.() -> LeaderboardContract.LeaderboardState) =
        _leaderboardState.getAndUpdate(update)

    init {
        observe()
    }

    private fun observe() {

        viewModelScope.launch {
            setLeaderboardState { copy(isLoading = true) }
            try {
                val list = catsRepository.fetchAllResultsForCategory(category = categ)
                println("Lista za leaderboard: $list")
                setLeaderboardState { copy(results = list, nick = usersDataStore.data.value.nickname) }
            }catch (error: IOException){
                setLeaderboardState { copy(error = LeaderboardContract.LeaderboardState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setLeaderboardState { copy(isLoading = false) }
            }

        }
    }

}

inline val SavedStateHandle.category: Int
    get() = checkNotNull(get("category")) {"category is mandatory"}