package com.example.catlistapp.cats.gallery

import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel

interface CatGalleryContract {

    data class CatGalleryState(
        val catId: String,
        val loading: Boolean = false,
        val photos: List<String> = emptyList()
    )
}