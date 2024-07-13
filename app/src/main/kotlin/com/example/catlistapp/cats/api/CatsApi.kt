package com.example.catlistapp.cats.api

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.api.model.CatApiGalleryModel
import com.example.catlistapp.cats.entities.CatGallery
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {


    @GET("breeds")
    suspend fun getAllCats(): List<CatApiModel>

    @GET("breeds/{breed_id}")
    suspend fun getCat(
        @Path("breed_id") id: String,
    ): CatApiModel

    @GET("images/{photoId}")
    suspend fun fetchPhotoById(
        @Path("photoId") photoId: String,
    ): CatApiGalleryModel


    @GET("images/search?limit=20")
    suspend fun getAllCatsPhotos(@Query("breed_ids") id: String): List<CatGallery>

}