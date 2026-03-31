package com.simats.roomshareairentalapp

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@Composable
fun BottomNavigationBar(
    currentScreen: String = "home",
    onNavigate: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            icon = { Icon(if (currentScreen == "home") Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("HOME", fontWeight = if (currentScreen == "home") FontWeight.Bold else FontWeight.Medium, fontSize = 10.sp) },
            selected = currentScreen == "home",
            onClick = { onNavigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppTheme.Primary,
                unselectedIconColor = AppTheme.TextTertiary,
                selectedTextColor = AppTheme.Primary,
                unselectedTextColor = AppTheme.TextTertiary,
                indicatorColor = Color(0xFFE8F0FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Group, contentDescription = "Matches") },
            label = { Text("MATCHES", fontWeight = if (currentScreen == "matches") FontWeight.Bold else FontWeight.Medium, fontSize = 10.sp) },
            selected = currentScreen == "matches",
            onClick = { onNavigate("matches") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppTheme.Primary,
                unselectedIconColor = AppTheme.TextTertiary,
                selectedTextColor = AppTheme.Primary,
                unselectedTextColor = AppTheme.TextTertiary,
                indicatorColor = Color(0xFFE8F0FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Business, contentDescription = "Hotels") },
            label = { Text("HOTELS", fontWeight = if (currentScreen == "hotel_list") FontWeight.Bold else FontWeight.Medium, fontSize = 10.sp) },
            selected = currentScreen == "hotel_list",
            onClick = { onNavigate("hotel_list") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppTheme.Primary,
                unselectedIconColor = AppTheme.TextTertiary,
                selectedTextColor = AppTheme.Primary,
                unselectedTextColor = AppTheme.TextTertiary,
                indicatorColor = Color(0xFFE8F0FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat") },
            label = { Text("CHAT", fontWeight = if (currentScreen == "messages_list") FontWeight.Bold else FontWeight.Medium, fontSize = 10.sp) },
            selected = currentScreen == "messages_list",
            onClick = { onNavigate("messages_list") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppTheme.Primary,
                unselectedIconColor = AppTheme.TextTertiary,
                selectedTextColor = AppTheme.Primary,
                unselectedTextColor = AppTheme.TextTertiary,
                indicatorColor = Color(0xFFE8F0FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = "Profile") },
            label = { Text("PROFILE", fontWeight = if (currentScreen == "profile") FontWeight.Bold else FontWeight.Medium, fontSize = 10.sp) },
            selected = currentScreen == "profile",
            onClick = { onNavigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppTheme.Primary,
                unselectedIconColor = AppTheme.TextTertiary,
                selectedTextColor = AppTheme.Primary,
                unselectedTextColor = AppTheme.TextTertiary,
                indicatorColor = Color(0xFFE8F0FF)
            )
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    RoomshareAIRentalAppTheme {
        BottomNavigationBar()
    }
}
