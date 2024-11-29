package com.example.tertiaryprogramreccommender

import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun AptitudeTestActivity(navController: NavHostController, controller: AptitudeTestController) {
    val testTypes = controller.testTypes.value
    val questionsMap = controller.questionsMap.value
    val isLoading = controller.isLoading.value

    // Remember the current index and current test type to prevent unnecessary re-renders
    val currentIndex = remember { mutableStateOf(0) }
    val currentTestType = testTypes.getOrElse(currentIndex.value) { "" }

    // Ensure the answers map is preserved across recompositions
    val selectedAnswers =
        remember { mutableStateOf(mutableMapOf<String, MutableMap<String, String?>>()) }

    if (selectedAnswers.value[currentTestType] == null) {
        selectedAnswers.value[currentTestType] = mutableMapOf()
    }

    var remainingTime by remember { mutableStateOf(120 * 60) } // 1 hour in seconds

    // Only refresh the questions once
    LaunchedEffect(Unit) {
        controller.refreshQuestions()  // Ensure questions are shuffled at the start
    }




    // Calculate hours, minutes, and seconds
    val hours = remainingTime / 3600
    val minutes = (remainingTime % 3600) / 60
    val seconds = remainingTime % 60

    // Format time as hh:mm:ss
    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    val questions = questionsMap[currentTestType] ?: emptyList()
    val answeredQuestions = selectedAnswers.value[currentTestType]?.count { it.value != null } ?: 0

    val totalAnsweredQuestions = selectedAnswers.value.values.sumOf { answers ->
        answers.count { it.value != null }
    }
    val questioneach = questions.size

    val totalQuestions = questionsMap.values.sumOf { it.size }

// Calculate the overall progress as a fraction
    val overallProgress =
        if (totalQuestions > 0) totalAnsweredQuestions / totalQuestions.toFloat() else 0f

    val numOfTestTypes = selectedAnswers.value.count { testType ->
        val answers = selectedAnswers.value[testType.key]
        val questions = questionsMap[testType.key]
        answers != null && questions != null && answers.size == questions.size && answers.values.none { it.isNullOrBlank() }
    }

    val totalNumOfTestTypes = testTypes.size

    var showDialog by remember { mutableStateOf(false) }
    val allTestTypesAnswered = testTypes.all { testType ->
        selectedAnswers.value[testType]?.size == questionsMap[testType]?.size
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Incomplete Answers") },
            text = { Text("You need to answer all the questions before proceeding or submitting the test.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Get the current authenticated user's ID from Firebase
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Text(
                        text = "Loading Aptitude Test,\n please wait...",
                        fontFamily = Roboto,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            } else {

                // Countdown timer logic with auto-submit when time runs out
                LaunchedEffect(remainingTime) {
                    while (remainingTime > 0) {
                        delay(1000L)
                        remainingTime--
                    }
                    // When time runs out, automatically submit the answers
                    submitTest(selectedAnswers.value, controller, navController)
                }


                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentTestType,
                        fontFamily = Roboto,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Answered Test Types: $numOfTestTypes/$totalNumOfTestTypes",
                        fontFamily = Roboto,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Answered Questions: $answeredQuestions/$questioneach",
                        fontFamily = Roboto,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                   Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        LinearProgressIndicator(
                            progress = overallProgress, // Reflect overall test progress
                            modifier = Modifier
                                .height(10.dp)
                                .width(260.dp),
                            color = Color(0xFFFFFFFF),
                            trackColor = Color.DarkGray,
                        )
                       // Display the formatted time
                        Text(
                            text = " $timeText ",
                            fontFamily = Roboto,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )


                    }
                }



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MultipleChoiceQuestion(
                        questions,
                        selectedAnswers.value[currentTestType] ?: mutableMapOf()
                    ) { selectedOption, question ->
                        selectedAnswers.value[currentTestType]?.set(question, selectedOption)
                    }
                }

                Box(
                    modifier = Modifier
                        .height(85.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(color = Color(0xFF2962FF))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                val currentTestType = testTypes.getOrNull(currentIndex.value)
                                val questions = questionsMap[currentTestType] ?: emptyList()
                                val answers = selectedAnswers.value[currentTestType] ?: emptyMap()
                                if (answers.size == questions.size) {
                                    if (currentIndex.value < testTypes.size - 1) {
                                        currentIndex.value++
                                    }
                                } else {
                                    showDialog = true
                                }
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    if (currentIndex.value > 0) {
                                        currentIndex.value--
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.previous),
                                    contentDescription = "Previous",
                                    tint = Color.White
                                )
                                Text(
                                    text = "Prev",
                                    fontFamily = Roboto,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (allTestTypesAnswered) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    controller.checkAnswersAndStoreResults(
                                        userId = userId,
                                        selectedAnswers = selectedAnswers.value as Map<String, Map<String, String>>
                                    )
                                    navController.navigate("RecommendationActivity")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Submit",
                                    tint = Color.White
                                )
                                Text(
                                    text = "Submit",
                                    fontFamily = Roboto,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                val currentTestType = testTypes.getOrNull(currentIndex.value)
                                val questions = questionsMap[currentTestType] ?: emptyList()
                                val answers = selectedAnswers.value[currentTestType] ?: emptyMap()
                                if (answers.size == questions.size) {
                                    if (currentIndex.value < testTypes.size - 1) {
                                        currentIndex.value++
                                    }
                                } else {
                                    showDialog = true
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.next),
                                contentDescription = "Next",
                                tint = Color.White
                            )
                            Text(
                                text = "Next",
                                fontFamily = Roboto,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

fun submitTest(
    selectedAnswers: Map<String, Map<String, String?>>,
    controller: AptitudeTestController,
    navController: NavHostController
) {
    // Create a new map to store updated answers with default values ("0") for unanswered questions
    val updatedAnswers = selectedAnswers.toMutableMap()

    // Iterate through each test type
    updatedAnswers.keys.forEach { testType ->
        // Check if there are unanswered questions for the current testType
        val testAnswers = updatedAnswers[testType]?.toMutableMap() ?: mutableMapOf()

        // If there are unanswered questions, fill them with "0"
        testAnswers.forEach { (question, answer) ->
            if (answer == null) {
                testAnswers[question] = "0"  // Set unanswered questions to "0"
            }
        }

        // Update the map with the filled answers
        updatedAnswers[testType] = testAnswers
    }

    // Now submit the answers (with default "0" for unanswered questions)
    controller.checkAnswersAndStoreResults(
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
        selectedAnswers = updatedAnswers as Map<String, Map<String, String>>  // Cast to non-nullable Map
    )

    // Navigate to the next screen
    navController.navigate("RecommendationActivity")
}




@Composable
fun MultipleChoiceQuestion(
    questions: List<Question>,
    selectedAnswers: MutableMap<String, String?>,
    onOptionSelected: (String, String) -> Unit
) {
    val scrollState = rememberScrollState()

    OutlinedCard(
        modifier = Modifier
            .padding(16.dp)
            .size(width = 360.dp, height = 600.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .fillMaxSize()

                .verticalScroll(scrollState)

        ) {
            questions.forEach { question ->
                QuestionItem(
                    question = question,
                    selectedAnswer = selectedAnswers[question.questionText],
                    onOptionSelected = { option -> onOptionSelected(option, question.questionText) }
                )
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    selectedAnswer: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = question.questionText,
            fontFamily = Roboto,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )

        question.choices.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedAnswer == option,
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = option,
                    fontFamily = Roboto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}





