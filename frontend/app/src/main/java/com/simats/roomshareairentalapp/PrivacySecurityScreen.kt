package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySecurityScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            StandardTopBar(title = "Privacy & Security", onBackClick = onBackClick)
        },
        containerColor = AppTheme.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SecurityStatusCard()

            PrivacySection(
                title = "Data Protection",
                description = "Your personal data and AI matching preferences are encrypted using industry-standard protocols. We never share your sensitive information with third parties without your explicit consent.",
                icon = Icons.Default.Lock
            )

            PrivacySection(
                title = "Profile Visibility",
                description = "You can control who sees your profile. By default, only verified potential roommates with a high harmony score can view your full details.",
                icon = Icons.Default.Visibility
            )

            PrivacySection(
                title = "AI Usage",
                description = "Our AI processing happens securely. The data provided in your lifestyle habits and budget is used solely to improve matching accuracy and provide insights.",
                icon = Icons.Default.GppGood
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Last updated: October 2024",
                fontSize = 12.sp,
                color = AppTheme.TextTertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun SecurityStatusCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.GppGood, 
                contentDescription = null, 
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Your account is secure", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Text("AI-driven protection is active", fontSize = 12.sp, color = Color(0xFF2E7D32).copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
fun PrivacySection(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 1.dp
        ) {
            Text(
                text = description,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacySecurityScreenPreview() {
    RoomshareAIRentalAppTheme {
        PrivacySecurityScreen()
    }
}
