package com.example.tertiaryprogramreccommender

import BottomNavigationBar
import Roboto
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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

@Composable
fun RecommendationActivity(
    navController: NavHostController,
    user: User
) {
    val authState = user.authState.observeAsState()

    // Navigate to login if the user is unauthenticated
    LaunchedEffect(authState.value) {
        if (authState.value is User.AuthState.Unauthenticated) {
            navController.navigate("LoginActivity")
        }
    }

    // Get the current authenticated user's ID
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // State to store fetched scores as strings (e.g., "5 out of 10")
    val scoresState = remember { mutableStateOf<Map<String, Pair<Int, Int>>>(emptyMap()) }

    // State for loading indicator
    val isLoading = remember { mutableStateOf(true) }

    // State to store the top programs
    val topPrograms = remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }

    val recommendation = Recommendation()
    LaunchedEffect(userId) {
        userId?.let {
            recommendation.fetchUserScores { scores ->
                scoresState.value = scores
                isLoading.value = false  // Set loading to false once data is fetched
                println("Scores fetched: $scores")

                // Fetch the ranking of programs based on the scores
                recommendation.programRanking(scores) { rankedPrograms ->
                    topPrograms.value = rankedPrograms
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image inside an OutlinedCard
        OutlinedCard(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "RECOMMENDATIONS",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Roboto,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                            // Scores Section
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Loading indicator with text while fetching scores
                                if (isLoading.value) {

                                    Column(
                                        horizontalAlignment = CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize()
                                            .padding(16.dp)
                                    ) {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Gathering Scores, please wait",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Roboto
                                        )
                                    }
                                } else {
                                    OutlinedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .height(340.dp)
                                    ) {

                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth()
                                        ) {

                                            Text(text="Scores",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = Roboto,
                                                modifier = Modifier.align(CenterHorizontally))

                                            // Display scores fetched from Firebase
                                            if (scoresState.value.isNotEmpty()) {
                                                scoresState.value.forEach { (testType, score) ->
                                                    val (correctAnswers, totalQuestions) = score

                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {

                                                        Column {

                                                            Text(
                                                                text = "$testType ",
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                fontFamily = Roboto,
                                                            )
                                                        }
                                                        Text(
                                                            text = "$correctAnswers/$totalQuestions",
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = Roboto,
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))

                                            } else {
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Text(
                                                    text = "No Scores Yet",
                                                    modifier = Modifier.align(CenterHorizontally),
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = Roboto,
                                                    color=Color.Red
                                                )
                                            }
                                        }
                                    }
                                }

                                // Fetch real ranking of passed programs and display
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Top,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    // Display message if no programs are passed
                                    if (topPrograms.value.isEmpty()) {
                                        OutlinedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                                .height(220.dp)
                                        ) {
                                        Text(
                                            text = "No Programs Passed, Please Try Again",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Roboto,
                                            color = Color.Red,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )}
                                    } else {
                                        // Display the list of top programs
                                        OutlinedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                                .height(220.dp)
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .fillMaxSize()
                                                    .verticalScroll(rememberScrollState())
                                            ) {

                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = "Passed Programs",
                                                        modifier = Modifier
                                                            .padding(top = 16.dp)
                                                            .align(Alignment.Center),
                                                        fontSize = 20.sp,
                                                        fontFamily = Roboto,
                                                        fontWeight = FontWeight.ExtraBold,
                                                    )
                                                }

                                                topPrograms.value.forEachIndexed { index, program ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween // Distribute space between components
                                                    ) {
                                                        // Program Info
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = "Top ${index + 1}:",
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF000000),
                                                                fontFamily = Roboto,
                                                            )

                                                            Spacer(modifier = Modifier.width(16.dp)) // Spacing between texts

                                                            Text(
                                                                text = "${program.first} ",
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF000000),
                                                                fontFamily = Roboto,
                                                            )
                                                        }

                                                        // Percentage and View Button
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = "${program.second.toInt()}%",
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF000000),
                                                                fontFamily = Roboto,
                                                                modifier = Modifier.width(40.dp), // Fixed width to align percentages
                                                                textAlign = TextAlign.End // Align percentage text to the end
                                                            )

                                                            Spacer(modifier = Modifier.width(20.dp))

                                                            TextButton(
                                                                onClick = {
                                                                    navController.navigate("Insights/${program.first}")
                                                                },
                                                            ) {
                                                                Text(
                                                                    text = "View",
                                                                    color = Color(0xFF011952),
                                                                    fontFamily = Roboto,
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    fontSize = 15.sp
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Button(
                                            onClick = {
                                                navController.navigate("AptitudeTestActivity")
                                            },
                                            colors = ButtonDefaults.buttonColors(Color(0xFF011952))
                                        ) {
                                            Text(
                                                text = "Retake Aptitude Test",
                                                fontFamily = Roboto,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
