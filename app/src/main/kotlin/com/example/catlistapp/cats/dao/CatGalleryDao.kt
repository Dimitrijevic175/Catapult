package com.example.catlistapp.cats.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catlistapp.cats.entities.CatGallery
import kotlinx.coroutines.flow.Flow


@Dao
interface CatGalleryDao {

    @Query("select url from images where id= :id")
    fun getAllImagesForId(id: String): Flow<List<String>>

    @Query("select url from images where url = :url")
    fun getImageByUrl(url: String): Flow<String>

    @Query("SELECT * FROM images")
    suspend fun getAll(): List<CatGallery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: CatGallery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGalleryCats(cats: List<CatGallery>)


    @Query("SELECT * FROM images WHERE id = :catId")
    fun getPhotosByCatId(catId: String): Flow<List<CatGallery>>
}