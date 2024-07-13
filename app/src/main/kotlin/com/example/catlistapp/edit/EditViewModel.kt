package com.example.catlistapp.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.catlistapp.profile.datastore.ProfileData
import com.example.catlistapp.profile.datastore.ProfileDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore,
//    private val accountDetailsViewModel: AccountDetailsViewModel
) : ViewModel() {
    private val _state = MutableStateFlow(EditContract.EditState())
    val state = _state.asStateFlow()

    private val _events = MutableStateFlow<EditContract.EditEvent?>(null)
    val events = _events.asStateFlow()


    init {
        observeEvents()
        fetchData()
    }

    private fun setState(reducer: EditContract.EditState.() -> EditContract.EditState) {
        _state.update(reducer)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is EditContract.EditEvent.NicknameChanged -> setState { copy(nickname = event.nickname) }
                    is EditContract.EditEvent.FullNameChanged -> setState { copy(fullName = event.fullName) }
                    is EditContract.EditEvent.EmailChanged -> setState { copy(email = event.email) }
                    is EditContract.EditEvent.SaveClicked -> {
                        val state = _state.value
                        updateProfileData(state.fullName, state.nickname, state.email)
                        // Emitirajte ažurirane događaje
                        setEvent(EditContract.EditEvent.FullNameChanged(state.fullName))
                        setEvent(EditContract.EditEvent.NicknameChanged(state.nickname))
                        setEvent(EditContract.EditEvent.EmailChanged(state.email))
                    }
                    else -> Unit
                }
            }
        }
    }

    fun setEvent(event: EditContract.EditEvent) {
        _events.value = event
    }

    private fun fetchData() {
        viewModelScope.launch {
            val profileData = profileDataStore.data.first()
            setState {
                copy(
                    nickname = profileData.nickname,
                    fullName = profileData.fullName,
                    email = profileData.email
                )
            }
        }
    }

    private suspend fun updateProfileData(fullName: String, nickname: String, email: String) {
        val profile = ProfileData(fullName = fullName, nickname = nickname, email = email)
        profileDataStore.updateProfileData(profile)
        setState { copy(fullName = fullName, nickname = nickname, email = email) }
    }
}