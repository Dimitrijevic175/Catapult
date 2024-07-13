package com.example.catlistapp.accountDetails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.catlistapp.profile.datastore.ProfileDataStore
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catlistapp.edit.EditContract
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(EditContract.EditState())
    val state = _state.asStateFlow()
    private fun setState(reducer: EditContract.EditState.() -> EditContract.EditState) = _state.update(reducer)

    private val _events = MutableStateFlow<EditContract.EditEvent?>(null)
    val events = _events.asStateFlow()

    fun setEvent(event: EditContract.EditEvent) {
        _events.value = event
    }

    init {
        fetchProfileData()
        observeEvents()
        observeProfileData()
    }

    fun fetchProfileData() {
        viewModelScope.launch {
            val profileData = profileDataStore.data.first()
            setState {
                copy(
                    fullName = profileData.fullName,
                    nickname = profileData.nickname,
                    email = profileData.email
                )
            }
        }
    }

    private fun observeProfileData() {
        viewModelScope.launch {
            profileDataStore.data.collect { profileData ->
                setState {
                    copy(
                        fullName = profileData.fullName,
                        nickname = profileData.nickname,
                        email = profileData.email,
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is EditContract.EditEvent.FullNameChanged -> setState { copy( fullName = event.fullName) }
                    is EditContract.EditEvent.NicknameChanged -> setState { copy( nickname = event.nickname) }
                    is EditContract.EditEvent.EmailChanged -> setState { copy( email = event.email) }

                    is EditContract.EditEvent.SaveClicked -> {
                        when(event){
                            is EditContract.EditEvent.FullNameChanged -> setState { copy( fullName = event.fullName) }
                            is EditContract.EditEvent.NicknameChanged -> setState { copy( nickname = event.nickname) }
                            is EditContract.EditEvent.EmailChanged -> setState { copy( email = event.email) }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun eventPublisher(event : EditContract.EditEvent) {
        _events.value = event
    }



}

