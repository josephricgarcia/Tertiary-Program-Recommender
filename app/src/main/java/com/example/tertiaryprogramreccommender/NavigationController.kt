package com.example.tertiaryprogramreccommender

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun NavigationController(modifier: Modifier = Modifier, user: User) {
    val navigationController = rememberNavController()

    val controller = AptitudeTestController(
        model = AptitudeTestModel(),
    )

    NavHost(navController = navigationController, startDestination = "AboutUs", builder =  {

        composable("AboutUs") {
            AboutUs(navigationController)
        }

        composable("LoginActivity") {
            LoginActivity(modifier, navigationController, user)

        }

        composable("SignupActivity") {
            SignupActivity(modifier,navigationController,user)
        }

        composable("Dashboard") {
            Dashboard(navigationController, user)
        }

        composable("AptitudeTestActivity") {
            AptitudeTestActivity(
                navigationController, controller)
        }
        composable("EditAccountActivity") {
            EditAccountActivity(navigationController, user)
        }

        composable("RecommendationActivity") {
            RecommendationActivity(navigationController, user)
        }

        composable("Insights/{programName}") { backStackEntry ->
            val programName = backStackEntry.arguments?.getString("programName")
            if (programName != null) {
                Insights(navigationController, programName)
            }
        }

        composable("ForgotPasswordActivity") {
            ForgotPasswordActivity(navigationController, user)
        }
    }
    )
}
