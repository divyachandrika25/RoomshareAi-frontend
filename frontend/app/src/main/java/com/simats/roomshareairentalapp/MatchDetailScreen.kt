package com.simats.roomshareairentalapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: String,
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {}
) {
    var matchDetail by remember { mutableStateOf<MatchDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(matchId) {
        try {
            val response = RetrofitClient.instance.getMatchDetail(matchId)
            if (response.isSuccessful) {
                matchDetail = response.body()
            } else {
                errorMessage = "Failed to load match details"
            }
        } catch (_: Exception) {
            errorMessage = "An error occurred"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Match Details",
                onBackClick = onBackClick
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Unknown error", color = Color.Red)
            }
        } else {
            matchDetail?.let { detail ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Profile Header
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.Surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = detail.photo ?: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=200",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = detail.fullName ?: "Unknown User",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.TextPrimary
                                )
                                Text(
                                    text = "${detail.age ?: "N/A"} years • ${detail.roomStatus ?: "Searching"}",
                                    fontSize = 14.sp,
                                    color = AppTheme.TextTertiary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF4CAF50),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${(detail.compatibilityScore).toInt()}% Match",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // AI Explanation
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = AppTheme.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI INSIGHT",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.Primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = detail.aiExplanation ?: "Our AI thinks you'll get along great based on your shared lifestyle preferences.",
                                fontSize = 14.sp,
                                color = AppTheme.TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    StandardSectionHeader(title = "Lifestyle & Preferences")
                    
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.Surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            StandardInfoRow(icon = Icons.Default.Schedule, label = "Sleep Schedule", value = detail.sleepSchedule ?: "N/A")
                            StandardInfoRow(icon = Icons.Default.CheckCircle, label = "Cleanliness", value = detail.cleanliness ?: "N/A")
                            StandardInfoRow(icon = Icons.Default.Person, label = "Social Style", value = detail.socialInteraction ?: "N/A")
                            StandardInfoRow(icon = Icons.Default.Star, label = "Monthly Budget", value = detail.monthlyBudget ?: "N/A")
                            StandardInfoRow(icon = Icons.Default.Room, label = "Preferred City", value = detail.preferredCity ?: "N/A")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onChatClick(detail.email) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Chatting", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
