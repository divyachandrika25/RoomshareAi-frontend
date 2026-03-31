package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ─────────────────────────────────────────
//  AI CHATBOT SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatbotScreen(
    email: String,
    onBackClick: () -> Unit,
    messages: List<AIChatbotMessage>,
    suggestions: List<String>?,
    isLoadingHistory: Boolean,
    isSending: Boolean,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = AppTheme.Background,
        topBar = { AIChatTopBar(onBackClick = onBackClick, onClearChat = onClearChat, hasMessages = messages.isNotEmpty()) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Subtle background gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(AppTheme.PrimaryAlpha8, Color.Transparent)
                        )
                    )
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // ── Body ──
                when {
                    isLoadingHistory -> {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = AppTheme.Primary,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(Modifier.height(12.dp))
                                Text("Syncing Knowledge Base…", fontSize = 13.sp, color = AppTheme.TextTertiary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    messages.isEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item { AIChatWelcome(suggestions = suggestions, onSuggestionClick = onSendMessage) }
                        }
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            contentPadding = PaddingValues(vertical = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(messages) { msg ->
                                AIChatMessageBubble(msg)
                            }
                            if (isSending) {
                                item { TypingIndicator() }
                            }
                        }
                    }
                }

                // ── Input Bar ──
                AIChatInputBar(
                    inputText = inputText,
                    isSending = isSending,
                    onInputChange = { inputText = it },
                    onSend = {
                        if (inputText.isNotBlank() && !isSending) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun AIChatTopBar(
    onBackClick: () -> Unit,
    onClearChat: () -> Unit,
    hasMessages: Boolean
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
                        Icons.AutoMirrored.Filled.ArrowBack, "Back",
                        tint = AppTheme.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.width(4.dp))

            // AI identity pill
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(AppTheme.PremiumGradient, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "RoomShare AI",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(AppTheme.Success, CircleShape)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Online · Always available",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.Success
                        )
                    }
                }
            }

            // Clear button
            if (hasMessages) {
                IconButton(onClick = onClearChat) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(AppTheme.ErrorAlpha8, RoundedCornerShape(AppTheme.RadiusMd)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Delete, "Clear chat",
                            tint = AppTheme.Error,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            } else {
                Spacer(Modifier.size(48.dp))
            }
        }
        HorizontalDivider(color = AppTheme.Divider)
    }
}

// ─────────────────────────────────────────
//  WELCOME / EMPTY STATE
// ─────────────────────────────────────────
@Composable
private fun AIChatWelcome(
    suggestions: List<String>?,
    onSuggestionClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Hero banner
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
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AutoAwesome, null,
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "RoomShare AI Assistant",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-0.3).sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Your intelligent guide for finding roommates,\nhousing tips, budgeting & lease advice.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )
                Spacer(Modifier.height(20.dp))
                // Capability pills
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("🏠 Rooms", "💰 Budget", "📋 Lease").forEach { tag ->
                        Surface(
                            color = Color.White.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        // Suggestions
        if (!suggestions.isNullOrEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "SUGGESTED QUESTIONS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.TextTertiary,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 4.dp)
                )
                suggestions.forEach { suggestion ->
                    SuggestionChip(text = suggestion, onClick = { onSuggestionClick(suggestion) })
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(AppTheme.RadiusMd))
            .clip(RoundedCornerShape(AppTheme.RadiusMd))
            .clickable { onClick() },
        shape = RoundedCornerShape(AppTheme.RadiusMd),
        color = Color.White,
        border = BorderStroke(1.dp, AppTheme.Divider)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Info, null,
                    tint = AppTheme.Primary,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.weight(1f),
                lineHeight = 19.sp
            )
            Icon(
                Icons.Default.ChevronRight, null,
                tint = AppTheme.TextTertiary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ─────────────────────────────────────────
//  MESSAGE BUBBLE
// ─────────────────────────────────────────
@Composable
fun AIChatMessageBubble(message: AIChatbotMessage) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // AI avatar
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        AppTheme.PremiumGradient,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
        }

        // Bubble
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd   = 20.dp,
                bottomStart = if (isUser) 20.dp else 4.dp,
                bottomEnd   = if (isUser) 4.dp  else 20.dp
            ),
            color = if (isUser) Color.Transparent else Color.White,
            border = if (!isUser) BorderStroke(1.dp, AppTheme.Divider) else null,
            shadowElevation = if (isUser) 0.dp else 4.dp,
            modifier = if (isUser) Modifier.background(
                AppTheme.PremiumGradient,
                shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
            ) else Modifier
        ) {
            MarkdownText(
                text = message.content,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp),
                color = if (isUser) Color.White else AppTheme.TextPrimary,
                fontSize = 14.sp
            )
        }

        // User trailing space
        if (isUser) Spacer(Modifier.width(4.dp))
    }
}

// ─────────────────────────────────────────
//  TYPING INDICATOR
// ─────────────────────────────────────────
@Composable
fun TypingIndicator() {
    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    AppTheme.PremiumGradient,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(10.dp))
        Surface(
            shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
            color = Color.White,
            border = BorderStroke(1.dp, AppTheme.Divider),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TypingDot(delayMs = 0)
                TypingDot(delayMs = 200)
                TypingDot(delayMs = 400)
            }
        }
    }
}

@Composable
private fun TypingDot(delayMs: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot_$delayMs")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, delayMillis = delayMs, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    Box(
        modifier = Modifier
            .size(7.dp)
            .offset(y = offsetY.dp)
            .background(AppTheme.Primary, CircleShape)
    )
}

// ─────────────────────────────────────────
//  INPUT BAR
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AIChatInputBar(
    inputText: String,
    isSending: Boolean,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // New Input Deck
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = AppTheme.Divider.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, AppTheme.Divider)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp, max = 150.dp),
                    placeholder = {
                        Text(
                            "Ask AI anything...",
                            fontSize = 15.sp,
                            color = AppTheme.TextTertiary,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor   = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor   = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor             = AppTheme.Primary
                    ),
                    keyboardOptions  = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions  = KeyboardActions(onSend = { onSend() }),
                    maxLines         = 5
                )
            }

            Spacer(Modifier.width(12.dp))

            // Premium Send Button
            val canSend = inputText.isNotBlank() && !isSending
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        if (canSend) AppTheme.PremiumGradient else Brush.linearGradient(listOf(AppTheme.Divider, AppTheme.Divider)),
                        CircleShape
                    )
                    .clip(CircleShape)
                    .clickable(enabled = canSend) { onSend() },
                contentAlignment = Alignment.Center
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.Send, "Send",
                        tint = if (canSend) Color.White else Color(0xFF94A3B8),
                        modifier = Modifier
                            .size(22.dp)
                            .offset(x = 1.dp)
                    )
                }
            }
        }
    }
}