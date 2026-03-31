package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun EmailVerificationScreen(
    onBackClick: () -> Unit = {},
    onSendCodeClick: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validateEmail(mail: String): Boolean {
        return mail.endsWith("@gmail.com")
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

            Text(
                text = "Email Verification",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0D1E3C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your email address to receive a verification code.",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = Color(0xFF1E63FF)
                    )
                },
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill your email")
                        }
                    } else if (!validateEmail(email)) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Email must include @gmail.com")
                        }
                    } else {
                        scope.launch {
                            isLoading = true
                            try {
                                val response = RetrofitClient.instance.forgotPassword(
                                    ForgotPasswordRequest(email)
                                )

                                if (response.isSuccessful && response.body()?.success == true) {
                                    snackbarHostState.showSnackbar("Verification code sent!")
                                    onSendCodeClick(email)
                                } else {
                                    val errorBody = response.errorBody()?.string() ?: ""
                                    val errorMsg = try {
                                        val json = JSONObject(errorBody)
                                        when {
                                            json.has("error") -> json.getString("error")
                                            json.has("detail") -> json.getString("detail")
                                            json.has("message") -> json.getString("message")
                                            else -> "Error: $errorBody"
                                        }
                                    } catch (_: Exception) {
                                        "Error ${response.code()}: $errorBody"
                                    }
                                    snackbarHostState.showSnackbar(errorMsg)
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Network error: ${e.message}")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E63FF)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Send Code",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationPreview() {
    RoomshareAIRentalAppTheme {
        EmailVerificationScreen()
    }
}
