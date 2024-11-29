package com.example.tertiaryprogramreccommender

import Roboto
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SignupActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    user: User
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authState = user.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is User.AuthState.Authenticated -> {
                navController.navigate("Dashboard")
            }

            is User.AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as User.AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                // Clear the error after displaying it
                user.handlePromptMessageError()
            }

            else -> Unit
        }
    }

    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    if (!isConnected) {
        // Show the no internet screen
        NoInternetScreen {
            // Recheck internet connection
            isConnected = isNetworkAvailable(context)
        }
    } else {


        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp) // Rounded corners for the card
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background Image inside the Box
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(280.dp)
                    )

                    Text(
                        text = "Let's Get You Started",
                        color = Color.Black,
                        fontFamily = Roboto,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Create Your Account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Roboto,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .size(width = 300.dp, height = 60.dp),
                        value = username,
                        onValueChange = { username = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = "Username Icon"
                            )
                        },
                        label = { Text("Username") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .size(width = 300.dp, height = 60.dp),
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.email_icon),
                                contentDescription = "Email"
                            )
                        },
                        label = { Text("Email Address") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .size(width = 300.dp, height = 60.dp),
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password_lock),
                                contentDescription = "Lock Icon"
                            )
                        },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = if (passwordVisible) painterResource(id = R.drawable.password_visibility_on) else painterResource(
                                        id = R.drawable.password_visibility_off
                                    ),
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .size(width = 300.dp, height = 60.dp),
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password_lock),
                                contentDescription = "Lock Icon"
                            )
                        },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (confirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }) {
                                Icon(
                                    painter = if (confirmPasswordVisible) painterResource(id = R.drawable.password_visibility_on) else painterResource(
                                        id = R.drawable.password_visibility_off
                                    ),
                                    contentDescription = if (confirmPasswordVisible) "Hide confirm password" else "Show confirm password"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            user.handleSignup(username, email, password, confirmPassword)
                        },
                        modifier = Modifier.size(300.dp, 40.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF011952))
                    ) {
                        Text(text = "CREATE ACCOUNT", fontFamily = Roboto)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Text(text = "Already have an account? ", fontFamily = Roboto)
                        Text(
                            text = "Sign In",
                            fontFamily = Roboto,

                            modifier = Modifier.clickable {
                                navController.navigate("LoginActivity")
                            },
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    TextButton(
                        onClick = { navController.navigate("AboutUs") },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "ABOUT US",
                            fontFamily = Roboto,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}