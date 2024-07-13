package com.example.catlistapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.profile.datastore.ProfileData
import com.example.catlistapp.profile.datastore.ProfileDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(LogInContract.LoginState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LogInContract.LoginState.() -> LogInContract.LoginState) = _state.update(reducer)

    private val events = MutableSharedFlow<LogInContract.LoginEvent>()
    fun setEvent(event: LogInContract.LoginEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is LogInContract.LoginEvent.OnNameChange -> {
                        setState { copy(name = event.name, isNameValid = event.name.isNotBlank()) }
                    }
                    is LogInContract.LoginEvent.OnNicknameChange -> {
                        setState { copy(nickname = event.nickname, isNicknameValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$"))) }
                    }
                    is LogInContract.LoginEvent.OnEmailChange -> {
                        setState { copy(email = event.email, isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) }
                    }
                    LogInContract.LoginEvent.OnCreateProfile -> {
                        if (state.value.isNameValid && state.value.isNicknameValid && state.value.isEmailValid) {
                            val newProfileData = ProfileData(
                                fullName = state.value.name,
                                nickname = state.value.nickname,
                                email = state.value.email
                            )
                            profileDataStore.updateProfileData(newProfileData)
                            setState { copy(isProfileCreated = true) }
                        }
                    }
                }
            }
        }
    }
}