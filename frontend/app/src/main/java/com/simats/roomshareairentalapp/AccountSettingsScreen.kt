package com.simats.roomshareairentalapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    initialSettings: AccountSettingsData?,
    userEmail: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onUpdateSettings: (Boolean?, String?, String?) -> Unit,
    onChangeEmail: (String, String) -> Unit,
    onChangePassword: (String, String, String) -> Unit,
    onDeleteAccount: (String, String) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(initialSettings?.notificationsEnabled ?: true) }
    var email by remember { mutableStateOf(userEmail) }
    var selectedLanguage by remember { mutableStateOf(initialSettings?.language ?: "English (US)") }
    var privacySettings by remember { mutableStateOf(initialSettings?.privacySettings ?: "Public") }
    
    LaunchedEffect(initialSettings) {
        initialSettings?.let {
            notificationsEnabled = it.notificationsEnabled
            selectedLanguage = it.language ?: "English (US)"
            privacySettings = it.privacySettings ?: "Public"
        }
    }

    LaunchedEffect(userEmail) {
        email = userEmail
    }
    
    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Account Settings",
                onBackClick = onBackClick
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // Header equivalent to web
                SectionHeaderComp(
                    icon = Icons.Default.Settings,
                    iconBg = AppTheme.PrimaryAlpha8,
                    iconColor = AppTheme.Primary,
                    title = "System Configuration",
                    subtitle = "Privacy, security & global preferences"
                )

                Spacer(modifier = Modifier.height(32.dp))
                
                // General Settings Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppTheme.RadiusLg),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = BorderStroke(1.dp, AppTheme.Divider)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ModernSettingItem(
                            icon = Icons.Default.Notifications, 
                            label = "Pulse Notifications", 
                            desc = "Control how we alert you about matches and secure messages.",
                            onClick = { 
                                val newVal = !notificationsEnabled
                                notificationsEnabled = newVal
                                onUpdateSettings(newVal, null, null)
                            }
                        ) {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { 
                                    notificationsEnabled = it 
                                    onUpdateSettings(it, null, null)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AppTheme.Primary,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = AppTheme.Divider
                                ),
                                modifier = Modifier.scale(0.8f)
                            )
                        }
                        
                        DividerComp()

                        ModernSettingItem(
                            icon = Icons.Default.Email, 
                            label = "Verified Identity / Email", 
                            desc = email,
                            onClick = { showEmailDialog = true }
                        )

                        DividerComp()

                        ModernSettingItem(
                            icon = Icons.Default.Lock, 
                            label = "Access Credentials", 
                            desc = "Frequent password rotations enhance your security score.",
                            onClick = { showPasswordDialog = true }
                        )

                        DividerComp()

                        ModernSettingItem(
                            icon = Icons.Default.Shield, 
                            label = "Digital Footprint", 
                            desc = "Review data transparency and privacy governance protocols.",
                            onClick = { /* Navigate to privacy if needed */ }
                        )

                        DividerComp()

                        ModernSettingItem(
                            icon = Icons.Default.DeleteForever, 
                            label = "Termination Phase", 
                            desc = "Irreversibly purge all matching data and identity files.",
                            danger = true,
                            onClick = { showDeleteDialog = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Security-First Infrastructure Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppTheme.RadiusLg))
                        .background(AppTheme.HeaderGradient)
                        .padding(28.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                "Security-First",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                "AES-256 encryption active.",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Dialogs — Modern Styling
        if (showEmailDialog) {
            var tempEmail by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showEmailDialog = false },
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(52.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Email, null, tint = AppTheme.Primary, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Update Identity", 
                            fontWeight = FontWeight.Black, 
                            fontSize = 20.sp,
                            color = AppTheme.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "Enter your new email address. This will update your primary login credential.",
                            fontSize = 14.sp,
                            color = AppTheme.TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = tempEmail,
                            onValueChange = { tempEmail = it },
                            placeholder = { Text("new.email@roomshare.ai") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.Primary,
                                unfocusedBorderColor = AppTheme.Divider,
                                cursorColor = AppTheme.Primary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            if (tempEmail.isNotBlank()) {
                                onChangeEmail(email, tempEmail)
                                showEmailDialog = false 
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(AppTheme.RadiusMd),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary)
                    ) {
                        Text("CONFIRM CHANGE", fontWeight = FontWeight.Black, color = Color.White)
                    }
                },
                dismissButton = {
                   TextButton(onClick = { showEmailDialog = false }, modifier = Modifier.fillMaxWidth()) {
                       Text("ABORT", color = AppTheme.TextTertiary, fontWeight = FontWeight.Bold)
                   }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(AppTheme.RadiusLg)
            )
        }

        if (showPasswordDialog) {
            var oldPwd by remember { mutableStateOf("") }
            var newPwd by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showPasswordDialog = false },
                title = { 
                   Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(52.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Key, null, tint = AppTheme.Primary, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Rotate Credentials", 
                            fontWeight = FontWeight.Black, 
                            fontSize = 20.sp,
                            color = AppTheme.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "Strengthen account integrity by updating your security phrase.",
                            fontSize = 14.sp,
                            color = AppTheme.TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = oldPwd,
                            onValueChange = { oldPwd = it },
                            placeholder = { Text("Current Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            visualTransformation = PasswordVisualTransformation(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.Primary,
                                unfocusedBorderColor = AppTheme.Divider,
                                cursorColor = AppTheme.Primary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newPwd,
                            onValueChange = { newPwd = it },
                            placeholder = { Text("New Security Phrase") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            visualTransformation = PasswordVisualTransformation(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.Primary,
                                unfocusedBorderColor = AppTheme.Divider,
                                cursorColor = AppTheme.Primary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            if (oldPwd.isNotBlank() && newPwd.isNotBlank()) {
                                onChangePassword(email, oldPwd, newPwd)
                                showPasswordDialog = false 
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(AppTheme.RadiusMd),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary)
                    ) {
                        Text("UPDATE SECURITY", fontWeight = FontWeight.Black, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPasswordDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("CANCEL", color = AppTheme.TextTertiary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(AppTheme.RadiusLg)
            )
        }

        if (showDeleteDialog) {
            var password by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier.size(52.dp).background(AppTheme.ErrorAlpha, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Warning, null, tint = AppTheme.Error, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Account Deletion", 
                            fontWeight = FontWeight.Black, 
                            fontSize = 20.sp,
                            color = AppTheme.Error,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "This action irreversibly terminates your profile and all associated logs. This cannot be undone.",
                            fontSize = 14.sp,
                            color = AppTheme.Error.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Confirm Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.Error,
                                unfocusedBorderColor = AppTheme.ErrorAlpha,
                                cursorColor = AppTheme.Error
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            onDeleteAccount(email, password)
                            showDeleteDialog = false 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Error),
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(AppTheme.RadiusMd),
                        enabled = password.isNotEmpty()
                    ) {
                        Text("DELETE PERMANENTLY", fontWeight = FontWeight.Black, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("ABORT PROTOCOL", color = AppTheme.TextTertiary, fontWeight = FontWeight.Black)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(AppTheme.RadiusLg)
            )
        }
    }
}

@Composable
fun SectionHeaderComp(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(AppTheme.RadiusMd))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = AppTheme.TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = subtitle.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = AppTheme.TextTertiary,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun ModernSettingItem(
    icon: ImageVector,
    label: String,
    desc: String,
    danger: Boolean = false,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (danger) AppTheme.ErrorAlpha else AppTheme.PrimaryAlpha8),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, 
                    null, 
                    tint = if (danger) AppTheme.Error else AppTheme.Primary, 
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (danger) AppTheme.Error else AppTheme.TextPrimary,
                    fontSize = 14.sp,
                    letterSpacing = (-0.2).sp
                )
                Text(
                    text = desc,
                    color = AppTheme.TextTertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                )
            }
            if (content != null) {
                content()
            } else {
                Icon(
                    Icons.Default.ChevronRight, 
                    null, 
                    tint = AppTheme.Divider, 
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun DividerComp() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = AppTheme.Divider,
        thickness = 1.dp
    )
}


@Preview(showBackground = true)
@Composable
fun AccountSettingsScreenPreview() {
    RoomshareAIRentalAppTheme {
        AccountSettingsScreen(
            initialSettings = null, 
            userEmail = "john@example.com", 
            isLoading = false, 
            onBackClick = {}, 
            onUpdateSettings = { _, _, _ -> },
            onChangeEmail = { _, _ -> },
            onChangePassword = { _, _, _ -> },
            onDeleteAccount = { _, _ -> }
        )
    }
}
