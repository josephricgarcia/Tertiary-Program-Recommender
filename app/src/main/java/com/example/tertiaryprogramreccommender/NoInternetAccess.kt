package com.example.tertiaryprogramreccommender

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoInternetScreen(onRetryClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp) // Full-screen card with no rounded corners
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop, // Better cropping for full-screen visuals
                modifier = Modifier.fillMaxSize()
            )

            // Foreground Content
            Column(
                modifier = Modifier
                    .padding(24.dp) // Consistent padding for content
                    .background(
                        color = Color.White.copy(alpha = 0.8f), // Semi-transparent background
                        shape = RoundedCornerShape(16.dp) // Rounded corners
                    )
                    .padding(16.dp), // Inner padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Title
                Text(
                    text = "No Internet Connection!",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Image
                Image(
                    painter = painterResource(id = R.drawable.nointernet),
                    contentDescription = "No Internet Illustration",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Adjust width for better layout
                        .aspectRatio(1f) // Maintain aspect ratio
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Retry Button
                Button(
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(Color(0xFF011952)),
                    modifier = Modifier.fillMaxWidth(0.5f), // Adjust button width
                ) {
                    Text(
                        text = "Retry",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

