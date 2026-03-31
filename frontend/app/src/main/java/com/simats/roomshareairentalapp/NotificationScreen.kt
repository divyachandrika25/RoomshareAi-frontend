package com.simats.roomshareairentalapp

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    email: String = "",
    onBackClick: () -> Unit,
    onNotificationClick: (ApiNotification) -> Unit = {}
) {
    var notifications by remember { mutableStateOf<List<ApiNotification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            try {
                val response = RetrofitClient.instance.getNotifications(email)
                if (response.isSuccessful) {
                    notifications = response.body()?.notifications ?: emptyList()
                }
            } catch (_: Exception) {} finally { isLoading = false }
        } else { isLoading = false }
    }

    val unreadCount = notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Activity Feed",
                onBackClick = onBackClick,
                actions = {
                    if (unreadCount > 0) {
                        TextButton(onClick = {
                            scope.launch {
                                try {
                                    val response = RetrofitClient.instance.markAllNotificationsRead(mapOf("email" to email))
                                    if (response.isSuccessful) {
                                        notifications = notifications.map { it.copy(isRead = true) }
                                    }
                                } catch (_: Exception) {}
                            }
                        }) {
                            Icon(Icons.Default.Check, null, tint = AppTheme.Primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("MARK ALL READ", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary)
                        }
                    }
                }
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else if (notifications.isEmpty()) {
            EmptyActivityState()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("$unreadCount UNREAD UPDATES TRACKING YOUR STATUS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AppTheme.TextTertiary, letterSpacing = 1.sp)
                    
                    notifications.forEach { notif ->
                        NotificationCardComp(notif, onClick = { 
                            scope.launch {
                                if (!notif.isRead) {
                                    try {
                                        val response = RetrofitClient.instance.markNotificationRead(MarkNotificationReadRequest(notif.id))
                                        if (response.isSuccessful) {
                                            notifications = notifications.map { 
                                                if (it.id == notif.id) it.copy(isRead = true) else it
                                            }
                                        }
                                    } catch (_: Exception) {}
                                }
                            }
                            onNotificationClick(notif)
                        })
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    SafetyHubBannerComp()
                }
            }
        }
    }
}

@Composable
fun NotificationCardComp(notif: ApiNotification, onClick: () -> Unit) {
    val isUnread = !notif.isRead
    val typeIcon = when {
        notif.notificationType.contains("MATCH", true) -> Icons.Default.Groups
        notif.notificationType.contains("TOUR", true) -> Icons.Default.CalendarToday
        notif.notificationType.contains("BOOKING", true) -> Icons.Default.Home
        notif.notificationType.contains("CHAT", true) -> Icons.AutoMirrored.Filled.Chat
        else -> Icons.Default.Notifications
    }

    val iconColor = when {
        notif.notificationType.contains("MATCH", true) -> Color(0xFF10B981)
        notif.notificationType.contains("TOUR", true) -> Color(0xFF8B5CF6)
        notif.notificationType.contains("BOOKING", true) -> Color(0xFFF59E0B)
        notif.notificationType.contains("CHAT", true) -> Color(0xFF1E63FF)
        else -> AppTheme.Primary
    }

    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (isUnread) 1f else 0.7f).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isUnread) AppTheme.Primary.copy(alpha = 0.1f) else Color(0xFFF1F5F9))
    ) {
        Box {
            if (isUnread) {
                Box(modifier = Modifier.width(4.dp).height(40.dp).background(AppTheme.Primary, RoundedCornerShape(2.dp)).align(Alignment.CenterStart))
            }
            
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                Surface(
                    color = iconColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(52.dp),
                    border = BorderStroke(1.dp, iconColor.copy(alpha = 0.15f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(typeIcon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = AppTheme.Primary.copy(alpha = 0.08f), shape = RoundedCornerShape(6.dp)) {
                            Text(notif.notificationType.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                        Text(formatNotificationTime(notif.createdAt), fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.TextTertiary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(notif.title, fontSize = 15.sp, fontWeight = FontWeight.Black, color = AppTheme.TextPrimary)
                    Text(notif.message, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppTheme.TextSecondary, lineHeight = 18.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun SafetyHubBannerComp() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D1E3C), RoundedCornerShape(24.dp))
            .padding(24.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Shield, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
            Column {
                Text("Stay Safe & Private", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Always use RoomShare's secure chat to communicate before sharing personal details.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
            Button(
                onClick = { /* Safety Hub */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("SAFETY HUB", fontWeight = FontWeight.Black, fontSize = 12.sp, color = AppTheme.Primary)
            }
        }
    }
}

@Composable
fun EmptyActivityState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(modifier = Modifier.size(80.dp), color = AppTheme.Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(24.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.NotificationsOff, null, tint = AppTheme.Primary, modifier = Modifier.size(36.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("All caught up!", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AppTheme.TextPrimary)
        Text("No new notifications yet. We'll alert you later.", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.TextSecondary, textAlign = TextAlign.Center)
    }
}

fun formatNotificationTime(timestamp: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(timestamp)
        val now = Date()
        val diff = now.time - (date?.time ?: 0L)
        
        when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000}m ago"
            diff < 86400000 -> "${diff / 3600000}h ago"
            else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date ?: now)
        }
    } catch (_: Exception) { "Recent" }
}
