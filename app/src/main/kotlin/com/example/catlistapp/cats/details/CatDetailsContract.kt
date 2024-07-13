package com.example.catlistapp.cats.details

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel

interface CatDetailsContract {

    data class CatDetailsState(

        val catId: String,
        val loading: Boolean = false,
        val cat: CatApiModel? = null,
    )
}