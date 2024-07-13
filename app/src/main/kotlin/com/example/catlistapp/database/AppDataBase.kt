package com.example.catlistapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.dao.CatDao
import com.example.catlistapp.cats.dao.CatGalleryDao
import com.example.catlistapp.cats.dao.QuizResultDao
import com.example.catlistapp.cats.entities.CatGallery
import com.example.catlistapp.cats.entities.QuizResult

@Database(
    entities = [
        CatApiModel::class, CatGallery::class, QuizResult::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)

abstract class AppDataBase: RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun catGalleryDao(): CatGalleryDao
    abstract fun quizResultDao() : QuizResultDao
}