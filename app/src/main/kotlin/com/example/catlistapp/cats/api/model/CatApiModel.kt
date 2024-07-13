package com.example.catlistapp.cats.api.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "cats")
@Serializable
data class CatApiModel (
    @PrimaryKey
    val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val life_span: String,
    val alt_names: String? = "Nema alternativnog imena",
    @SerialName("adaptability") val adaptability: Int,
    @SerialName("affection_level") val affection_level: Int,
    @SerialName("stranger_friendly") val stranger_friendly: Int,
    @SerialName("dog_friendly") val dog_friendly: Int,
    @SerialName("energy_level") val energy_level: Int,
    @SerialName("social_needs") val social_needs: Int,
    @SerialName("health_issues") val health_issues: Int,
    val intelligence: Int,
    val rare: Int,
    val wikipedia_url: String?="ne postoji",
    var reference_image_id: String?=null,
    @Embedded
    val weight: Weight,

    )


@Serializable
data class Weight(
    val imperial: String,
    val metric : String
)


//{
//    "weight": {
//    "imperial": "7  -  10",
//    "metric": "3 - 5"
//},
//    "id": "abys",
//    "name": "Abyssinian",
//    "cfa_url": "http://cfa.org/Breeds/BreedsAB/Abyssinian.aspx",
//    "vetstreet_url": "http://www.vetstreet.com/cats/abyssinian",
//    "vcahospitals_url": "https://vcahospitals.com/know-your-pet/cat-breeds/abyssinian",
//    "temperament": "Active, Energetic, Independent, Intelligent, Gentle",
//    "origin": "Egypt",
//    "country_codes": "EG",
//    "country_code": "EG",
//    "description": "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
//    "life_span": "14 - 15",
//    "indoor": 0,
//    "lap": 1,
//    "alt_names": "",
//    "adaptability": 5,
//    "affection_level": 5,
//    "child_friendly": 3,
//    "dog_friendly": 4,
//    "energy_level": 5,
//    "grooming": 1,
//    "health_issues": 2,
//    "intelligence": 5,
//    "shedding_level": 2,
//    "social_needs": 5,
//    "stranger_friendly": 5,
//    "vocalisation": 1,
//    "experimental": 0,
//    "hairless": 0,
//    "natural": 1,
//    "rare": 0,
//    "rex": 0,
//    "suppressed_tail": 0,
//    "short_legs": 0,
//    "wikipedia_url": "https://en.wikipedia.org/wiki/Abyssinian_(cat)",
//    "hypoallergenic": 0,
//    "reference_image_id": "0XYvRd7oD"
//}