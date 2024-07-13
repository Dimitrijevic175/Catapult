package com.example.catlistapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.catlistapp.profile.datastore.ProfileData
import com.example.catlistapp.profile.datastore.ProfileDataSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Singleton
    @Provides
    fun provideProfileDataStore(
        @ApplicationContext context: Context
    ): DataStore<ProfileData> {
        return DataStoreFactory.create(
            produceFile = { context.dataStoreFile(fileName = "profile.json") },
            serializer = ProfileDataSerializer(),
        )
    }
}