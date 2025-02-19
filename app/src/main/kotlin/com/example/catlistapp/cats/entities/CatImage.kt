package com.example.catlistapp.cats.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = "images")
@Serializable
data class CatGallery (
    @PrimaryKey
    val url: String,
    @Transient
    val id: String = ""
)