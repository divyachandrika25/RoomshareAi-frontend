package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Smartphone
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

@Composable
fun PhoneVerificationScreen(onBackClick: () -> Unit = {}, onSendCodeClick: (String) -> Unit = {}) {
    var phoneNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validatePhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5FE))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF0D1E3C)
                        )
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SMS Verification",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0D1E3C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your phone number to receive a verification code.",
                fontSize = 16.sp,
                color = Color(0xFF64748B),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10) phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Smartphone, contentDescription = null, tint = Color(0xFF22C55E))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF22C55E),
                    unfocusedBorderColor = Color(0xFFF1F5F9),
                    focusedLabelColor = Color(0xFF22C55E),
                    unfocusedLabelColor = Color(0xFF64748B)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    if (phoneNumber.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Please fill your phone number") }
                    } else if (!validatePhone(phoneNumber)) {
                        scope.launch { snackbarHostState.showSnackbar("Phone number must be exactly 10 digits") }
                    } else {
                        onSendCodeClick(phoneNumber) 
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E63FF)
                )
            ) {
                Text(
                    text = "Send Code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneVerificationPreview() {
    RoomshareAIRentalAppTheme {
        PhoneVerificationScreen()
    }
}
