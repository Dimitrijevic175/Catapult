package com.example.catlistapp.cats.list.model

data class CatListUiModel(
    val id: String,
    val name: String,
    val alt_names: String?,
    val description: String,
    val temperament: String
)