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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ForgotPasswordActivity(navController: NavHostController, user: User) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }

    // Create a callback for the reset result
    val onResult: (Boolean, String) -> Unit = { success, message ->
        showToast = true
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Layout
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Forgot Your Password?",
                    fontFamily = Roboto,
                    color = Color(0xFF001F3F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )

                Image(
                    painter = painterResource(id = R.drawable.forgot),
                    contentDescription = "Logo",
                    modifier = Modifier.size(450.dp)
                )

                Card(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter your email address to create a new password",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 15.sp
                        )

                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Blue,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            leadingIcon = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.MailOutline,
                                        contentDescription = "Email",
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "Email Address",
                                    color = Color.DarkGray,
                                    fontFamily = Roboto
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Serif
                            )
                        )

                        Button(
                            onClick = {
                                user.handleForgotPassword(email, onResult)
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF011952)),
                            modifier = Modifier.padding(top = 20.dp)
                        ) {
                            Text(
                                text = "Send Reset Link",
                                fontFamily = Roboto,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = "Remember Password? ", fontFamily = Roboto)
                    Text(
                        text = "Login Here",
                        modifier = Modifier.clickable {
                            navController.navigate("LoginActivity")
                        },
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Roboto
                    )
                }
            }
        }
    }
}
