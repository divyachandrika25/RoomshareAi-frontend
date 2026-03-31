package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ─────────────────────────────────────────
//  DESIGN TOKENS — Single source of truth
// ─────────────────────────────────────────
object ProfileDesign {
    // Primary blue palette
    val Blue50  = Color(0xFFEFF6FF)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue200 = Color(0xFFBFDBFE)
    val Blue400 = Color(0xFF60A5FA)
    val Blue500 = Color(0xFF3B82F6)
    val Blue600 = Color(0xFF2563EB)
    val Blue700 = Color(0xFF1D4ED8)
    val Blue800 = Color(0xFF1E40AF)
    val Blue900 = Color(0xFF1E3A8A)

    // Accent — electric indigo
    val Indigo400 = Color(0xFF818CF8)
    val Indigo500 = Color(0xFF6366F1)
    val Indigo600 = Color(0xFF4F46E5)

    // Neutrals
    val Slate50  = Color(0xFFF8FAFC)
    val Slate100 = Color(0xFFF1F5F9)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate400 = Color(0xFF94A3B8)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate900 = Color(0xFF0F172A)

    // Semantic
    val Success  = Color(0xFF10B981)
    val Rose     = Color(0xFFF43F5E)
}

// ─────────────────────────────────────────
//  PROFILE SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit = {},
    onAccountSettingsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onBookingHistoryClick: () -> Unit = {},
    onSavedRoomsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onAIChatbotClick: () -> Unit = {},
    onHotelsClick: () -> Unit = {},
    fullName: String = "",
    email: String = "",
    bio: String = "",
    occupation: String = "",
    age: String = "",
    targetArea: String = "",
    budget: String = "",
    moveInDate: String = "",
    profileImage: String = "",
    isPremium: Boolean = false,
    onUpdateProfile: (String, String, String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _, _, _ -> }
) {
    var isEditing by remember { mutableStateOf(false) }
    var localFullName     by remember { mutableStateOf(fullName) }
    var localBio          by remember { mutableStateOf(bio) }
    var localOccupation   by remember { mutableStateOf(occupation) }
    var localAge          by remember { mutableStateOf(age) }
    var localTargetArea   by remember { mutableStateOf(targetArea) }
    var localBudget       by remember { mutableStateOf(budget) }
    var localMoveInDate   by remember { mutableStateOf(moveInDate) }
    var localProfileImage by remember { mutableStateOf(profileImage) }

    val progress = remember(
        localFullName, localBio, localOccupation,
        localAge, localTargetArea, localBudget, localMoveInDate
    ) {
        listOf(
            localFullName, localBio, localOccupation,
            localAge, localTargetArea, localBudget, localMoveInDate
        ).count { it.isNotBlank() } / 7f
    }

    Scaffold(
        containerColor = ProfileDesign.Slate50,
        topBar = {
            ProfileTopBar(
                isEditing = isEditing,
                onBackClick = onBackClick,
                onEditSave = {
                    if (isEditing) onUpdateProfile(
                        localFullName, localBio, localOccupation,
                        localAge, localTargetArea, localBudget,
                        localMoveInDate, localProfileImage
                    )
                    isEditing = !isEditing
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Hero Banner ──
            ProfileHero(
                localFullName = localFullName,
                email = email,
                localProfileImage = localProfileImage,
                progress = progress,
                isPremium = isPremium
            )

            // ── Content ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-24).dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Personal Identity
                ProfileSection(
                    icon = Icons.Default.Person,
                    iconBg = ProfileDesign.Blue600,
                    title = "Personal Identity",
                    subtitle = "Basic details for professional matching"
                ) {
                    ProfileField(Icons.Default.Person,        "Full Name",  localFullName,   isEditing) { localFullName = it }
                    ProfileField(Icons.Default.CalendarToday, "Age",        localAge,        isEditing) { localAge = it }
                    ProfileField(Icons.Default.Work,          "Occupation", localOccupation, isEditing) { localOccupation = it }
                }

                // Roommate Preferences
                ProfileSection(
                    icon = Icons.Default.AutoAwesome,
                    iconBg = ProfileDesign.Indigo500,
                    title = "Roommate Profile",
                    subtitle = "Preferences for AI-powered discovery"
                ) {
                    ProfileField(Icons.Default.Map,      "Target Area",    localTargetArea, isEditing) { localTargetArea = it }
                    ProfileField(Icons.Default.Payments, "Monthly Budget", localBudget,     isEditing) { localBudget = it }
                    ProfileField(Icons.Default.Event,    "Move-in Date",   localMoveInDate, isEditing) { localMoveInDate = it }
                }

                // About Me
                ProfileSection(
                    icon = Icons.AutoMirrored.Filled.Message,
                    iconBg = ProfileDesign.Blue500,
                    title = "About Me",
                    subtitle = "Your lifestyle and personality"
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = localBio,
                            onValueChange = { localBio = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "Tell potential roommates about yourself…",
                                    fontSize = 14.sp,
                                    color = ProfileDesign.Slate400
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ProfileDesign.Blue500,
                                unfocusedBorderColor = ProfileDesign.Slate200,
                                cursorColor = ProfileDesign.Blue600
                            ),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = ProfileDesign.Slate900,
                                lineHeight = 21.sp
                            )
                        )
                    } else {
                        Text(
                            localBio.ifBlank { "No bio added yet. A complete profile increases your match rate." },
                            fontSize = 14.sp,
                            color = if (localBio.isBlank()) ProfileDesign.Slate400 else ProfileDesign.Slate700,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Account Management
                AccountManagementSection(
                    onHotelsClick        = onHotelsClick,
                    onAIChatbotClick     = onAIChatbotClick,
                    onBookingHistoryClick = onBookingHistoryClick,
                    onSavedRoomsClick    = onSavedRoomsClick,
                    onAccountSettingsClick = onAccountSettingsClick,
                    onPrivacyClick       = onPrivacyClick,
                    onHelpClick          = onHelpClick
                )

                // Logout
                LogoutButton(onLogout = onLogout)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    isEditing: Boolean,
    onBackClick: () -> Unit,
    onEditSave: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(ProfileDesign.Blue50, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ArrowBack, "Back",
                        tint = ProfileDesign.Blue700,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                "My Profile",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ProfileDesign.Slate900
            )
            Spacer(Modifier.weight(1f))
            // Edit / Save
            Surface(
                color = if (isEditing) ProfileDesign.Blue600 else ProfileDesign.Blue50,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onEditSave() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                        null,
                        tint = if (isEditing) Color.White else ProfileDesign.Blue700,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (isEditing) "Save" else "Edit",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEditing) Color.White else ProfileDesign.Blue700
                    )
                }
            }
        }
        HorizontalDivider(color = ProfileDesign.Slate100)
    }
}

