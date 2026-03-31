package com.simats.roomshareairentalapp

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class ChatSummary(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val imageUrl: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isGroup: Boolean = false
)

// ─────────────────────────────────────────
//  MAIN SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(
    onMenuClick: () -> Unit = {},
    chatSummaries: List<ChatSummary>,
    onChatClick: (ChatSummary) -> Unit,
    onNewChatClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onSearch: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(searchQuery, chatSummaries) {
        if (searchQuery.isBlank()) chatSummaries
        else chatSummaries.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    val unreadTotal = remember(chatSummaries) { chatSummaries.sumOf { it.unreadCount } }

    BackHandler { onBackClick() }

    Scaffold(
        containerColor = AppTheme.Background,
        topBar = {
            MessagesTopBar(
                unreadTotal = unreadTotal,
                onMenuClick = onMenuClick,
                onBackClick = onBackClick,
                onNewChatClick = onNewChatClick,
                onRefresh = onRefresh
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Hero Header ──
            MessagesHero(
                searchQuery = searchQuery,
                onSearchChange = {
                    searchQuery = it
                    onSearch(it)
                },
                totalChats = chatSummaries.size,
                unreadTotal = unreadTotal
            )

            // ── Body ──
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                shadowElevation = 0.dp
            ) {
                if (isLoading) {
                    LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {
                        items(5) { MessageSkeletonItem() }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        // AI Banner is now an item so it doesn't push the list
                        item {
                            AICtaBanner(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                                onClick = onAIClick
                            )
                        }

                        if (filtered.isEmpty()) {
                            item {
                                MessagesEmptyState(
                                    isSearch = searchQuery.isNotBlank(),
                                    onClear = { searchQuery = "" },
                                    onRefresh = onRefresh
                                )
                            }
                        } else {
                            // Section: Unread
                            val unread = filtered.filter { it.unreadCount > 0 }
                            val read   = filtered.filter { it.unreadCount == 0 }

                            if (unread.isNotEmpty()) {
                                item { MessagesSectionLabel("Unread Conversations") }
                                items(unread) { chat ->
                                    MessageItem(chat = chat, onClick = { onChatClick(chat) })
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 88.dp, end = 20.dp),
                                        color = AppTheme.Divider
                                    )
                                }
                            }

                            if (read.isNotEmpty()) {
                                item {
                                    MessagesSectionLabel(
                                        if (unread.isNotEmpty()) "Earlier" else "All Messages"
                                    )
                                }
                                items(read) { chat ->
                                    MessageItem(chat = chat, onClick = { onChatClick(chat) })
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 88.dp, end = 20.dp),
                                        color = AppTheme.Divider
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun MessagesTopBar(
    unreadTotal: Int,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onRefresh: () -> Unit
) {
    Surface(color = Color.White, shadowElevation = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back
            IconButton(onClick = onBackClick) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(AppTheme.RadiusMd)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ArrowBack, "Back",
                        tint = AppTheme.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Title + unread badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Messages",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppTheme.TextPrimary
                )
                if (unreadTotal > 0) {
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        color = AppTheme.Primary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            unreadTotal.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Menu + New Chat
            Row {
                IconButton(onClick = onMenuClick) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(AppTheme.Divider, RoundedCornerShape(AppTheme.RadiusMd)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Menu, "Menu", tint = AppTheme.TextPrimary, modifier = Modifier.size(18.dp))
                    }
                }
                IconButton(onClick = onNewChatClick) {
                    Icon(Icons.Default.Add, null, tint = AppTheme.TextSecondary)
                }
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, null, tint = AppTheme.TextSecondary)
                }
            }
        }
        HorizontalDivider(color = AppTheme.Divider)
    }
}

// ─────────────────────────────────────────
//  HERO HEADER
// ─────────────────────────────────────────
@Composable
private fun MessagesHero(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    totalChats: Int,
    unreadTotal: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.HeaderGradient)
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-30).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 28.dp)
        ) {
            Text(
                "Inbox & Chats",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )

            // Stats row
            Row(
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroStatPill(label = "$totalChats Conversations")
                if (unreadTotal > 0) {
                    HeroStatPill(label = "$unreadTotal Unread", highlight = true)
                }
            }

            // Search bar
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.RadiusLg),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search, null,
                        tint = AppTheme.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        ),
                        decorationBox = { inner ->
                            if (searchQuery.isEmpty()) {
                                Text("Search messages…", fontSize = 14.sp, color = AppTheme.TextTertiary, fontWeight = FontWeight.Medium)
                            }
                            inner()
                        }
                    )
                    if (searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = { onSearchChange("") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStatPill(label: String, highlight: Boolean = false) {
    Surface(
        color = if (highlight) Color.White.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.14f),
        shape = RoundedCornerShape(20.dp),
        border = if (highlight) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}

// ─────────────────────────────────────────
//  SECTION LABEL
// ─────────────────────────────────────────
@Composable
private fun MessagesSectionLabel(label: String) {
    Text(
        text = label.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        color = AppTheme.TextTertiary,
        letterSpacing = 0.8.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

// ─────────────────────────────────────────
//  MESSAGE ITEM
// ─────────────────────────────────────────
@Composable
fun MessageItem(chat: ChatSummary, onClick: () -> Unit) {
    val hasUnread = chat.unreadCount > 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (hasUnread) AppTheme.PrimaryAlpha8.copy(alpha = 0.05f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        // Unread accent bar
        if (hasUnread) {
            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height(36.dp)
                    .align(Alignment.CenterStart)
                    .background(AppTheme.Primary, RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Avatar ──
            Box(modifier = Modifier.size(54.dp)) {
                AsyncImage(
                    model = chat.imageUrl.takeIf { it.isNotBlank() }
                        ?: "https://ui-avatars.com/api/?name=${chat.name}&background=2563EB&color=fff&size=200",
                    contentDescription = chat.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(2.dp, RoundedCornerShape(AppTheme.RadiusMd))
                        .clip(RoundedCornerShape(AppTheme.RadiusMd))
                        .border(
                            width = if (hasUnread) 2.dp else 1.dp,
                            color = if (hasUnread) AppTheme.Primary else AppTheme.Divider,
                            shape = RoundedCornerShape(AppTheme.RadiusMd)
                        ),
                    contentScale = ContentScale.Crop
                )

                // Online dot
                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .background(AppTheme.Success, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }

                // Group badge
                if (chat.isGroup) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .background(AppTheme.Primary, RoundedCornerShape(6.dp))
                            .border(1.5.dp, Color.White, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Group, null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.width(14.dp))

            // ── Text Content ──
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.name,
                        fontSize = 15.sp,
                        fontWeight = if (hasUnread) FontWeight.ExtraBold else FontWeight.Bold,
                        color = AppTheme.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(Modifier.width(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Default.Schedule, null,
                            tint = AppTheme.TextTertiary,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            chat.time,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasUnread) AppTheme.Primary else AppTheme.TextTertiary
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.lastMessage,
                        fontSize = 13.sp,
                        color = if (hasUnread) AppTheme.TextSecondary else AppTheme.TextTertiary,
                        fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (hasUnread) {
                        Spacer(Modifier.width(10.dp))
                        Surface(
                            color = AppTheme.Primary,
                            shape = RoundedCornerShape(7.dp)
                        ) {
                            Text(
                                chat.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  AI CTA BANNER
// ─────────────────────────────────────────
@Composable
private fun AICtaBanner(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(AppTheme.RadiusLg),
                ambientColor = AppTheme.PrimaryAlpha15,
                spotColor = AppTheme.PrimaryAlpha8
            )
            .clip(RoundedCornerShape(AppTheme.RadiusLg))
            .clickable { onClick() },
        shape = RoundedCornerShape(AppTheme.RadiusLg),
        color = Color.White
    ) {
        Box {
            // Gradient accent strip
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(AppTheme.PremiumGradient)
                    .align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AppTheme.PremiumGradient, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AutoAwesome, null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "AI Roommate Finder",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = AppTheme.Violet,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "BETA",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    Text(
                        "Chat with AI to find your perfect match faster",
                        fontSize = 12.sp,
                        color = AppTheme.TextSecondary,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward, null,
                        tint = AppTheme.Primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  SKELETON LOADER
// ─────────────────────────────────────────
@Composable
private fun MessageSkeletonItem() {
    val alpha by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0.4f,
        targetValue  = 0.85f,
        animationSpec = infiniteRepeatable(
            animation   = tween(900, easing = LinearEasing),
            repeatMode  = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(AppTheme.RadiusMd))
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(14.dp)
                    .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(11.dp)
                    .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp))
            )
        }
    }
}

// ─────────────────────────────────────────
//  EMPTY STATE
// ─────────────────────────────────────────
@Composable
private fun MessagesEmptyState(isSearch: Boolean, onClear: () -> Unit, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isSearch) Icons.Default.SearchOff else Icons.Outlined.ChatBubbleOutline,
                null,
                tint = AppTheme.PrimaryLight,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            if (isSearch) "No chats found" else "No messages yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppTheme.TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isSearch) "Try a different name or keyword"
            else "Your conversations will appear here once you start matching with roommates.",
            fontSize = 14.sp,
            color = AppTheme.TextSecondary,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { if (isSearch) onClear() else onRefresh() },
            shape = RoundedCornerShape(AppTheme.RadiusMd),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(if (isSearch) "Clear Search" else "Refresh Inbox", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// ─────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessagesListScreenPreview() {
    MessagesListScreen(
        chatSummaries = listOf(
            ChatSummary("1", "Priya Sharma", "Let's discuss the move-in date.", "10:30 AM", "", 3, isOnline = true),
            ChatSummary("2", "Downtown Group", "Who's bringing the kitchen stuff?", "Yesterday", "", 1, isGroup = true),
            ChatSummary("3", "Rajan Kumar", "The rent is ₹12,000 per month.", "Mon", "", 0, isOnline = false),
            ChatSummary("4", "Velachery Flatmates", "Anyone free this weekend?", "Sun", "", 0, isGroup = true)
        ),
        onChatClick = {}
    )
}