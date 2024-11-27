package com.example.tertiaryprogramreccommender

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Question(
    val questionText: String,
    val choices: List<String>,
    val correctAnswer: String
)

class AptitudeTestModel {
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child("aptitudeTest")

    fun fetchQuestions(callback: (Map<String, List<Question>>) -> Unit) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questionsData = mutableMapOf<String, List<Question>>()

                snapshot.children.forEach { testTypeSnapshot ->
                    val testType = testTypeSnapshot.key ?: return@forEach
                    val questionsList = mutableListOf<Question>()

                    testTypeSnapshot.children.forEach { questionSnapshot ->
                        val questionText = questionSnapshot.child("question").getValue(String::class.java)
                        val choices = questionSnapshot.child("choices").children.mapNotNull { it.getValue(String::class.java) }
                        val correctAnswer = questionSnapshot.child("correctAnswer").getValue(String::class.java)

                        if (questionText != null && correctAnswer != null && choices.isNotEmpty()) {
                            questionsList.add(Question(questionText, choices, correctAnswer))
                        }
                    }
                    questionsData[testType] = questionsList
                }

                callback(questionsData)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data: ${error.message}")
            }
        })
    }

    fun storeTestResults(userId: String, testType: String, correctAnswers: Int, totalQuestions: Int) {
        val resultsReference = database.reference.child("userScores").child(userId).child(testType)
        val scoreData = mapOf(
            "correctAnswers" to correctAnswers,
            "totalQuestions" to totalQuestions
        )
        resultsReference.setValue(scoreData)
    }
}