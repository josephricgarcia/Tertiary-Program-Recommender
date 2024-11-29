package com.example.tertiaryprogramreccommender

import BottomNavigationBar
import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BackgroundImage() {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun EditAccountActivity(
    navController: NavHostController,
    user: User
) {
    val authState = user.authState.observeAsState()

    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    if (!isConnected) {
        // Show the no internet screen
        NoInternetScreen {
            // Recheck internet connection
            isConnected = isNetworkAvailable(context)
        }
    } else {


        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->

            Box(modifier = Modifier.fillMaxSize()) {
                BackgroundImage()

                LaunchedEffect(authState.value) {
                    when (authState.value) {
                        is User.AuthState.Unauthenticated -> navController.navigate("LoginActivity") {
                            popUpTo("AccountScreen") { inclusive = true }
                            launchSingleTop = true
                        }

                        else -> Unit
                    }
                }

                EditAccount(authState.value, paddingValues, navController, user)
            }
        }
    }
}

    @Composable
    fun EditAccount(
        authState: User.AuthState?,
        paddingValues: PaddingValues,
        navController: NavHostController,
        userController: User
    ) {
        val showSignOutDialog = remember { mutableStateOf(false) }

        // States for input fields
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val newPassword = remember { mutableStateOf("") }

        // Fetch username from the database
        LaunchedEffect(authState) {
            if (authState is User.AuthState.Authenticated) {
                val userId = userController.auth.currentUser?.uid
                userId?.let {
                    userController.fetchUsernameFromDatabase(userId) { fetchedUsername ->
                        username.value = fetchedUsername ?: "Unknown User"
                    }
                }
            }
        }

        // State for overlay
        val showEditOverlay = remember { mutableStateOf(false) }
        val fieldToEdit = remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "MY ACCOUNT",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = Roboto,
                    color = Color.Black,
                )

                when (authState) {
                    is User.AuthState.Authenticated -> {
                        val user = userController.auth.currentUser
                        user?.let {
                            Icon(
                                painter = painterResource(id = R.drawable.account_circle),
                                tint = Color.Black,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(16.dp)
                            )

                            Text(
                                text = username.value,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily.Default,
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = it.email ?: "",
                                fontSize = 16.sp,
                                fontFamily = Roboto,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray
                            )

                            Text(
                                text = "Account Settings",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Roboto,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Change Username
                            SettingsRow("Change Username") {
                                fieldToEdit.value = "username"
                                showEditOverlay.value = true
                            }

                            // Change Password
                            SettingsRow("Change Password") {
                                fieldToEdit.value = "password"
                                showEditOverlay.value = true
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray
                            )

                            // Sign Out
                            TextButton(onClick = {
                                showSignOutDialog.value = true
                            }) {
                                Text(
                                    text = "Sign Out",
                                    fontFamily = Roboto,
                                    color = Color(0xFFA30000),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                            }

                            if (showSignOutDialog.value) {
                                AlertDialog(
                                    onDismissRequest = { showSignOutDialog.value = false },
                                    title = {
                                        Text(
                                            "WARNING",
                                            color = Color(0xFFA30000),
                                            fontFamily = Roboto,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 25.sp
                                        )
                                    },
                                    text = {
                                        Text(
                                            "Are you sure you want to log out?",
                                            fontFamily = Roboto,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 17.sp
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showSignOutDialog.value = false }) {
                                            Text(
                                                "No", color = Color(0xFFA30000),
                                                fontFamily = Roboto,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                showSignOutDialog.value = false
                                                userController.handleSignOut()
                                                navController.navigate("AboutUs") {
                                                    popUpTo("AccountScreen") { inclusive = true }
                                                    launchSingleTop = true
                                                }
                                            }
                                        ) {
                                            Text(
                                                "Yes", color = Color(0xFF008000),
                                                fontFamily = Roboto,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    else -> {
                        // Handle loading and error states as needed
                        Text("Loading or Error...")
                    }
                }
            }

            // Edit Overlay for Username or Password
            if (showEditOverlay.value) {
                EditAccountOverlay(
                    fieldToEdit.value,
                    username.value,
                    password.value,
                    onSave = { updatedValue, newPassword ->
                        when (fieldToEdit.value) {
                            "username" -> userController.handleEditAccount(username = updatedValue)
                            "password" -> userController.handleEditAccount(
                                password = updatedValue,
                                newPassword = newPassword
                            )
                        }
                        showEditOverlay.value = false
                    },
                    onCancel = {
                        showEditOverlay.value = false
                    }
                )
            }
        }
    }

    @Composable
    fun EditAccountOverlay(
        field: String,
        username: String,
        password: String,
        onSave: (String, String?) -> Unit,
        onCancel: () -> Unit
    ) {
        val fieldValue = remember {
            mutableStateOf(
                when (field) {
                    "username" -> username
                    else -> ""
                }
            )
        }
        val passwordFieldValue = remember { mutableStateOf("") }
        val newPasswordFieldValue = remember { mutableStateOf("") }
        val saveMessage = remember { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Edit $field",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (field == "password") {
                        TextField(
                            value = passwordFieldValue.value,
                            onValueChange = { passwordFieldValue.value = it },
                            label =
                            { Text("Current Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = newPasswordFieldValue.value,
                            onValueChange = { newPasswordFieldValue.value = it },
                            label = { Text("New Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )
                    } else {
                        TextField(
                            value = fieldValue.value,
                            onValueChange = { fieldValue.value = it },
                            label = { Text("Enter new $field") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                when {
                                    field == "username" && fieldValue.value.isBlank() -> {
                                        saveMessage.value = "Username cannot be empty"
                                    }

                                    field == "password" && (passwordFieldValue.value.isBlank() || newPasswordFieldValue.value.isBlank()) -> {
                                        saveMessage.value = "Passwords cannot be empty"
                                    }

                                    field == "password" -> {
                                        // Verify current password
                                        val userController = User()
                                        userController.verifyCurrentPassword(passwordFieldValue.value) { isValid ->
                                            if (!isValid) {
                                                saveMessage.value = "Current password is incorrect"
                                            } else {
                                                // Proceed to save the new password
                                                onSave(
                                                    passwordFieldValue.value,
                                                    newPasswordFieldValue.value
                                                )
                                                saveMessage.value = "Changes saved successfully"
                                            }
                                        }
                                    }

                                    else -> {
                                        onSave(
                                            when (field) {
                                                "username" -> fieldValue.value
                                                "password" -> passwordFieldValue.value
                                                else -> ""
                                            },
                                            if (field == "password") newPasswordFieldValue.value else null
                                        )
                                        saveMessage.value = "Changes saved successfully"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF008000)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { onCancel() },
                            colors = ButtonDefaults.buttonColors(Color(0xFFA30000)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                    }

                    saveMessage.value?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = if (it.contains("successfully")) Color(0xFF008000) else Color(
                                0xFFA30000
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SettingsRow(label: String, onEdit: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = Roboto,
                color = Color.Black
            )

            TextButton(onClick = onEdit) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Edit",
                        color = Color(0xFF1976D2),
                        fontFamily = Roboto
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
