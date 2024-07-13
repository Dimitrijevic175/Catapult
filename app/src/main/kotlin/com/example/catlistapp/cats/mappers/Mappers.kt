package com.example.catlistapp.cats.mappers

import com.example.catlistapp.cats.api.model.CatApiGalleryModel
import com.example.catlistapp.cats.entities.CatGallery

fun CatApiGalleryModel.asPhotoDbModel(catId: String): CatGallery {
    return CatGallery(

        url = this.url,
        id = catId
    )
}