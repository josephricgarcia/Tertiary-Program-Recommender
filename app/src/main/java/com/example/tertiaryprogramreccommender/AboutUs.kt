package com.example.tertiaryprogramreccommender

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun AboutUs(navController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundImage()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeText()
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(250.dp)
                )
                AboutUsText()

                // Carousel of About Us content
                AboutUsCarousel()

                GetStartedButton(navController)
            }
        }
    }
}

@Composable
fun WelcomeText() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "WELCOME TO", fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = Roboto,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Text(
            text = "Tertiary Program Recommender",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Roboto,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun AboutUsText() {
    Text(
        text = "ABOUT US",
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Roboto,
        color = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AboutUsCarousel() {
    val pagerState = rememberPagerState()

    HorizontalPager(
        state = pagerState,
        count = 3,
        modifier = Modifier.fillMaxWidth().height(430.dp)
    ) { page ->
        AboutUsCard(page)
    }
}

@Composable
fun AboutUsCard(page: Int) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(420.dp), // Adjust height as necessary
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFE5E5E5))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .align(alignment = Alignment.CenterHorizontally )


        ) {
            when (page) {
                0 -> {Column(
                    modifier = Modifier.fillMaxSize()){
                    Text(
                        text = "The Tertiary Program Recommender (TPR) is a tool designed to help users identify the best-fit tertiary education programs offered by BISU-Clarin Campus. By analyzing the strengths revealed through aptitude test, TPR provides personalized recommendations to guide students in making informed decisions about their educational journey. TPR ensures that you choose the program that aligns with your aptitude and potential.",
                                fontSize = 15.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Left,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(74.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {



                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next", // Ensure consistent size
                            tint = Color.Black
                        )
                    }

                }
                }
                1 -> {Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "The Programs Offered are:" +
                                "\n• BSCS (Bachelor of Science in Computer Science)" +
                                "\n• BSES (Bachelor of Science in Environmental Science major in Coastal Resource Management)" +
                                "\n• BSHM (Bachelor of Science in Hospitality Management)" +
                                "\n• BSEd (Math) (Bachelor in Secondary Education, major in Mathematics)" +
                                "\n• BEEd (Bachelor of Elementary Education)" +
                                "\n• BTLEd (HE) (Bachelor in Technology Livelihood Education, major in Home Economics)",
                        fontSize = 15.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify,
                        color = Color.Black
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.previous),
                            contentDescription = "Previous",
                            tint = Color.Black
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next",
                            modifier = Modifier.size(32.dp), // Ensure consistent size
                            tint = Color.Black
                        )
                    }

                }


                }
                2 -> {Column(
                    modifier = Modifier.fillMaxSize()){
                    Text(
                        text = "By leveraging the results of users' aptitude tests, TPR creates a personalized recommendations. " +
                                "This allows students and professionals to find the ideal academic path with ease. Whether you're pursuing tertiary education or " +
                                "seeking to further your academic journey, TPR is your trusted guide to making informed decisions.",
                        fontSize = 15.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(124.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.previous),
                        contentDescription = "Previous",
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Next",
                        tint = Color.Black
                    )
                }

                }
                }
                }
            }
    }
}

@Composable
fun GetStartedButton(navController: NavHostController) {
    Button(
        onClick = {
            navController.navigate("LoginActivity")
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFF011952))
    ) {
        Text(
            text = "GET STARTED",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Roboto,
            color = Color.White
        )
    }
}
