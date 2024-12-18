package com.example.tertiaryprogramreccommender

import Roboto
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.tertiaryprogramreccommender.ui.theme.TertiaryProgramRecommenderTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val user: User by viewModels()

        setContent {
            TertiaryProgramRecommenderTheme {
                StatusBarColorWrapper {
                    val context = LocalContext.current

                    // Track the network connection status and loading state
                    var isConnected by remember { mutableStateOf(false) }
                    var isLoading by remember { mutableStateOf(true) }

                    // Launching a coroutine for network check
                    LaunchedEffect(Unit) {
                        // Simulate loading delay
                        delay(1000)

                        // Check network availability
                        isConnected = isNetworkAvailable(context)
                        isLoading = false // Set loading to false after the check
                    }

                    // Show a loading screen or splash screen while checking the network
                    if (isLoading) {
                        SplashScreen()
                    } else if (!isConnected) {
                        NoInternetScreen {
                            // Recheck internet connection
                            isConnected = isNetworkAvailable(context)
                        }
                    } else {
                        Scaffold { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                NavigationController(
                                    modifier = Modifier.padding(innerPadding),
                                    user = user
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Function to check if network is available
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            connectivityManager.activeNetworkInfo
        }

        val capabilities = connectivityManager.getNetworkCapabilities(network as Network?)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    // Composable for displaying a splash screen
    @Composable
    fun SplashScreen() {
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "TERTIARY PROGRAM",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Roboto,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Text(
                        text = "RECOMMENDER",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Roboto,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(250.dp)
                )
            LinearProgressIndicator() // Show loading spinner
                }
            }
        }
    }

    // Composable for displaying a no internet screen with a retry button
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
                        text = "No Internet Connection.\nPlease check your connection and try again.",
                        fontFamily = Roboto,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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

    @Composable
    fun StatusBarColorWrapper(content: @Composable () -> Unit) {
        val activity = LocalContext.current as Activity
        val blueColor = Color(0xFF2962FF)

        SideEffect {
            // Enable edge-to-edge display and set the status bar color
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            activity.window.statusBarColor = blueColor.toArgb()
        }
        content()
    }
}
