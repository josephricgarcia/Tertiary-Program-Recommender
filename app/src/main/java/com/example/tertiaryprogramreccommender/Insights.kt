package com.example.tertiaryprogramreccommender

import Roboto
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Insights(
    navController: NavHostController,
    programName: String
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()

            )

            // Map of program names to reasoning types
            val programToReasoningType = mapOf(
                "BSCS" to Pair("Logical Reasoning", "Mathematical Aptitude"),
                "BSHM" to Pair("Customer Service Skills", "Business Management"),
                "BSES-CRM" to Pair("Environmental Awareness", "Geographical Knowledge"),
                "BSEd-Math" to Pair("Mathematical Reasoning", "Teaching Aptitude"),
                "BTLed-HE" to Pair("Practical Knowledge", "Home Economics"),
                "BEED" to Pair("Educational Psychology", "Teaching Methods")
            )


// Retrieve reasoning type based on program name
            val reasoningType = programToReasoningType[programName] ?: "N/A"

// Information details for each program
            val programDetails = when (programName) {
                "BSCS" -> "Foundational Skills: Learn core programming languages (Python, Java, C++) and build a solid math background (calculus, discrete math)." +
                        "\nCore Topics: Expect to study algorithms, data structures, databases, and computer systems." +
                        "\nSpecializations: Explore areas like AI, cybersecurity, data science, software engineering, networking, or game development." +
                        "\nProblem Solving: Cultivate patience and critical thinking for debugging and complex problem-solving." +
                        "\nContinuous Learning: Stay updated with new languages and tools through online courses and resources."

                "BSHM" -> "Foundational Skills: Develop communication, leadership, and customer service skills to effectively manage hospitality settings." +
                        "\nCore Topics: Study principles of management, event planning, food and beverage operations, and tourism." +
                        "\nSpecializations: Explore areas like hotel management, culinary arts, event coordination, and tourism management." +
                        "\nProblem Solving: Hone critical thinking and adaptability to handle guest needs and resolve on-the-spot issues." +
                        "\nContinuous Learning: Keep up with industry trends and new hospitality technologies to enhance guest experiences."

                "BEED" -> "Foundational Skills: Build strong interpersonal and communication skills to work effectively with students and parents." +
                        "\nCore Topics: Study curriculum development, child psychology, teaching strategies, and classroom management." +
                        "\nSpecializations: Focus on elementary education, literacy, special education, or curriculum design." +
                        "\nProblem Solving: Develop patience, creativity, and critical thinking to meet diverse student needs." +
                        "\nContinuous Learning: Stay updated on educational methodologies and technology in education."

                "BSES-CRM" -> "Foundational Skills: Gain knowledge in environmental sciences, research methods, and conservation principles." +
                        "\nCore Topics: Study ecology, environmental policy, coastal resource management, and sustainability." +
                        "\nSpecializations: Explore areas like wildlife conservation, resource management, and environmental policy." +
                        "\nProblem Solving: Develop analytical skills for environmental problem-solving and policy-making." +
                        "\nContinuous Learning: Keep up with environmental trends and regulations to promote sustainability."

                "BSEd-Math" -> "Foundational Skills: Develop strong mathematical foundations and analytical skills." +
                        "\nCore Topics: Study advanced mathematics, including calculus, algebra, geometry, and statistics." +
                        "\nSpecializations: Focus on areas like secondary education, curriculum design, or mathematics pedagogy." +
                        "\nProblem Solving: Cultivate critical thinking and logic to present complex mathematical concepts in an accessible way." +
                        "\nContinuous Learning: Stay updated with new educational approaches and mathematical advancements."

                "BTLed-HE" -> "Foundational Skills: Acquire knowledge in home economics, management, and practical life skills." +
                        "\nCore Topics: Study nutrition, family and consumer science, interior design, and entrepreneurship." +
                        "\nSpecializations: Explore fields like culinary arts, fashion design, or community services." +
                        "\nProblem Solving: Develop creativity and adaptability to provide solutions for real-life household and community challenges." +
                        "\nContinuous Learning: Stay informed about trends in home economics and educational techniques."

                else -> "Information not available for this program."
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    TextButton(
                        onClick = { navController.navigate("RecommendationActivity") },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }

                    Text(
                        text = "INSIGHTS",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Roboto,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                    )


                val imageResId = when (programName) {
                    "BSCS" -> R.drawable.bscs
                    "BSHM" -> R.drawable.bshm
                    "BSES-CRM" -> R.drawable.bses
                    "BEED" -> R.drawable.cte
                    "BTLed-HE" -> R.drawable.cte
                    "BSEd-Math" -> R.drawable.cte
                    else -> null
                }

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        imageResId?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = "Logo",

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .height(200.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        Text(
                            text = "Program: $programName",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Strength: $reasoningType",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = programDetails,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left
                        )
                    }
                }
            }
        }
    }
}
