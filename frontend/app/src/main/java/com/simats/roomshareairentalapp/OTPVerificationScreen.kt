package com.simats.roomshareairentalapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onVerifyClick: (String) -> Unit = {},
    isPasswordResetFlow: Boolean = false
) {
    var otpCode by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    
    var timeLeft by remember { mutableIntStateOf(59) }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Timer logic
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppTheme.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Premium Back Nav
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, AppTheme.Divider, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = AppTheme.TextPrimary, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Protocol Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppTheme.PrimaryAlpha8),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VerifiedUser, null, tint = AppTheme.Primary, modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Secure Verification",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = AppTheme.TextPrimary,
                letterSpacing = (-1).sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildString {
                    append("We've transmitted a 6-digit authentication key to ")
                    append(if (email.isNotEmpty()) email else "your registered gateway")
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── CONTINUATION OTP INPUT ──────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Hidden BasicTextField for raw input
                BasicTextField(
                    value = otpCode,
                    onValueChange = {
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                            otpCode = it
                        }
                    },
                    modifier = Modifier
                        .size(1.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                // Visual Representation
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { focusRequester.requestFocus() },
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    (0 until 6).forEach { index ->
                        val char = otpCode.getOrNull(index)?.toString() ?: ""
                        val isFocused = otpCode.length == index
                        
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.85f),
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            color = Color.White,
                            border = BorderStroke(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = if (isFocused) AppTheme.Primary else AppTheme.Divider
                            ),
                            shadowElevation = if (isFocused) 4.dp else 0.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = char,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AppTheme.TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                )
                                // Cursor mimic
                                if (isFocused && !isLoading) {
                                    Box(
                                        modifier = Modifier
                                            .height(24.dp)
                                            .width(2.dp)
                                            .background(AppTheme.Primary)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Resend Logic UI
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No signature received? ",
                    color = AppTheme.TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (timeLeft > 0) "Retry in ${timeLeft}s" else "Transmit New Key",
                    color = if (timeLeft > 0) AppTheme.TextTertiary else AppTheme.Primary,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(enabled = timeLeft == 0 && !isLoading) {
                        scope.launch {
                            isLoading = true
                            try {
                                val response = RetrofitClient.instance.sendOtp(SendOtpRequest(email))
                                if (response.isSuccessful) {
                                    snackbarHostState.showSnackbar("New authentication key transmitted")
                                    timeLeft = 59
                                }
                            } catch (_: Exception) {
                                snackbarHostState.showSnackbar("Gateway transmission error")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Button(
                onClick = { 
                    if (otpCode.length == 6) {
                        scope.launch {
                            isLoading = true
                            try {
                                if (isPasswordResetFlow) {
                                    onVerifyClick(otpCode)
                                } else {
                                    val response = RetrofitClient.instance.verifyOtp(VerifyOtpRequest(email, otpCode))
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        onVerifyClick(otpCode)
                                    } else {
                                        snackbarHostState.showSnackbar("Authentication failure: Invalid key")
                                    }
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Network protocol failure")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                enabled = !isLoading && otpCode.length == 6
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Establish Verification", fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPVerificationPreview() {
    RoomshareAIRentalAppTheme {
        OTPVerificationScreen()
    }
}
