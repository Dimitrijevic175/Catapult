package com.example.catlistapp.edit

interface EditContract {
    data class EditState(
        val loading: Boolean = false,
        val nickname: String = "",
        val fullName: String = "",
        val email: String = "",
    )

    sealed class EditEvent {
        data class NicknameChanged(val nickname: String) : EditEvent()
        data class FullNameChanged(val fullName: String) : EditEvent()
        data class EmailChanged(val email: String) : EditEvent()
        object SaveClicked : EditEvent()
//        data class SaveClicked(val nickname :String, val fullName: String, val email: String): EditEvent()
    }
}