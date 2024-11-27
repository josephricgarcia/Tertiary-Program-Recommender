package com.example.tertiaryprogramreccommender

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AptitudeTestController(private val model: AptitudeTestModel) : ViewModel() {
    val testTypes = mutableStateOf<List<String>>(emptyList())
    val questionsMap = mutableStateOf<Map<String, List<Question>>>(emptyMap())
    val isLoading = mutableStateOf(true)

    init {
        fetchQuestions()
    }

    // Function to fetch questions from the model
    private fun fetchQuestions() {
        viewModelScope.launch {
            model.fetchQuestions { fetchedData ->
                // Shuffle questions for each test type before storing in questionsMap
                val shuffledData = fetchedData.mapValues { (_, questions) ->
                    questions.shuffled() // Shuffle the list of questions for each test type
                }

                // Update the state with shuffled data
                questionsMap.value = shuffledData
                testTypes.value = shuffledData.keys.toList()
                isLoading.value = false
            }
        }
    }

    // Function to refresh and shuffle the questions when the test is started again
    fun refreshQuestions() {
        viewModelScope.launch {
            model.fetchQuestions { fetchedData ->
                // Shuffle questions again every time the test starts
                val shuffledData = fetchedData.mapValues { (_, questions) ->
                    questions.shuffled() // Shuffle the list of questions for each test type
                }

                questionsMap.value = shuffledData
            }
        }
    }

    // Function to check answers and store results in Firebase
    fun checkAnswersAndStoreResults(userId: String, selectedAnswers: Map<String, Map<String, String>>) {
        testTypes.value.forEach { testType ->
            val questions = questionsMap.value[testType] ?: return@forEach
            val userAnswers = selectedAnswers[testType] ?: return@forEach

            var correctAnswers = 0

            questions.forEach { question ->
                val userAnswer = userAnswers[question.questionText]
                if (userAnswer == question.correctAnswer) {
                    correctAnswers++
                }
            }

            // Store the score in Firebase
            model.storeTestResults(userId, testType, correctAnswers, questions.size)
        }
    }
}

