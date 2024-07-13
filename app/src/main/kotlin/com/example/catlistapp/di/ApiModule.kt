package com.example.catlistapp.di

import com.example.catlistapp.cats.api.CatsApi
import com.example.catlistapp.cats.api.ResultsApi
import com.example.catlistapp.networking.serialization.AppJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader(
                        "x-api-key", "live_TzChV0VlkVs8H6rJbqYGX4s3RFXG8zbmxZTrMfKjyaq3T0ZgcsHjY5W8PCKf2tiG"
                    )
                    .build()
                it.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("CatApiRetrofit")
    fun provideCatApiRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Singleton
    @Provides
    @Named("ResultRetrofit")
    fun provideResultRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://rma.finlab.rs/")
        .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Singleton
    @Provides
    @Named("CatApi")
    fun provideCatApi(@Named("CatApiRetrofit") retrofit: Retrofit): CatsApi = retrofit.create(CatsApi::class.java)

    @Singleton
    @Provides
    @Named("ResultApi")
    fun provideResultApi(@Named("ResultRetrofit") retrofit: Retrofit): ResultsApi = retrofit.create(
        ResultsApi::class.java)
}
