package com.example.tertiaryprogramreccommender

import BottomNavigationBar
import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Dashboard(
    navController: NavHostController,
    user: User
) {
    val authState = user.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is User.AuthState.Unauthenticated -> navController.navigate("LoginActivity")
            else -> Unit
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp) // Rounded corners for the card
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
                        .padding(it) // Scaffold padding to avoid overlap with the navigation bar
                ) {
                    // Main content of the page
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "WELCOME TO",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Roboto,
                            color = Color.Black
                        )

                        Text(
                            text = "Tertiary Program Recommender",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Roboto,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )

                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(200.dp)
                        )

                        Text(
                            text = "What's in the test?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Roboto,
                            textAlign = TextAlign.Left,
                            color = Color.Black
                        )

                        // Carousel with two cards
                        val pagerState = rememberPagerState(initialPage = 0)

                        HorizontalPager(
                            state = pagerState,
                            count = 2, // Number of pages (cards)
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp) // Restrict height for better layout
                        ) { page ->
                            when (page) {
                                0 -> {
                                    OutlinedCard(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFE5E5E5))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = "The types of aptitude tests are as follows:" +
                                                        "\n1. Business Management\n" +
                                                        "2. Customer Service Skills\n" +
                                                        "3. Educational Psychology\n" +
                                                        "4. Environmental Awareness\n" +
                                                        "5. Geographical Ecological Knowledge\n" +
                                                        "6. Logical Reasoning\n" +
                                                        "7. Mathematical Aptitude\n" +
                                                        "8. Mathematical Reasoning\n" +
                                                        "9. Practical Knowledge\n"+
                                                        "10. Teaching Aptitude\n" +
                                                        "11. Teaching Methods\n" +
                                                        "12. Teaching Principles in Home Economics",
                                                fontSize = 14.sp,
                                                fontFamily = Roboto,
                                                textAlign = TextAlign.Left,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Normal
                                            )


                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                Icon(
                                                    painter = painterResource(id = R.drawable.next),
                                                    contentDescription = "Next",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    OutlinedCard(
                                        modifier = Modifier.fillMaxSize()
                                            .padding(16.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFE5E5E5))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(15.dp)
                                        ) {
                                            Text(
                                                text = "Upon completion of the tests, you will receive a personalized " +
                                                        "report indicating your strengths and areas for improvement. " +
                                                        "This feedback is designed to guide you towards suitable academic" +
                                                        " programs and career paths that align with your abilities. " +
                                                        "Whether you are a student or a professional considering a " +
                                                        "career change, this test is a valuable tool in your " +
                                                        "decision-making process.",
                                                fontSize = 15.sp,
                                                fontFamily = Roboto,
                                                textAlign = TextAlign.Justify,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Normal
                                            )

                                            Spacer(modifier = Modifier.height(73.dp))

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.previous),
                                                    contentDescription = "Previous",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                navController.navigate("AptitudeTestActivity")
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(Color(0xFF011952))
                        ) {
                            Text(
                                text = "Start Aptitude Test",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Roboto
                            )
                        }
                    }
                }
            }
        }
    }
}
