package com.example.catlistapp.quiz.repository

import androidx.room.*
import com.example.catlistapp.cats.entities.QuizResult
import com.example.catlistapp.database.AppDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class QuizRepository @Inject constructor(
    private val database: AppDataBase
){
    suspend fun insertQuizResult(result: QuizResult) {
        database.quizResultDao().insertQuizResult(result)
    }

    fun getAllQuizResults(nickname: String) = database.quizResultDao().getQuizResultsForUser(nickname)

    suspend fun getBestScore(userId: String) = database.quizResultDao().getBestScore(userId)

    suspend fun getBestPosition(userId: String) = database.quizResultDao().getBestRanking(userId)

    suspend fun updateNickname(oldNickname: String, newNickname: String) {
        database.quizResultDao().updateNickname(oldNickname, newNickname)
    }

}