// ─────────────────────────────────────────
//  HERO BANNER
// ─────────────────────────────────────────
@Composable
private fun ProfileHero(
    localFullName: String,
    email: String,
    localProfileImage: String,
    progress: Float,
    isPremium: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Gradient background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.linearGradient(
                        listOf(ProfileDesign.Blue800, ProfileDesign.Blue600, ProfileDesign.Indigo500)
                    )
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (-60).dp, y = (-50).dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-30).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
        }

        // Avatar Card — overlapping
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = localProfileImage.takeIf { it.isNotBlank() }
                        ?: "https://ui-avatars.com/api/?name=${localFullName.ifBlank { "User" }}&background=2563EB&color=fff&size=200",
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .border(3.dp, Color.White, RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
                // Online indicator
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .background(ProfileDesign.Success, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }
    }

    // Name + email + progress below hero
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            localFullName.ifBlank { "Your Name" },
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = ProfileDesign.Slate900,
            letterSpacing = (-0.3).sp
        )
        if (email.isNotBlank()) {
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(email, fontSize = 13.sp, color = ProfileDesign.Slate400, fontWeight = FontWeight.Medium)
                if (isPremium) {
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        color = Color(0xFFFFD700).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = Color(0xFFDAA520),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "PREMIUM",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFDAA520),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Profile completeness card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(ProfileDesign.Blue50, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser, null,
                                tint = ProfileDesign.Blue600,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Profile Reliability Index",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ProfileDesign.Slate700
                        )
                    }
                    Surface(
                        color = if (progress >= 1f) ProfileDesign.Success.copy(alpha = 0.12f)
                                else ProfileDesign.Blue50,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "${(progress * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (progress >= 1f) ProfileDesign.Success else ProfileDesign.Blue600,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (progress >= 1f) ProfileDesign.Success else ProfileDesign.Blue600,
                    trackColor = ProfileDesign.Slate100
                )
                if (progress < 1f) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Complete your profile to improve AI matching accuracy",
                        fontSize = 11.sp,
                        color = ProfileDesign.Slate400,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  PROFILE SECTION CARD
