package com.simats.roomshareairentalapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onVerifyRequired: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Validation Logic
    fun validateEmail(mail: String): Boolean {
        return if (mail.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            emailError = null
            true
        } else {
            emailError = "Please enter a valid email address"
            false
        }
    }

    fun validatePassword(pass: String): Boolean {
        if (pass.isEmpty()) {
            passwordError = null
            return true
        }
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$"
        return if (pass.matches(passwordPattern.toRegex())) {
            passwordError = null
            true
        } else {
            passwordError = "Uppercase, Lowercase, Number & Special required"
            false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppTheme.Background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(64.dp))

            // Premium Logo Area
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AppTheme.Primary, AppTheme.Secondary)
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.app_logo),
                    contentDescription = "RoomShare Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppTheme.TextPrimary,
                letterSpacing = (-1).sp
            )
            Text(
                text = "Sign in to access your RoomShare AI profile",
                fontSize = 15.sp,
                color = AppTheme.TextSecondary,
                modifier = Modifier.padding(top = 10.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Email Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Primary Authentication (Email)", 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = AppTheme.TextPrimary, 
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; validateEmail(it) },
                    placeholder = { Text("Enter your registered email", color = AppTheme.TextTertiary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                    isError = emailError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.Primary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = AppTheme.TextPrimary,
                        unfocusedTextColor = AppTheme.TextPrimary
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Security Password", 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = AppTheme.TextPrimary, 
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; validatePassword(it) },
                    placeholder = { Text("Enter your password", color = AppTheme.TextTertiary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, 
                                contentDescription = null, 
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.Primary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = AppTheme.TextPrimary,
                        unfocusedTextColor = AppTheme.TextPrimary
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp), 
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot Password?",
                    color = AppTheme.Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable(enabled = !isLoading) { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val rawPassword = password
                    if (trimmedEmail.isNotEmpty() && validateEmail(trimmedEmail) && rawPassword.isNotEmpty() && validatePassword(rawPassword)) {
                        scope.launch {
                            isLoading = true
                            try {
                                val response = RetrofitClient.instance.login(LoginRequest(trimmedEmail, rawPassword))
                                if (response.isSuccessful && response.body()?.success == true) {
                                    val body = response.body()!!
                                    onLoginClick(trimmedEmail, body.token ?: "")
                                } else {
                                    val errorBody = response.errorBody()?.string() ?: ""
                                    try {
                                        val json = JSONObject(errorBody)
                                        if (json.optBoolean("needs_verification", false)) {
                                            snackbarHostState.showSnackbar("Please verify your account first.")
                                            onVerifyRequired(json.getString("email"))
                                        } else {
                                            snackbarHostState.showSnackbar(json.optString("error", "Invalid credentials"))
                                        }
                                    } catch (_: Exception) {
                                        snackbarHostState.showSnackbar("Invalid credentials")
                                    }
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Connectivity failure")
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Validation error") }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", color = AppTheme.TextSecondary, fontSize = 14.sp)
                Text(
                    text = "Create one",
                    color = Color(0xFF818CF8), 
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(enabled = !isLoading) { onSignUpClick() }
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = R.drawable.saveetha_logo,
                    contentDescription = "Saveetha Logo",
                    modifier = Modifier.size(60.dp)
                )
                AsyncImage(
                    model = R.drawable.ses_logo,
                    contentDescription = "SES Logo",
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RoomshareAIRentalAppTheme {
        LoginScreen()
    }
}
