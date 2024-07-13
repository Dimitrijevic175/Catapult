package com.example.catlistapp.cats.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catlistapp.cats.entities.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult)

    @Query("SELECT * FROM quiz_results WHERE nickname = :nickname ORDER BY date DESC")
    fun getQuizResultsForUser(nickname: String): Flow<List<QuizResult>>

    @Query("SELECT MAX(score) FROM quiz_results WHERE nickname = :nickname")
    suspend fun getBestScore(nickname: String): Float?

    @Query("SELECT MIN(ranking) FROM quiz_results WHERE nickname = :nickname")
    suspend fun getBestRanking(nickname: String): Int?

    @Query("UPDATE quiz_results SET nickname = :newNickname WHERE nickname = :oldNickname")
    suspend fun updateNickname(oldNickname: String, newNickname: String)

}