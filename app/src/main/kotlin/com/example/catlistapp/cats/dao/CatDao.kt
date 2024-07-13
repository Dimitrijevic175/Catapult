package com.example.catlistapp.cats.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catlistapp.cats.api.CatsApi
import com.example.catlistapp.cats.api.model.CatApiModel
import kotlinx.coroutines.flow.Flow


@Dao
interface CatDao {

    @Query("select * from cats")
    fun getAllCats(): Flow<List<CatApiModel>>

    @Query("select * from cats where id = :id")
    fun getCatById(id: String): Flow<CatApiModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCats(cats: List<CatApiModel>)

    @Query("SELECT * FROM cats")
    suspend fun getAll(): List<CatApiModel>
}