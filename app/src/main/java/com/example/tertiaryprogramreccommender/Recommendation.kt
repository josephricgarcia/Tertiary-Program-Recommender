package com.example.tertiaryprogramreccommender

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Recommendation {
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child("userScores")
    private val auth = FirebaseAuth.getInstance()

    fun fetchUserScores(callback: (Map<String, Pair<Int, Int>>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(emptyMap())
            return
        }

        databaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        callback(emptyMap())
                        return
                    }

                    val scores = mutableMapOf<String, Pair<Int, Int>>()
                    snapshot.children.forEach { testSnapshot ->
                        val testType = testSnapshot.key ?: return@forEach
                        val correctAnswers = testSnapshot.child("correctAnswers").getValue(Int::class.java) ?: 0
                        val totalQuestions = testSnapshot.child("totalQuestions").getValue(Int::class.java) ?: 0

                        scores[testType] = Pair(correctAnswers, totalQuestions)
                    }
                    callback(scores)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyMap())
                }
            })
    }

    fun programRanking(
        scores: Map<String, Pair<Int, Int>>,
        callback: (List<Pair<String, Double>>) -> Unit
    ) {
        if (scores.isEmpty()) {
            callback(emptyList())
            return
        }

        // Define the criteria for each program (passing percentage and required test types)
        val programs = mapOf(
            "BSCS" to Pair(50, listOf("Logical Reasoning", "Mathematical Aptitude")),
            "BSHM" to Pair(75, listOf("Customer Service Skills", "Business Management")),
            "BSES-CRM" to Pair(75, listOf("Environmental Awareness", "Geographical Knowledge")),
            "BSEd-Math" to Pair(80, listOf("Mathematical Reasoning", "Teaching Aptitude")),
            "BTLed-HE" to Pair(75, listOf("Practical Knowledge", "Home Economics")),
            "BEED" to Pair(75, listOf("Educational Psychology", "Teaching Methods"))
        )

        val passedPrograms = mutableListOf<Pair<String, Double>>()

        // Check each program's criteria and compare to the user's scores
        programs.forEach { (programName, criteria) ->
            val passingPercentage = criteria.first
            val testTypes = criteria.second

            // Sum up the user's scores for the required test types
            val (correctAnswersSum, totalQuestionsSum) = testTypes.fold(0 to 0) { acc, testType ->
                val score = scores[testType] ?: Pair(0, 0)
                acc.first + score.first to acc.second + score.second
            }

            if (totalQuestionsSum > 0) {
                val percentage = (correctAnswersSum.toDouble() / totalQuestionsSum) * 100
                if (percentage >= passingPercentage) {
                    passedPrograms.add(programName to percentage)
                }
            }
        }

        // Sort programs by the percentage in descending order
        callback(passedPrograms.sortedByDescending { it.second })
    }
}