package com.example.catlistapp.quiz.ui

import android.os.CountDownTimer
import android.util.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.entities.CatGallery
import com.example.catlistapp.cats.entities.QuizResult
import com.example.catlistapp.cats.repository.CatsRepository
import com.example.catlistapp.networking.dto.ResultDTO
import com.example.catlistapp.profile.datastore.ProfileDataStore
import com.example.catlistapp.quiz.model.QuestionType
import com.example.catlistapp.quiz.model.QuizQuestion
import com.example.catlistapp.quiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val catsRepository: CatsRepository,
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.QuizState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizContract.QuizState.() -> QuizContract.QuizState) = _state.update(reducer)

    private val events = MutableSharedFlow<QuizContract.QuizEvent>()
    fun setEvent(event: QuizContract.QuizEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private var pausedTimeRemaining: Long = 0
    private var timer: CountDownTimer? = null

    init {
        observeEvents()
        loadQuizQuestions()
    }


    private fun loadQuizQuestions() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                fetchAndSetQuestions()
                initializeTimer()
            } catch (error: Exception) {
                Log.d("QuizViewModel", "Exception", error)
                setState { copy(loading = false, error = error) }
            } finally {
                Log.d("QUIZ", "Questions generated: ${state.value.questions}")
            }
        }
    }

    private suspend fun fetchAndSetQuestions() {
        withContext(Dispatchers.IO) {
            val questions = generateQuestions()
            setState { copy(questions = questions, loading = false) }
        }
    }

    private fun initializeTimer() {
        startQuizTimer()
    }


    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is QuizContract.QuizEvent.StopQuiz -> {
                        timer?.cancel()
                        setState { copy(showExitDialog = true) }
                    }

                    is QuizContract.QuizEvent.ContinueQuiz -> {
                        pausedTimeRemaining = state.value.timeRemaining
                        startQuizTimer(pausedTimeRemaining)
                        setState { copy(showExitDialog = false) }
                    }

                    is QuizContract.QuizEvent.FinishQuiz -> finishQuiz()
                    is QuizContract.QuizEvent.OptionSelected -> {

                        val selectedOption = state.value.questions[state.value.currentQuestionIndex].options[event.optionIndex]
                        submitAnswer(selectedOption)

                    }
                    is QuizContract.QuizEvent.PublishScore -> publish()
                    else -> {}
                }
            }
        }
    }


    private fun startQuizTimer(durationInMillis: Long = 300000) {
        timer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimeRemaining(millisUntilFinished)
            }

            override fun onFinish() {
                handleTimerFinish()
            }
        }
        timer?.start()
    }

    private fun updateTimeRemaining(timeRemaining: Long) {
        setState { copy(timeRemaining = timeRemaining) }
    }

    private fun handleTimerFinish() {
        setState {
            copy(
                questions = emptyList(),
                quizFinished = true,
                score = calculateScore().coerceAtMost(100.00f)
            )
        }
    }


    fun submitAnswer(option: String) {
        viewModelScope.launch {
            processAnswer(option)
            delay(1000)
            if (isLastQuestion()) {
                endQuiz()
            } else {
                moveToNextQuestion()
            }
        }
    }

    private fun processAnswer(option: String) {
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        val correctAnswer = currentQuestion.correctAnswer
        val isCorrect = option == correctAnswer

        setState { copy(selectedOption = option) }

        if (isCorrect) {
            incrementCorrectAnswers()
        }
    }

    private fun incrementCorrectAnswers() {
        setState { copy(correctAnswers = state.value.correctAnswers + 1) }
    }

    private fun isLastQuestion(): Boolean {
        return _state.value.currentQuestionIndex >= state.value.questions.size - 1
    }

    private fun moveToNextQuestion() {
        setState {
            copy(
                currentQuestionIndex = currentQuestionIndex + 1,
                selectedOption = null
            )
        }
    }

    private fun endQuiz() {
        setState {
            copy(
                questions = emptyList(),
                quizFinished = true,
                score = calculateScore().coerceAtMost(100.00f)
            )
        }
        timer?.cancel()
    }


    private fun finishQuiz() {
        viewModelScope.launch {
            Log.d("QUIZ", "Quiz completed with score: ${state.value.score}")

            val userProfile = profileDataStore.data.first()

            val quizResult = QuizResult(
                nickname = userProfile.nickname,
                score = state.value.score,
                date = Date().toString(),
            )
            quizRepository.insertQuizResult(quizResult)
        }
    }

    private fun publish() {
        viewModelScope.launch {
            Log.d("QUIZ", "Quiz completed with score: ${state.value.score}")

            val userProfile = profileDataStore.data.first()

            val leaderboardPost = ResultDTO(
                nickname = userProfile.nickname,
                result = state.value.score,
                category = 1
            )
            val response = catsRepository.postResult(
                leaderboardPost.nickname,
                leaderboardPost.result,
                leaderboardPost.category
            )

            val quizResult = QuizResult(
                nickname = userProfile.nickname,
                score = state.value.score,
                date = Date().toString(),
            )
            quizRepository.insertQuizResult(quizResult)
        }
    }

    private fun calculateScore(): Float {

        return (state.value.correctAnswers * 2.5 * (1 + ((state.value.timeRemaining / 1000) + 120) / 300)).toFloat()
    }


    private suspend fun generateQuestions(): List<QuizQuestion> {
        val cats = catsRepository.getAllCats()
        val questionCats = cats.shuffled().take(23)
        val questions = mutableListOf<QuizQuestion>()

        Log.d("QUIZ", "Question cats: $questionCats")

        val allTemperaments =
            cats.flatMap { it.temperament.split(",") }.map { it.trim() }.distinct()

        questionCats.forEach { cat ->
            var photos = catsRepository.getAllPhotos().filter { it.id == cat.id }.shuffled()

            if (photos.isEmpty()) {

                try {
                    cat.reference_image_id?.let { catsRepository.fetchPhoto(it, cat.id) }
                } catch (error: Exception) {
                    Log.d("QUIZ", "Error fetching photo", error)
                }
                photos = catsRepository.getAllPhotos().filter { it.id == cat.id }.shuffled()
            }
            Log.d("QUIZ", "Question Photos: $photos")

            val catTemperaments = cat.temperament.split(",").map { it.trim() }
            val otherTemperaments = allTemperaments.filterNot { it in catTemperaments }

            val randomIndex = (1..3).random()

            when (randomIndex) {
                1 -> generateCatNameQuestion(cat, photos, questions, cats)
                2 -> generateIncorrectTemperamentQuestion(
                    catTemperaments,
                    otherTemperaments,
                    photos,
                    questions
                )

                3 -> generateCorrectTemperamentQuestion(
                    catTemperaments,
                    otherTemperaments,
                    photos,
                    questions
                )
            }
        }
        Log.d("QUIZ", "Questions generated: $questions")
        return questions.take(20)
    }

    private fun generateCatNameQuestion(
        cat: CatApiModel,
        photos: List<CatGallery>,
        questions: MutableList<QuizQuestion>,
        cats: List<CatApiModel>,
    ) {
        if (photos.isNotEmpty()) {
            val incorrectAnswers = cats.filter { it.id != cat.id }
                .shuffled()
                .map { it.name }
                .distinct()
                .take(3)

            questions.add(
                QuizQuestion(
                    question = "Koja je rasa mačke?",
                    correctAnswer = cat.name,
                    incorrectAnswers = incorrectAnswers,
                    imageUrl = photos.first().url,
                    questionType = QuestionType.CAT_NAME,
                    options = (incorrectAnswers + cat.name).shuffled()
                )
            )
        }
    }

    private fun generateIncorrectTemperamentQuestion(

        catTemperaments: List<String>,
        otherTemperaments: List<String>,
        photos: List<CatGallery>,
        questions: MutableList<QuizQuestion>
    ) {
        if (photos.isNotEmpty()) {
            val incorrectAnswers = catTemperaments.shuffled().take(3)
            val correctAnswer = otherTemperaments.random()

            questions.add(
                QuizQuestion(
                    question = "Nadji uljeza! Koji temperament ne pripada macki sa slike?",
                    correctAnswer = correctAnswer,
                    incorrectAnswers = incorrectAnswers,
                    imageUrl = photos.first().url,
                    questionType = QuestionType.NOT_CAT_TEMPERAMENT,
                    options = (incorrectAnswers + correctAnswer).shuffled()
                )
            )
        }
    }

    private fun generateCorrectTemperamentQuestion(

        catTemperaments: List<String>,
        otherTemperaments: List<String>,
        photos: List<CatGallery>,
        questions: MutableList<QuizQuestion>
    ) {
        if (photos.isNotEmpty()) {
            val correctAnswer = catTemperaments.random()
            val incorrectAnswers = otherTemperaments.shuffled().take(3)

            questions.add(
                QuizQuestion(
                    question = "Koji temperament pripada macki sa slike?",
                    correctAnswer = correctAnswer,
                    incorrectAnswers = incorrectAnswers,
                    imageUrl = photos.first().url,
                    questionType = QuestionType.CAT_TEMPERAMENT,
                    options = (incorrectAnswers + correctAnswer).shuffled()
                )
            )
        }
    }
}