package com.example.catlistapp.database.di

import com.example.catlistapp.cats.dao.CatDao
import com.example.catlistapp.cats.dao.CatGalleryDao
import com.example.catlistapp.database.AppDataBase
import com.example.catlistapp.database.AppDataBaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //class in which you can add bindings for types that cannot be constructor injected
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    fun provideCatDao(dataBase: AppDataBase): CatDao = dataBase.catDao()
    @Provides
    fun provideCatGalleryDao(dataBase: AppDataBase): CatGalleryDao = dataBase.catGalleryDao()

    @Provides
    @Singleton
    fun provideDatabase(appDataBaseBuilder: AppDataBaseBuilder): AppDataBase = appDataBaseBuilder.build()
}