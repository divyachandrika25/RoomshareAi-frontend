package com.simats.roomshareairentalapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
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
fun LifestyleHabitsScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onNextStepClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSavedClick: () -> Unit = {}
) {
    var sleepSchedule by remember { mutableStateOf("Balanced") }
    var cleanliness by remember { mutableStateOf("Organized") }
    var socialInteraction by remember { mutableStateOf("Moderate") }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                            tint = Color(0xFF0D1E3C)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color(0xFF0D1E3C))
                    }
                    IconButton(onClick = onSavedClick) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Saved", tint = Color(0xFF0D1E3C))
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
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lifestyle & Habits",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D1E3C)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "AI uses these to find your most compatible roommates.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                HabitSection(
                    title = "SLEEP SCHEDULE",
                    options = listOf("Early Bird", "Night Owl", "Balanced"),
                    selectedOption = sleepSchedule,
                    onOptionSelected = { sleepSchedule = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                HabitSection(
                    title = "CLEANLINESS",
                    options = listOf("Minimalist", "Organized", "Relaxed"),
                    selectedOption = cleanliness,
                    onOptionSelected = { cleanliness = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                HabitSection(
                    title = "SOCIAL INTERACTION",
                    options = listOf("Introvert", "Extrovert", "Moderate"),
                    selectedOption = socialInteraction,
                    onOptionSelected = { socialInteraction = it }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            GradientButton(
                text = "Next Step",
                onClick = {
                    if (email.isEmpty()) {
                        scope.launch { snackbarHostState.showSnackbar("Please ensure you are logged in.") }
                        return@GradientButton
                    }
                    scope.launch {
                        isLoading = true
                        try {
                            val request = LifestyleRequest(
                                email = email,
                                sleepSchedule = sleepSchedule,
                                cleanliness = cleanliness,
                                socialInteraction = socialInteraction
                            )
                            val response = RetrofitClient.instance.lifestyle(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                onNextStepClick()
                            } else {
                                val errorMsg = response.errorBody()?.string()?.let { body ->
                                    try {
                                        JSONObject(body).getString("error")
                                    } catch (_: Exception) {
                                        "Failed to save lifestyle"
                                    }
                                } ?: response.body()?.error ?: "Failed to save lifestyle"
                                snackbarHostState.showSnackbar(errorMsg)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Network error: ${e.message}")
                        } finally {
                            isLoading = false
                        }
                    }
                },
                loading = isLoading
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HabitSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            letterSpacing = 0.5.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                HabitChip(
                    text = option,
                    isSelected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun HabitChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) AppTheme.Primary else AppTheme.Divider
        ),
        color = if (isSelected) Color(0xFFF1F5FE) else Color.White,
        modifier = modifier.height(52.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) AppTheme.Primary else AppTheme.TextTertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LifestyleHabitsScreenPreview() {
    RoomshareAIRentalAppTheme {
        LifestyleHabitsScreen()
    }
}
