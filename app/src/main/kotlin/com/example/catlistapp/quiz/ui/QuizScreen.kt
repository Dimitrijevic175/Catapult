package com.example.catlistapp.quiz.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.example.catlistapp.quiz.model.QuizQuestion


fun NavGraphBuilder.quiz(
    route: String,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit
) = composable(
    route = route,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) { navBackStackEntry ->

    val quizViewModel: QuizViewModel = hiltViewModel(navBackStackEntry)

    val state = quizViewModel.state.collectAsState()

    QuizScreen(
        state = state.value,
        eventPublisher = {
            quizViewModel.setEvent(it)
        },
        //zak
//        onOptionSelected = { option -> quizViewModel.submitAnswer(option) },
        onQuizCompleted = onQuizCompleted,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizContract.QuizState,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
    //zak
//    onOptionSelected: (String) -> Unit,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit
) {
    if (state.showExitDialog) {
        AlertDialog(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { eventPublisher(QuizContract.QuizEvent.StopQuiz) },
            title = {
                Text(
                    "Exit Quiz",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    "Are you sure you want to exit the quiz?",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            confirmButton = {
                Button(
                    onClick = { onClose() },
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { eventPublisher(QuizContract.QuizEvent.ContinueQuiz) },
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("No")
                }
            }
        )


    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quiz",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { eventPublisher(QuizContract.QuizEvent.StopQuiz) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    Text(
                        text = "Time: ${state.timeRemaining / 1000 / 60} min ${state.timeRemaining / 1000 % 60} sec",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->

            Box(
                modifier = Modifier
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                if (state.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.questions.isNotEmpty()) {
                    Crossfade(targetState = state.currentQuestionIndex, label = "") { index ->
                        val question = state.questions[index]
                        QuestionScreen(
                            state = state,
                            question = question,
                            eventPublisher = eventPublisher
                            //zak
                            //prosldei state i evntp
//                            onOptionSelected = onOptionSelected
                        )
                    }
                } else if (state.quizFinished){
                    ResultScreen(
                        score = state.score,
                        onFinish = onClose,
                        eventPublisher = { eventPublisher(it) }
                    )
                } else {
                    onQuizCompleted()
                    Log.d("QUIZ", "No questions available")
                }
            }
        }
    )
}

@Composable
fun ProgressBarOurs(
    index: Int,
    size: Int
) {
    val progress by animateFloatAsState(
        targetValue = (index + 1) / size.toFloat(),
        label = ""
    )
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = { progress },
    )
}

@Composable
fun QuestionScreen(
    state : QuizContract.QuizState,
    question: QuizQuestion,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
//    onOptionSelected: (String) -> Unit
) {

//    //zak obe
//    val quizViewModel: QuizViewModel = hiltViewModel()
//    val state = quizViewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BackHandler {
//            quizViewModel.setEvent(QuizEvent.StopQuiz)
            eventPublisher(QuizContract.QuizEvent.StopQuiz)
        }

        ProgressBarOurs(index = state.currentQuestionIndex, size = state.questions.size)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question.question,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                if (question.imageUrl != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .border(2.dp, MaterialTheme.colorScheme.secondary)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(question.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .border(2.dp, Color.Transparent, RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val options = question.options
            val chunkedOptions = options.chunked(2)
            chunkedOptions.forEach { chunk ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    chunk.forEach { option ->
                        val buttonColor = when {
                            state.selectedOption == null -> MaterialTheme.colorScheme.surface
                            option == state.selectedOption && option == state.questions[state.currentQuestionIndex].correctAnswer -> Color.Green
                            option == state.selectedOption && option != state.questions[state.currentQuestionIndex].correctAnswer -> Color.Red
                            option != state.selectedOption && option == state.questions[state.currentQuestionIndex].correctAnswer -> Color.Green
                            else -> MaterialTheme.colorScheme.surface
                        }
                        Button(
                            onClick = {
                                if (state.selectedOption == null)
//                                    onOptionSelected(option)
                                    eventPublisher(QuizContract.QuizEvent.OptionSelected(options.indexOf(option)))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                                contentColor = MaterialTheme.colorScheme.secondary
                            ),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ResultScreen(
    score: Float,
    onFinish: () -> Unit,
    eventPublisher: (uiEvent: QuizContract.QuizEvent) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Your Score",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "$score",
                        style = TextStyle(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        Button(
                            onClick = {
                                eventPublisher(QuizContract.QuizEvent.PublishScore(score))
                                onFinish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .width(140.dp)
                        ) {
                            Text(
                                text = "Publish",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp,
                                ),
                            )
                        }
                        Button(
                            onClick = {
                                eventPublisher(QuizContract.QuizEvent.FinishQuiz)
                                onFinish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .width(140.dp)
                        ) {
                            Text(
                                text = "Finish",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp,
                                ),
                            )
                        }
                    }
                }
            }
        }
    )
}