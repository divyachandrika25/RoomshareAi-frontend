package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Work
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    formData: RoomShareFormData? = null,
    onBackClick: () -> Unit = {},
    onContinueClick: (moveInDate: String, stayDuration: String, employmentStatus: String) -> Unit = { _, _, _ -> }
) {
    var moveInDate by remember { mutableStateOf("") }
    var stayDuration by remember { mutableStateOf("") }
    var employmentStatus by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val showSelectionDialog = remember { mutableStateOf(false) }
    val selectionTitle = remember { mutableStateOf("") }
    val selectionOptions = remember { mutableStateOf(listOf<String>()) }
    val onOptionSelected = remember { mutableStateOf<(String) -> Unit>({}) }

    val roommateName = formData?.ownerName ?: "Sarah Jenkins"

    // Handle Date Selection
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        moveInDate = formatter.format(date)
                    }
                    showDatePicker.value = false
                }) {
                    Text("OK", color = Color(0xFF1E63FF))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Handle Option Selection
    if (showSelectionDialog.value) {
        AlertDialog(
            onDismissRequest = { showSelectionDialog.value = false },
            title = { Text("Select ${selectionTitle.value}", color = Color(0xFF0D1E3C), fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(selectionOptions.value) { option ->
                        ListItem(
                            headlineContent = { Text(option, color = Color(0xFF0D1E3C)) },
                            modifier = Modifier.clickable {
                                onOptionSelected.value(option)
                                showSelectionDialog.value = false
                            }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSelectionDialog.value = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Available Rooms",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF0D1E3C)
                        )
                    },
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
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF0D1E3C)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                // Progress Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (index <= 1) Color(0xFF1E63FF) else Color(0xFFF1F5FE))
                        )
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (moveInDate.isBlank() || stayDuration.isBlank() || employmentStatus.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill all details")
                        }
                    } else {
                        onContinueClick(moveInDate, stayDuration, employmentStatus)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E63FF)
                )
            ) {
                Text(
                    "Submit Application",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Your Details",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0D1E3C)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Help $roommateName understand your requirements.",
                fontSize = 16.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            DetailItem(
                label = "PREFERRED MOVE-IN DATE",
                value = if (moveInDate.isEmpty()) "Select Date" else moveInDate,
                icon = Icons.Default.CalendarMonth,
                onClick = {
                    showDatePicker.value = true
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            DetailItem(
                label = "DURATION OF STAY",
                value = if (stayDuration.isEmpty()) "Select Duration" else stayDuration,
                icon = Icons.Default.CalendarMonth,
                onClick = {
                    selectionTitle.value = "Duration of Stay"
                    selectionOptions.value = formData?.durationOptions ?: listOf("3 Months", "6 Months", "12 Months", "24+ Months")
                    onOptionSelected.value = { stayDuration = it }
                    showSelectionDialog.value = true
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            DetailItem(
                label = "EMPLOYMENT STATUS",
                value = if (employmentStatus.isEmpty()) "Select Status" else employmentStatus,
                icon = Icons.Default.Work,
                onClick = {
                    selectionTitle.value = "Employment Status"
                    selectionOptions.value = formData?.employmentOptions ?: listOf("Full-time", "Part-time", "Student", "Unemployed")
                    onOptionSelected.value = { employmentStatus = it }
                    showSelectionDialog.value = true
                }
            )
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8F9FE),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF1E63FF),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0D1E3C)
                    )
                }
                Text(
                    text = "Edit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E63FF)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    RoomshareAIRentalAppTheme {
        DetailsScreen()
    }
}
