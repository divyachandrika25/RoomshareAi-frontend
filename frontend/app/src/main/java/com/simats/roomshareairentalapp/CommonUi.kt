package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * App-wide high-contrast theme colors for perfect readability and a clean look.
 */
object AppTheme {
    val Primary = Color(0xFF1E63FF)
    val PrimaryDark = Color(0xFF0D1E3C)
    val Secondary = Color(0xFF9D1EFA)
    val Accent = Color(0xFF00D2FF)
    val Background = Color(0xFFF8F9FE)
    val Surface = Color.White
    
    // Semantic / Decorative
    val Emerald = Color(0xFF10B981)
    val Violet  = Color(0xFF7C3AED)
    val Amber   = Color(0xFFF59E0B)
    
    val PrimaryAlpha8  = Color(0x141E63FF)
    val PrimaryAlpha12 = Color(0x1F1E63FF)
    val PrimaryAlpha15 = Color(0x261E63FF)
    val PrimaryAlpha20 = Color(0x331E63FF)
    val PrimaryAlpha40 = Color(0x661E63FF)
    val PrimaryLight   = Color(0x141E63FF) // Alias for Alpha8
    
    // High-contrast, bright text colors
    val TextPrimary = Color(0xFF0D1E3C)   // Deep navy for titles
    val TextSecondary = Color(0xFF475569) // Slate for body
    val TextTertiary = Color(0xFF94A3B8)  // Light slate for labels
    
    val Success = Color(0xFF10B981)
    val Error = Color(0xFFEF4444)
    val ErrorAlpha = Color(0x1AEF4444)
    val ErrorAlpha8 = Color(0x14EF4444)
    val Warning = Color(0xFFF59E0B)
    
    val Divider = Color(0xFFF1F5F9)
    val Border = Color(0xFFE2E8F0)

    // Slate palette
    val Slate50  = Color(0xFFF8FAFC)
    val Slate100 = Color(0xFFF1F5F9)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate300 = Color(0xFFCBD5E1)
    val Slate400 = Color(0xFF94A3B8)
    val Slate500 = Color(0xFF64748B)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate800 = Color(0xFF1E293B)
    val Slate900 = Color(0xFF0F172A)

    // Blue palette
    val Blue50  = Color(0xFFEFF6FF)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue200 = Color(0xFFBFDBFE)
    val Blue300 = Color(0xFF93C5FD)
    val Blue400 = Color(0xFF60A5FA)
    val Blue500 = Color(0xFF3B82F6)
    val Blue600 = Color(0xFF2563EB)
    val Blue700 = Color(0xFF1D4ED8)
    val Blue800 = Color(0xFF1E40AF)
    val Blue900 = Color(0xFF1E3A8A)

    // Red/Rose
    val Rose = Color(0xFFF43F5E)
    val Red500 = Color(0xFFEF4444)

    // Indigo
    val Indigo500 = Color(0xFF6366F1)
    val Indigo600 = Color(0xFF4F46E5)

    // Gold
    val Gold = Color(0xFFFFD700)

    // Radii
    val RadiusSm = 8.dp
    val RadiusMd = 12.dp
    val RadiusLg = 16.dp
    val RadiusXl = 20.dp
    val Radius2Xl = 28.dp

    // Type Scale
    val LabelCaps = 9.sp
    val LabelXs = 10.sp
    val BodySm = 12.sp
    val BodyMd = 13.sp
    val BodyLg = 14.sp
    val TitleMd = 15.sp
    val TitleLg = 17.sp
    val Heading = 20.sp
    val Display = 24.sp
    val DisplayLg = 32.sp
    
    val PremiumGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF1E63FF), Color(0xFF6E9FFF))
    )
    
    val HeaderGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF1E40AF), Color(0xFF1E63FF), Color(0xFF6366F1))
    )
}

/**
 * Standardized Top Bar for all screens to ensure alignment and spacing are perfect.
 * Features optional back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { 
            Text(
                text = title, 
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 20.sp,
                color = AppTheme.TextPrimary
            ) 
        },
        navigationIcon = {
            if (navigationIcon != null) {
                navigationIcon()
            } else if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        tint = AppTheme.TextPrimary
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = AppTheme.Surface
        ),
        modifier = Modifier.statusBarsPadding().fillMaxWidth()
    )
}

/**
 * Uniform section header used across various screens.
 */
@Composable
fun StandardSectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AppTheme.TextPrimary,
        modifier = modifier.padding(vertical = 12.dp)
    )
}

/**
 * Brighter Info Row for displaying user details or room attributes.
 */
@Composable
fun StandardInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF0F5FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, modifier = Modifier.size(20.dp), tint = AppTheme.Primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = AppTheme.TextTertiary, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
        }
    }
}

/**
 * Reusable menu button for profile and settings screens.
 */
@Composable
fun StandardMenuButton(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = AppTheme.Surface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF0F5FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = AppTheme.Primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title, 
                modifier = Modifier.weight(1f), 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Bold, 
                color = AppTheme.TextPrimary
            )
            Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(20.dp), tint = AppTheme.TextTertiary)
        }
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = content
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled) AppTheme.PremiumGradient else Brush.linearGradient(listOf(Color.LightGray, Color.Gray)))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EmptyStateComp(
    title: String,
    subtitle: String = "",
    icon: ImageVector = Icons.Default.Inbox,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            color = AppTheme.Primary.copy(alpha = 0.05f),
            shape = CircleShape
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, modifier = Modifier.size(32.dp), tint = AppTheme.TextTertiary)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(title, color = AppTheme.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        if (subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(subtitle, color = AppTheme.TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
        if (action != null) {
            Spacer(modifier = Modifier.height(24.dp))
            action()
        }
    }
}

@Composable
fun AICtaBannerComp(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PremiumCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(listOf(AppTheme.Primary, Color(0xFF7C3AED))),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Need help finding a flatmate?", fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary, fontSize = 15.sp)
                Text("Ask our AI Assistant for help", fontSize = 12.sp, color = AppTheme.TextSecondary, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.Default.ChevronRight, null, tint = AppTheme.TextTertiary)
        }
    }
}

@Composable
fun SectionHeaderComp(title: String, subtitle: String, onViewAll: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(subtitle, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Primary, letterSpacing = 1.sp)
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary)
        }
        if (onViewAll != null) {
            Text(
                "View All",
                modifier = Modifier.clickable { onViewAll() },
                color = AppTheme.Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * A sleek, high-fidelity loading overlay to be used during navigation or heavy processing.
 * Prevents multiple clicks by blocking touch interactions.
 */
@Composable
fun FullScreenLoadingOverlay(message: String = "Loading details...") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(enabled = false) { } // Intercept and block clicks
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp),
                    color = Color(0xFF1E63FF),
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D1E3C),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun BudgetRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = AppTheme.TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary, fontSize = 14.sp)
    }
}
