package com.example.catlistapp.quiz.model

data class QuizQuestion(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val options: List<String>,
    val imageUrl: String? = null,
    val questionType: QuestionType
)
