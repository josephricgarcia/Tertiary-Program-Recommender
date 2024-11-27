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
import androidx.compose.foundation.layout.fillMaxWidth
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
fun LoginActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    user: User,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) } // Loading state

    val authState by user.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is User.AuthState.Authenticated -> {
                navController.navigate("Dashboard")
            }

            is User.AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as User.AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                user.handlePromptMessageError()
            }

            else -> Unit
        }
    }

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

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(15.dp))

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(300.dp)
                )

                Text(
                    text = "Welcome Back",
                    fontSize = 30.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Login To Your Account",
                    fontSize = 15.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Email Input
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
                    label = {
                        Text(
                            "Email Address",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Password Input
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
                    label = {
                        Text(
                            "Password",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    visualTransformation = if (passwordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            if (passwordVisible) {
                                Icon(
                                    painter = painterResource(id = R.drawable.password_visibility_off),
                                    contentDescription = "Hide password"
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.password_visibility_on),
                                    contentDescription = "Show password"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(1.dp))

                // Forgot Password
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Forgot Password?",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.clickable {
                            navController.navigate("ForgotPasswordActivity")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Login Button or Loading State
                Button(
                    onClick = {
                        loading = true // Start loading when button is clicked
                        user.handleLogin(email, password) // Perform login action
                    },
                    modifier = Modifier.size(300.dp, 40.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF011952))
                ) {
                    Text(text="SIGN IN", fontFamily = Roboto)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Don't have an account?",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        text = "Sign Up", fontFamily = Roboto, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("SignupActivity")
                        },
                        textDecoration = TextDecoration.Underline
                    )
                }

                TextButton(
                    onClick = { navController.navigate("AboutUs") },
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text(
                        text = "ABOUT US",
                        fontFamily = Roboto,
                        color = Color(0xFF011952),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



