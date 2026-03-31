package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetLocationScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onGenerateProfileClick: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSavedClick: () -> Unit = {}
) {
    var budget by remember { mutableStateOf(15000f) }
    var city by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StandardTopBar(
                title = "Budget & Location",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = AppTheme.TextPrimary
                        )
                    }
                    IconButton(onClick = onSavedClick) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Saved",
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Budget & Location",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Final details before AI starts processing your profile.",
                fontSize = 16.sp,
                color = AppTheme.TextTertiary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MONTHLY BUDGET",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextTertiary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "₹${budget.toInt()}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppTheme.Primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = budget,
                onValueChange = { budget = it },
                valueRange = 5000f..50000f,
                colors = SliderDefaults.colors(
                    thumbColor = AppTheme.Primary,
                    activeTrackColor = AppTheme.Primary,
                    inactiveTrackColor = Color(0xFFF1F5FE)
                ),
                enabled = !isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "₹5,000", fontSize = 12.sp, color = AppTheme.TextTertiary)
                Text(text = "₹50,000+", fontSize = 12.sp, color = AppTheme.TextTertiary)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "PREFERRED CITY",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextTertiary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Chennai", color = Color.LightGray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = AppTheme.Primary
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.Primary,
                    unfocusedBorderColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF8F9FE),
                    focusedContainerColor = Color(0xFFF8F9FE)
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                color = Color(0xFFF1F7FF),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AppTheme.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Sharing a room in this area could save you up to ")
                            withStyle(style = SpanStyle(
                                color = AppTheme.Primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )) {
                                append("₹8,000/month")
                            }
                            append(" based on our AI market analysis.")
                        },
                        fontSize = 14.sp,
                        color = AppTheme.Primary.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Generate AI Profile",
                onClick = {
                    if (city.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill preferred city")
                        }
                    } else if (email.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please ensure you are logged in.")
                        }
                    } else {
                        scope.launch {
                            isLoading = true
                            try {
                                val request = BudgetLocationRequest(
                                    email = email,
                                    monthlyBudget = budget.toInt().toString(),
                                    preferredCity = city
                                )
                                val response = RetrofitClient.instance.budgetLocation(request)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    onGenerateProfileClick(city)
                                } else {
                                    val errorMsg = try {
                                        response.errorBody()?.string()?.let { body ->
                                            JSONObject(body).getString("error")
                                        } ?: response.body()?.error ?: "Failed to save budget and location"
                                    } catch (_: Exception) {
                                        "Failed to save budget and location"
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
                loading = isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetLocationScreenPreview() {
    RoomshareAIRentalAppTheme {
        BudgetLocationScreen()
    }
}
