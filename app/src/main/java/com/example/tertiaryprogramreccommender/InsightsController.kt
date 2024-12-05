package com.example.tertiaryprogramreccommender

import androidx.navigation.NavHostController

class InsightsController(private val navController: NavHostController) {
    fun getReasoningType(programName: String): Pair<String, String> =
        InsightsModel.getReasoningType(programName)

    fun getProgramDetails(programName: String): String =
        InsightsModel.getProgramDetails(programName)

    fun onBackPressed() {
        navController.navigate("RecommendationActivity")
    }
}
