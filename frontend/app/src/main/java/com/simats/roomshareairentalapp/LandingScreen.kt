package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@Composable
fun LandingScreen(onGetStarted: () -> Unit) {
    val backgroundGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF0F7FF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logos and Home Icon Card
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Saveetha Logo
                AsyncImage(
                    model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR0t_W6P_NfP0-3E6B4H-f-oG7X6XyF7vY9Sg&s",
                    contentDescription = "Saveetha Logo",
                    modifier = Modifier.size(70.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))

                // Logo Card (The "Home Image")
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(20.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.app_logo),
                            contentDescription = "RoomShare Logo",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // SSE Logo
                AsyncImage(
                    model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_6M2p_XmQ_9xVnC1_vY3P8T1z_4_Q_z_6_S_8&s",
                    contentDescription = "SSE Logo",
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title and Sparkle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "SmartUrban",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D1B2A)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFF4FC3F7)
                )
            }

            Text(
                text = "Smart Renting with AI",
                fontSize = 18.sp,
                color = Color(0xFF546E7A),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Features List
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.wrapContentWidth()
            ) {
                FeatureItem("AI-Powered Apartment Matching")
                Spacer(modifier = Modifier.height(16.dp))
                FeatureItem("Smart Roommate Compatibility")
                Spacer(modifier = Modifier.height(16.dp))
                FeatureItem("Fraud Detection & Safety")
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Get Started Button
            GradientButton(
                text = "Get Started",
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Version 2.0",
                fontSize = 14.sp,
                color = Color(0xFF90A4AE)
            )
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(0xFF00B0FF), CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF455A64),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    RoomshareAIRentalAppTheme {
        LandingScreen(onGetStarted = {})
    }
}
