package com.example.tertiaryprogramreccommender

import androidx.navigation.NavHostController

class InsightsController(private val navController: NavHostController) {

    // Instance of InsightsModel
    private val insightsModel = InsightsModel()

    // Get reasoning type for a program
    fun getReasoningType(programName: String): Pair<String, String> =
        insightsModel.getReasoningType(programName)

    // Get program details for a program
    fun getProgramDetails(programName: String): String =
        insightsModel.getProgramDetails(programName)

    // Handle back press navigation
    fun onBackPressed() {
        navController.navigate("RecommendationActivity")
    }
}
