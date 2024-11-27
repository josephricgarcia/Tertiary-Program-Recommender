package com.example.tertiaryprogramreccommender

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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.tertiaryprogramreccommender.ui.theme.TertiaryProgramRecommenderTheme

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
                    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

                    if (!isConnected) {
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

    // Composable for displaying a no internet screen with a retry button
    @Composable
    fun NoInternetScreen(onRetryClick: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No internet connection available!", color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetryClick) {
                    Text(text = "Retry")
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
