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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val scoresState = remember { mutableStateOf<Map<String, Pair<Int, Int>>>(emptyMap()) }
    val isLoading = remember { mutableStateOf(true) }
    val topPrograms = remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }

    // Instantiate model and controller
    val model = RecommendationModel()
    val controller = RecommendationController(model)

    // Fetch data through the controller
    LaunchedEffect(userId) {
        userId?.let {
            controller.getUserScores { scores ->
                scoresState.value = scores
                controller.getRecommendedPrograms { rankedPrograms ->
                    topPrograms.value = rankedPrograms
                    isLoading.value = false // Set loading to false once data is fetched
                }
            }
        }
    }

    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    if (!isConnected) {
        NoInternetScreen {
            isConnected = isNetworkAvailable(context)
        }
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.dp, Color.Unspecified)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = "Background Image",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(
                        text = "RECOMMENDATIONS",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = Roboto
                    )

                    if (isLoading.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(CenterHorizontally)
                            )
                            Text(
                                text = "Loading Recommendations...",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                    } else {
                        DisplayScores(scoresState.value)

                        if (topPrograms.value.isEmpty()) {
                            Text(
                                text = "No Programs Passed. Please retake the test.",
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

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
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Roboto
                                    )
                                }
                            }
                        } else {
                            DisplayTopPrograms(navController, topPrograms.value)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
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


@Composable
fun DisplayScores(scores: Map<String, Pair<Int, Int>>) {
    if (scores.isEmpty()) {

        Spacer(modifier = Modifier.height(300.dp))

        Text(
            text = "No Scores Available",
            color = Color.Red,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "YOUR SCORES",
                    modifier = Modifier.align(CenterHorizontally),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                scores.forEach { (testType, score) ->
                    val (correctAnswers, totalQuestions) = score
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = testType, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                        Text(text = "$correctAnswers / $totalQuestions", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayTopPrograms(
    navController: NavHostController,
    programs: List<Pair<String, Double>>
) {
    if (programs.isEmpty()) {
        Text(
            text = "No Programs Passed. Please retake the test.",
            color = Color.Red,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    navController.navigate("AptitudeTestActivity")
                },
                colors = ButtonDefaults.buttonColors(
                    Color(
                        0xFF011952
                    )
                )
            ) {
                Text(
                    text = "Retake Aptitude Test",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Roboto
                )
            }
        }

    } else {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "RECOMMENDED PROGRAMS",
                modifier = Modifier.padding(top = 16.dp)
                    .align(CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    programs.forEachIndexed { index, program ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Top ${index + 1}: ${program.first}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            TextButton(
                                onClick = {
                                    navController.navigate("InsightsActivity/${program.first}")
                                }
                            ) {
                                Text(
                                    text = "View",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = Roboto,
                                    color = Color(0xFF304FFE)
                                )
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
                    colors = ButtonDefaults.buttonColors(
                        Color(
                            0xFF011952
                        )
                    )
                ) {
                    Text(
                        text = "Retake Aptitude Test",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Roboto
                    )

            }

        }
    }
}
