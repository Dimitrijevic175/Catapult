package com.example.catlistapp.profile.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataStore @Inject constructor(
    private val dataStore: DataStore<ProfileData>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val data = dataStore.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { dataStore.data.first() },
        )

    val isEmpty: Flow<Boolean> = data
        .map { it == ProfileData.EMPTY }
        .distinctUntilChanged()

    suspend fun updateProfileData(
        data: ProfileData
    ) {
        dataStore.updateData { data }
        Log.d("DATASTORE", "ProfileData = $data")
    }

    suspend fun updateFullName(
        fullName: String
    ): ProfileData {
        Log.d("DATASTORE", "New Full Name = $fullName")
        return dataStore.updateData {
            it.copy(fullName = fullName)
        }
    }

    suspend fun updateNickname(
        nickname: String
    ): ProfileData {
        Log.d("DATASTORE", "New Nickname = $nickname")
        return dataStore.updateData {
            it.copy(nickname = nickname)
        }
    }

    suspend fun updateEmail(
        email: String
    ): ProfileData {
        Log.d("DATASTORE", "New Email = $email")
        return dataStore.updateData {
            it.copy(email = email)
        }
    }
}