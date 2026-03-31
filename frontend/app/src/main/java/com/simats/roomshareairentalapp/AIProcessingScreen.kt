package com.simats.roomshareairentalapp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import kotlinx.coroutines.delay

@Composable
fun AIProcessingScreen(
    onProcessingFinished: () -> Unit = {}
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000)
        )
        delay(500) // Small delay after animation completes
        onProcessingFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Circular AI Icon with Percentage
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(AppTheme.Primary)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(progress.value * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Text Labels
        Text(
            text = "AI is at Work",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Matching with 50,000+ Profiles...",
            fontSize = 16.sp,
            color = AppTheme.TextSecondary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = AppTheme.Primary,
            trackColor = Color(0xFFF0F4FF),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "PROCESSING CLOUD DATA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.Primary.copy(alpha = 0.6f),
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AIProcessingScreenPreview() {
    RoomshareAIRentalAppTheme {
        AIProcessingScreen()
    }
}
