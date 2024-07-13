package com.example.catlistapp.profile

interface LogInContract {

    data class LoginState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val isNameValid: Boolean = false,
        val isNicknameValid: Boolean = false,
        val isEmailValid: Boolean = false,
        val isProfileCreated: Boolean = false
    )

    sealed class LoginEvent {
        data class OnNameChange(val name: String) : LoginEvent()
        data class OnNicknameChange(val nickname: String) : LoginEvent()
        data class OnEmailChange(val email: String) : LoginEvent()
        data object OnCreateProfile : LoginEvent()
    }

}