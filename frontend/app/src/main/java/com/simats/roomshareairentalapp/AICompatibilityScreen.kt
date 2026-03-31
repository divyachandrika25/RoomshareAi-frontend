package com.simats.roomshareairentalapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AICompatibilityScreen(
    currentEmail: String,
    targetEmail: String,
    onBackClick: () -> Unit
) {
    var compatibilityData by remember { mutableStateOf<AICompatibilityData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(currentEmail, targetEmail) {
        try {
            val response = RetrofitClient.instance.getAICompatibility(currentEmail, targetEmail)
            if (response.isSuccessful) {
                compatibilityData = response.body()?.data
            }
        } catch (_: Exception) {} finally { isLoading = false }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Harmony Insight",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, null, tint = AppTheme.TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else if (compatibilityData != null) {
            val data = compatibilityData!!
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                // Gradient Header Score
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(Brush.linearGradient(listOf(Color(0xFF1E63FF), Color(0xFF0D1E3C)))),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("AI HARMONY INSIGHT", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF93C5FD), letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("${data.totalMatch.toInt()}", fontSize = 72.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text("%", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF93C5FD), modifier = Modifier.padding(bottom = 16.dp))
                        }
                        Text("CO-LIVING COMPATIBILITY", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF93C5FD).copy(alpha = 0.7f), letterSpacing = 0.5.sp)
                    }
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    // Headline Narrative
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
                            Box(modifier = Modifier.width(4.dp).height(40.dp).background(AppTheme.Primary.copy(alpha = 0.3f), RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "\"${data.headline}\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Breakdown Items
                    data.breakdown.forEach { item ->
                        HarmonyBreakdownItem(item)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Conflict Detection / Security Protocol
                    SecurityProtocolCard(data.conflictDetection)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("ACKNOWLEDGE INSIGHT", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun HarmonyBreakdownItem(item: CompatibilityBreakdown) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(item.title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Black, color = AppTheme.TextPrimary, letterSpacing = 0.5.sp)
            Surface(color = AppTheme.Primary.copy(alpha = 0.08f), shape = RoundedCornerShape(6.dp)) {
                Text("${item.score}%", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp))) {
            Box(modifier = Modifier.fillMaxWidth(item.score / 100f).fillMaxHeight().background(AppTheme.Primary, RoundedCornerShape(4.dp)))
        }
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(item.note, fontSize = 12.sp, color = AppTheme.TextTertiary)
    }
}

@Composable
fun SecurityProtocolCard(conflict: ConflictDetection) {
    val isRisk = conflict.title.contains("Moderate") || conflict.title.contains("High") || conflict.title.contains("Risk")
    val bgColor = if (isRisk) Color(0xFFFFF1F2) else Color(0xFFF0FDF4)
    val borderColor = if (isRisk) Color(0xFFFECDD3) else Color(0xFFDCFCE7)
    val iconColor = if (isRisk) Color(0xFFF43F5E) else Color(0xFF10B981)
    val textColor = if (isRisk) Color(0xFF9F1239) else Color(0xFF166534)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, null, tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("SECURITY PROTOCOL", fontSize = 10.sp, fontWeight = FontWeight.Black, color = textColor, letterSpacing = 1.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(conflict.title.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Black, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(conflict.message, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textColor.copy(alpha = 0.7f), lineHeight = 18.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AICompatibilityScreenPreview() {
    RoomshareAIRentalAppTheme {
        AICompatibilityScreen("test@user.com", "test@target.com", {})
    }
}
