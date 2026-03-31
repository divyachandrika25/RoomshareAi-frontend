package com.simats.roomshareairentalapp

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(
    roommate: Roommate,
    messages: List<ChatMessage>,
    onSendMessage: (String, Boolean) -> Unit,
    onSendImage: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSearchInConversation: () -> Unit = {},
    onMuteNotifications: () -> Unit = {},
    onBlockRoommate: () -> Unit = {},
    onReportChat: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()

    BackHandler { onBackClick() }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(AppTheme.RadiusMd)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = AppTheme.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                AsyncImage(
                                    model = roommate.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, AppTheme.Divider, RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(AppTheme.Success, CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    roommate.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AppTheme.TextPrimary,
                                    letterSpacing = (-0.3).sp
                                )
                                Text(
                                    if (isTyping) "typing..." else "Active now",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTyping) AppTheme.Primary else AppTheme.Success,
                                )
                            }
                        }
                        
                        IconButton(onClick = { /* Call Action */ }) {
                            Icon(Icons.Default.Phone, null, tint = AppTheme.TextSecondary, modifier = Modifier.size(20.dp))
                        }
                        
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, null, tint = AppTheme.TextSecondary)
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .border(1.dp, AppTheme.Divider, RoundedCornerShape(AppTheme.RadiusMd))
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Search Conversation", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                                    onClick = { showMenu = false; onSearchInConversation() },
                                    leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp), tint = AppTheme.Primary) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Block User", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppTheme.Error) },
                                    onClick = { showMenu = false; onBlockRoommate() },
                                    leadingIcon = { Icon(Icons.Default.Block, null, tint = AppTheme.Error, modifier = Modifier.size(18.dp)) }
                                )
                            }
                        }
                    }
                    
                    // Encryption notice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.Background)
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, null, tint = AppTheme.Primary.copy(alpha = 0.4f), modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "END-TO-END ENCRYPTED",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = AppTheme.TextTertiary,
                            letterSpacing = 1.sp
                        )
                    }
                    HorizontalDivider(color = AppTheme.Divider)
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                // AI Suggestions
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val smartReplies = listOf("How's 6 PM tomorrow?", "Is deposit negotiable?", "See more photos?")
                    items(smartReplies) { reply ->
                        Surface(
                            onClick = { onSendMessage(reply, true) },
                            shape = RoundedCornerShape(AppTheme.RadiusMd),
                            color = AppTheme.PrimaryAlpha8,
                            border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12)
                        ) {
                            Text(
                                reply,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.Primary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Surface(
                    color = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.Background, RoundedCornerShape(AppTheme.RadiusLg))
                            .border(1.dp, AppTheme.Divider, RoundedCornerShape(AppTheme.RadiusLg))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Attachment */ }) {
                            Icon(Icons.Default.AttachFile, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { /* Emoji */ }) {
                            Icon(Icons.Default.SentimentSatisfiedAlt, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(20.dp))
                        }
                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { 
                                Text(
                                    "Type a message...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AppTheme.TextTertiary
                                ) 
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = AppTheme.Primary,
                                focusedTextColor = AppTheme.TextPrimary,
                                unfocusedTextColor = AppTheme.TextPrimary
                            ),
                            maxLines = 4
                        )
                        
                        if (messageText.isBlank()) {
                            IconButton(onClick = { /* Mic */ }) {
                                Icon(Icons.Default.Mic, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
                            }
                        } else {
                            IconButton(
                                onClick = { 
                                    onSendMessage(messageText, true)
                                    messageText = "" 
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(AppTheme.Primary, RoundedCornerShape(12.dp))
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        },
        containerColor = AppTheme.Background
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                DirectChatMessageComp(message, roommate.imageUrl)
            }
            if (isTyping) {
                item { TypingIndicatorComp(roommate.imageUrl) }
            }
        }
    }
}

@Composable
fun DirectChatMessageComp(message: ChatMessage, roommateAvatar: String) {
    val isMe = message.isFromMe
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val bgColor = if (isMe) AppTheme.Primary else Color.White
    val textColor = if (isMe) Color.White else AppTheme.TextPrimary
    val shape = if (isMe) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            AsyncImage(
                model = roommateAvatar,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, AppTheme.Divider, RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(horizontalAlignment = alignment) {
            if (message.isRoomAction) {
                ChatRoomActionCard(message)
            } else {
                Surface(
                    color = bgColor,
                    shape = shape,
                    shadowElevation = if (isMe) 6.dp else 1.dp,
                    tonalElevation = 0.dp,
                    border = if (!isMe) BorderStroke(1.dp, AppTheme.Divider) else null
                ) {
                    Text(
                        text = message.text,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = message.time,
                    fontSize = 10.sp,
                    color = AppTheme.TextTertiary,
                    fontWeight = FontWeight.Bold
                )
                if (isMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.DoneAll,
                        null,
                        tint = AppTheme.Primary.copy(alpha = 0.6f),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatRoomActionCard(message: ChatMessage) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(AppTheme.RadiusLg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = AppTheme.PrimaryAlpha8,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "SHARED PROPERTY",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.Primary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        message.roomTitle ?: "Pacific Heights Loft",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.TextPrimary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            message.roomPrice ?: "₹45,000",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = AppTheme.Primary
                        )
                        Text(
                            "/mo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextTertiary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                Surface(
                    modifier = Modifier.size(40.dp),
                    color = AppTheme.PrimaryAlpha8,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Home, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* View Profile */ },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("VIEW DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }
    }
}

@Composable
fun TypingIndicatorComp(avatarUrl: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp),
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, AppTheme.Divider)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) {
                    Box(modifier = Modifier.size(5.dp).background(AppTheme.Primary.copy(alpha = 0.3f), CircleShape))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DirectChatScreenPreview() {
    RoomshareAIRentalAppTheme {
        val sampleRoommate = Roommate("1", "Sarah Jenkins", "sarah@jenkins.com", 24, "SF", emptyList(), "₹45,000", "https://images.unsplash.com/photo-1544005313-94ddf0286df2")
        DirectChatScreen(
            roommate = sampleRoommate,
            messages = listOf(
                ChatMessage(senderName = "Sarah Jenkins", text = "Hello!", isFromMe = false, time = "10:00 AM"),
                ChatMessage(senderName = "You", text = "Hi there!", isFromMe = true, time = "10:05 AM")
            ),
            onSendMessage = { _, _ -> }
        )
    }
}