// ─────────────────────────────────────────
@Composable
private fun ProfileSection(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp),
                ambientColor = ProfileDesign.Blue900.copy(alpha = 0.06f),
                spotColor   = ProfileDesign.Blue700.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = ProfileDesign.Slate900)
                    Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = ProfileDesign.Slate400)
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = ProfileDesign.Slate100
            )
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                content()
            }
        }
    }
}

// ─────────────────────────────────────────
//  PROFILE FIELD ROW
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = if (isEditing) Alignment.CenterVertically else Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(ProfileDesign.Blue50, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = ProfileDesign.Blue600, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = ProfileDesign.Slate400,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(2.dp))
            if (isEditing) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor   = ProfileDesign.Blue50,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor   = ProfileDesign.Blue600,
                        unfocusedIndicatorColor = ProfileDesign.Slate200,
                        cursorColor             = ProfileDesign.Blue600
                    ),
                    textStyle = TextStyle(
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = ProfileDesign.Slate900
                    )
                )
            } else {
                Text(
                    text       = value.ifBlank { "Not set" },
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = if (value.isBlank()) ProfileDesign.Slate400 else ProfileDesign.Slate900
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  ACCOUNT MANAGEMENT SECTION
// ─────────────────────────────────────────
@Composable
private fun AccountManagementSection(
    onHotelsClick: () -> Unit,
    onAIChatbotClick: () -> Unit,
    onBookingHistoryClick: () -> Unit,
    onSavedRoomsClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp),
                ambientColor = ProfileDesign.Blue900.copy(alpha = 0.06f),
                spotColor   = ProfileDesign.Blue700.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            // Section label
            Text(
                "Account Management",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ProfileDesign.Slate400,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                letterSpacing = 0.3.sp
            )
            HorizontalDivider(color = ProfileDesign.Slate100)

            data class MenuItem(val icon: ImageVector, val label: String, val badge: String? = null, val action: () -> Unit)
            val items = listOf(
                MenuItem(Icons.Default.Business,  "Explore Hotels",    action = onHotelsClick),
                MenuItem(Icons.Default.AutoAwesome,"AI Chatbot",        badge = "NEW", action = onAIChatbotClick),
                MenuItem(Icons.Default.History,   "Booking History",   action = onBookingHistoryClick),
                MenuItem(Icons.Default.Bookmark,  "Saved Rooms",       action = onSavedRoomsClick),
                MenuItem(Icons.Default.Settings,  "Account Settings",  action = onAccountSettingsClick),
                MenuItem(Icons.Default.Security,  "Privacy & Data",    action = onPrivacyClick),
                MenuItem(Icons.Default.HelpOutline,"Help & Support",   action = onHelpClick)
            )

            items.forEachIndexed { idx, item ->
                AccountMenuItem(
                    icon   = item.icon,
                    label  = item.label,
                    badge  = item.badge,
                    onClick = item.action
                )
                if (idx < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp, end = 20.dp),
                        color = ProfileDesign.Slate100
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountMenuItem(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(ProfileDesign.Blue50, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = ProfileDesign.Blue600, modifier = Modifier.size(17.dp))
        }
        Spacer(Modifier.width(14.dp))
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ProfileDesign.Slate900,
            modifier = Modifier.weight(1f)
        )
        if (badge != null) {
            Surface(
                color = ProfileDesign.Indigo500,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    badge,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    letterSpacing = 0.5.sp
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight, null,
            tint = ProfileDesign.Slate400,
            modifier = Modifier.size(17.dp)
        )
    }
}

// ─────────────────────────────────────────
//  LOGOUT BUTTON
// ─────────────────────────────────────────
@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onLogout() },
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFF1F2),
        border = BorderStroke(1.dp, Color(0xFFFECDD3))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFE4E6), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout, null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                "Sign Out",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE11D48)
            )
        }
    }
}

// ─────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onBackClick = {},
        fullName    = "Alex Johnson",
        email       = "alex.johnson@email.com",
        occupation  = "Software Engineer",
        age         = "28",
        targetArea  = "Velachery, Chennai",
        budget      = "₹15,000",
        moveInDate  = "1st Feb 2026",
        bio         = "Tech professional seeking a quiet, clean workspace-friendly flat near the metro."
    )
}