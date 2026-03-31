package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    userEmail: String,
    userName: String,
    userProfileImage: String,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Drawer Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.linearGradient(listOf(AppTheme.Primary, Color(0xFF0D1E3C))))
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (userProfileImage.isNotBlank()) {
                                // In a real app, use AsyncImage. For drawer simplicity, we use a placeholder or icon if image fails
                                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
                            } else {
                                Text(
                                    text = userName.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(userName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(userEmail, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DrawerSectionLabel("Main Menu")
                DrawerItem(Icons.Default.Home, "Home", "home", currentScreen, onNavigate)
                DrawerItem(Icons.Default.People, "Matches", "matches", currentScreen, onNavigate)
                DrawerItem(Icons.Default.Chat, "Messages", "messages_list", currentScreen, onNavigate)
                DrawerItem(Icons.Default.AutoAwesome, "AI Search", "ai_assistant", currentScreen, onNavigate, isPremium = true)
                DrawerItem(Icons.Default.Business, "Hotels & Stays", "hotel_list", currentScreen, onNavigate)
                DrawerItem(Icons.Default.SmartToy, "AI Chatbot", "ai_chatbot", currentScreen, onNavigate, isPremium = true)

                Spacer(modifier = Modifier.height(16.dp))
                DrawerSectionLabel("Personal")
                DrawerItem(Icons.Default.Person, "My Profile", "profile", currentScreen, onNavigate)
                DrawerItem(Icons.Default.Favorite, "Saved Roommates", "saved", currentScreen, onNavigate)
                DrawerItem(Icons.Default.History, "Booking History", "booking_history", currentScreen, onNavigate)
                DrawerItem(Icons.Default.Notifications, "Notifications", "notifications", currentScreen, onNavigate)

                Spacer(modifier = Modifier.height(16.dp))
                DrawerSectionLabel("Other")
                DrawerItem(Icons.Default.Settings, "Account Settings", "account_settings", currentScreen, onNavigate)
                DrawerItem(Icons.Default.Star, "Premium Subscription", "subscription", currentScreen, onNavigate)
                DrawerItem(Icons.Default.Security, "Privacy & Safety", "privacy_security", currentScreen, onNavigate)

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Sign Out", color = Color(0xFFEF4444), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerSectionLabel(label: String) {
    Text(
        text = label.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        color = AppTheme.TextTertiary,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    screenId: String,
    currentScreen: String,
    onNavigate: (String) -> Unit,
    isPremium: Boolean = false
) {
    val isSelected = currentScreen == screenId
    Surface(
        onClick = { onNavigate(screenId) },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = if (isSelected) AppTheme.Primary.copy(alpha = 0.1f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, 
                null, 
                tint = if (isSelected) AppTheme.Primary else if (isPremium) Color(0xFFF59E0B) else AppTheme.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = if (isSelected) AppTheme.Primary else AppTheme.TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (isPremium) {
                Badge(
                    containerColor = Color(0xFFFEF3C7),
                    contentColor = Color(0xFFD97706)
                ) {
                    Text("AI", fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
