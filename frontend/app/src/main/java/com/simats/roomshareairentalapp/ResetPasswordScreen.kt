package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onResetSuccess: () -> Unit = {},
    otp: String = ""
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validatePassword(pass: String): Boolean {
        val hasUppercase = pass.any { it.isUpperCase() }
        val hasNumber = pass.any { it.isDigit() }
        val hasSpecial = pass.any { !it.isLetterOrDigit() }
        return if (pass.length >= 6) {
            passwordError = null
            true
        } else {
            passwordError = "Password must be at least 6 characters"
            false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5FE))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF0D1E3C)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Reset Password", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D1E3C))
            Text("Set your new password to secure your account.", color = Color.Gray, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { 
                    newPassword = it
                    validatePassword(it)
                },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                isError = passwordError != null,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1E63FF)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E63FF),
                    unfocusedBorderColor = Color(0xFFF1F5F9),
                    focusedLabelColor = Color(0xFF1E63FF),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedContainerColor = Color(0xFFF8F9FE),
                    focusedContainerColor = Color(0xFFF8F9FE)
                ),
                enabled = !isLoading
            )
            if (passwordError != null) {
                Text(passwordError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmError = if (it != newPassword) "Passwords do not match" else null
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                isError = confirmError != null,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1E63FF)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E63FF),
                    unfocusedBorderColor = Color(0xFFF1F5F9),
                    focusedLabelColor = Color(0xFF1E63FF),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedContainerColor = Color(0xFFF8F9FE),
                    focusedContainerColor = Color(0xFFF8F9FE)
                ),
                enabled = !isLoading
            )
            if (confirmError != null) {
                Text(confirmError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Please fill all details") }
                    } else {
                        val isPassValid = validatePassword(newPassword)
                        val isMatch = newPassword == confirmPassword
                        confirmError = if (!isMatch) "Passwords do not match" else null
                        
                        if (isPassValid && isMatch) {
                            scope.launch {
                                isLoading = true
                                try {
                                    val response = RetrofitClient.instance.resetPassword(ResetPasswordRequest(email, otp, newPassword))
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        snackbarHostState.showSnackbar("Password reset successfully")
                                        onResetSuccess()
                                    } else {
                                        val errorBody = response.errorBody()?.string() ?: ""
                                        val errorMsg = try {
                                            val json = JSONObject(errorBody)
                                            json.optString("error").takeIf { it.isNotEmpty() }
                                                ?: json.optString("detail").takeIf { it.isNotEmpty() }
                                                ?: json.optString("message").takeIf { it.isNotEmpty() }
                                                ?: "Error: $errorBody"
                                        } catch (e: Exception) {
                                            "Error ${response.code()}: $errorBody"
                                        }
                                        snackbarHostState.showSnackbar(errorMsg)
                                    }
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Error: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E63FF)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Reset Password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    RoomshareAIRentalAppTheme {
        ResetPasswordScreen()
    }
}
