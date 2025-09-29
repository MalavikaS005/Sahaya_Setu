package com.sid.civilq_1.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var receiveNewsletters by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Form validation
    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            password == confirmPassword &&
            agreeToTerms

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF666666)
                )
            }
            Text(
                text = "Back",
                color = Color(0xFF666666),
                fontSize = 16.sp,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Create account title
        Text(
            text = "Create account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4A7C59),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Join us today and start your journey",
            fontSize = 16.sp,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // First name and Last name row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "First name",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = {
                        Text(
                            text = "First name",
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
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Last name",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = {
                        Text(
                            text = "Last name",
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
                    singleLine = true,
                    enabled = !isLoading
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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

        // Phone number field
        Text(
            text = "Phone number",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = {
                Text(
                    text = "Enter your phone number",
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                    text = "Create a password",
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

        Spacer(modifier = Modifier.height(24.dp))

        // Confirm password field
        Text(
            text = "Confirm password",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = {
                Text(
                    text = "Confirm your password",
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
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.PlayArrow else Icons.Default.PlayArrow,
                        contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF666666)
                    )
                }
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Terms and conditions checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4A7C59),
                    uncheckedColor = Color(0xFF4A7C59)
                ),
                enabled = !isLoading
            )
            Text(
                text = "I agree to the Terms & Conditions and Privacy Policy",
                color = Color(0xFF666666),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Newsletter checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = receiveNewsletters,
                onCheckedChange = { receiveNewsletters = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4A7C59),
                    uncheckedColor = Color(0xFF4A7C59)
                ),
                enabled = !isLoading
            )
            Text(
                text = "I want to receive newsletters and updates",
                color = Color(0xFF666666),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Create account button
        Button(
            onClick = {
                if (isFormValid) {
                    isLoading = true
                    // Simulate signup process
                    // In real app, call your authentication API here
                    GlobalScope.launch {
                        delay(2000) // Simulate network delay
                        // Navigate to home and clear back stack
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid && !isLoading) Color(0xFF4A7C59) else Color(0xFFCCCCCC)
            ),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Create Account",
                    color = if (isFormValid) Color.White else Color(0xFF666666),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Divider with text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
            Text(
                text = "Or sign up with",
                color = Color(0xFF999999),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social signup buttons
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
                    .clickable(enabled = !isLoading) { /* Handle Facebook signup */ },
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
                    .clickable(enabled = !isLoading) { /* Handle Google signup */ },
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
                    .clickable(enabled = !isLoading) { /* Handle Apple signup */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍎",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                color = Color(0xFF999999),
                fontSize = 14.sp
            )
            Text(
                text = "Login",
                color = Color(0xFF4A7C59),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SignUpScreenLightPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignUpScreenDarkPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun SignUpScreenTabletPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Small Phone", device = "spec:width=320dp,height=568dp,dpi=160")
@Composable
fun SignUpScreenSmallPhonePreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "With Validation", device = "spec:width=360dp,height=740dp,dpi=420")
@Composable
fun SignUpScreenValidationPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}