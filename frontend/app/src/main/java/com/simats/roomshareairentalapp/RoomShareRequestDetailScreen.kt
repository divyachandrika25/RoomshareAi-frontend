package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomShareRequestDetailScreen(
    request: RoomShareRequestDetailData? = null,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onAcceptClick: (String) -> Unit = {},
    onDeclineClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Request Detail",
                onBackClick = onBackClick
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else if (request == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Request not found", color = AppTheme.TextSecondary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Requester Info Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = request.requesterPhoto,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = coil.compose.rememberAsyncImagePainter("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&q=80&w=200")
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = request.requesterName ?: "Unknown User",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = request.requesterEmail,
                            fontSize = 14.sp,
                            color = AppTheme.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { onChatClick(request.requesterEmail) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary.copy(alpha = 0.1f), contentColor = AppTheme.Primary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Message")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                StandardSectionHeader("Room Requested")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, null, tint = AppTheme.Primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(request.roomTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                StandardSectionHeader("Intro Message")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${request.introMessage}\"",
                        modifier = Modifier.padding(16.dp),
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                StandardSectionHeader("Proposed Details")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProposedDetailRow(Icons.Default.CalendarMonth, "Move-in Date", request.preferredMoveInDate)
                        HorizontalDivider(color = AppTheme.Divider)
                        ProposedDetailRow(Icons.Default.Schedule, "Stay Duration", request.durationOfStay)
                        HorizontalDivider(color = AppTheme.Divider)
                        ProposedDetailRow(Icons.Default.Work, "Employment", request.employmentStatus)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (request.status == "PENDING") {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(
                            onClick = { onDeclineClick(request.id) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Decline", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onAcceptClick(request.id) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Accept", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (request.status == "ACCEPTED") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status: ${request.status}",
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = if (request.status == "ACCEPTED") Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProposedDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 12.sp, color = AppTheme.TextTertiary)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomShareRequestDetailScreenPreview() {
    RoomshareAIRentalAppTheme {
        RoomShareRequestDetailScreen()
    }
}
