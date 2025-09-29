package com.sid.civilq_1.screens

import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // For Google Sign-In to work with Firebase, you need to request ID token
    val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("290279683385-81erbnvb3shb4op63rl26cc2vl5272tb.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = remember{
        GoogleSignIn.getClient(context,googleSignInOptions)
    }
    val launcher
    =rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential= GoogleAuthProvider.getCredential(account.idToken,null)
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        Log.d("GoogleSignIn", "Success: ${authResult.result?.user?.uid}")
                        Toast.makeText(context, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()

                        // Navigate to home and clear ALL previous screens
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        val errorMessage = authResult.exception?.localizedMessage ?: "Google Sign-In failed"
                        Log.e("GoogleSignInError", errorMessage, authResult.exception)
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Welcome back title
        Text(
            text = "Welcome back!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4A7C59),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Email field
        Text(
            text = "Email address",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = "Enter your email",
                    color = Color(0xFFAAAAAA)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF4A7C59),
                focusedBorderColor = Color(0xFF4A7C59),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password field
        Text(
            text = "Password",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    text = "••••••••••",
                    color = Color(0xFFAAAAAA)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF4A7C59),
                focusedBorderColor = Color(0xFF4A7C59),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.PlayArrow else Icons.Default.PlayArrow,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF666666)
                    )
                }
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Remember me and Forgot password row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4A7C59),
                        uncheckedColor = Color(0xFF4A7C59)
                    ),
                    enabled = !isLoading
                )
                Text(
                    text = "Remember me",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Forgot password?",
                color = Color(0xFF4A7C59),
                fontSize = 14.sp,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    // TODO: Handle forgot password
                    Toast.makeText(context, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true

                    Firebase.auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Log.d("Login", "Success: ${task.result?.user?.uid}")
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                                // Navigate to home and clear ALL previous screens
                                navController.navigate("home") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                                Log.e("LoginError", errorMessage, task.exception)
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7C59)),
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }


        Spacer(modifier = Modifier.height(40.dp))

        // Divider with text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
            Text(
                text = "Login with",
                color = Color(0xFF999999),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social login buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Facebook button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1877F2))
                    .clickable(enabled = !isLoading) {
                        Toast.makeText(context, "Facebook login coming soon", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "f",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Google button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(enabled = !isLoading) {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color(0xFF4285F4),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Apple button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable(enabled = !isLoading) {
                        Toast.makeText(context, "Apple login coming soon", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍎",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign up text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color(0xFF999999),
                fontSize = 14.sp
            )
            Text(
                text = "Sign up",
                color = Color(0xFF4A7C59),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    navController.navigate("signup")
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun LoginScreenLightPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenDarkPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun LoginScreenTabletPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Small Phone", device = "spec:width=320dp,height=568dp,dpi=160")
@Composable
fun LoginScreenSmallPhonePreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "With Data")
@Composable
fun LoginScreenWithDataPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}