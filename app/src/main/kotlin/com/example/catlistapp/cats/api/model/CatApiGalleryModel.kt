package com.example.catlistapp.cats.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CatApiGalleryModel(

    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)
