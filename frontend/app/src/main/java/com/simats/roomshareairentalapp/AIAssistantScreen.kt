package com.simats.roomshareairentalapp

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch

// ── Design tokens ─────────────────────────────────────────────────────────────
private val Blue        = Color(0xFF1E63FF)
private val BlueDark    = Color(0xFF0D1E3C)
private val BlueDeep    = Color(0xFF080F1E)
private val BlueSurface = Color(0xFFF0F5FF)
private val Emerald     = Color(0xFF10B981)
private val Rose        = Color(0xFFF43F5E)
private val TextPri     = Color(0xFF0F172A)
private val TextSec     = Color(0xFF475569)
private val TextMuted   = Color(0xFF94A3B8)
private val Border      = Color(0xFFE2E8F0)
private val BgPage      = Color(0xFFF8FAFC)
private val CardWhite   = Color(0xFFFFFFFF)

private val NavyGradient = Brush.linearGradient(listOf(Blue, BlueDark, BlueDeep))

// ── Helpers ───────────────────────────────────────────────────────────────────
private fun initials(name: String?): String {
    val n = name?.trim() ?: return "U"
    return n.split(" ").filter { it.isNotEmpty() }.take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
}

// ── Letter avatar ─────────────────────────────────────────────────────────────
@Composable
private fun LetterAvatar(name: String?, size: Int = 32) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.3).dp))
            .background(NavyGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials(name),
            color = Color.White,
            fontSize = (size * 0.35).sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp
        )
    }
}

// ── AI avatar icon ────────────────────────────────────────────────────────────
@Composable
private fun AIAvatar(pulse: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by if (pulse) infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "alpha"
    ) else remember { mutableFloatStateOf(1f) }.let { derivedStateOf { it.floatValue } }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Blue.copy(alpha = 0.1f))
            .border(1.dp, Blue.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Blue.copy(alpha = if (pulse) alpha else 1f),
            modifier = Modifier.size(16.dp)
        )
    }
}

// ── Thinking / typing dots ────────────────────────────────────────────────────
@Composable
fun ThinkingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dotAlphas = (0..2).map { i ->
        infiniteTransition.animateFloat(
            initialValue = 0.25f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(600, delayMillis = i * 150),
                RepeatMode.Reverse
            ),
            label = "dot$i"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AIAvatar(pulse = true)

        Surface(
            color = CardWhite,
            shape = RoundedCornerShape(16.dp).copy(topStart = CornerSize(4.dp)),
            border = BorderStroke(1.dp, Border)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                dotAlphas.forEach { anim ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Blue.copy(alpha = anim.value))
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    "Thinking…",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ── Quick prompt chip ─────────────────────────────────────────────────────────
@Composable
private fun QuickChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite)
            .border(1.dp, Border, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSec
        )
    }
}

// ── Result card ───────────────────────────────────────────────────────────────
@Composable
fun ResultCardComp(
    result: AILocationAgentResult,
    onResultClick: (AILocationAgentResult) -> Unit,
    onCallClick: (String) -> Unit,
    onNavigateClick: (String) -> Unit
) {
    Surface(
        color = CardWhite,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Border),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&auto=format&fit=crop",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                                startY = 60f
                            )
                        )
                )
                // Stars badge
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(7.dp))
                        .background(CardWhite.copy(alpha = 0.94f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "${result.stars ?: 4}★",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Blue
                    )
                }
                // Verified badge
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Emerald.copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Verified",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // Details
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            result.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPri,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.LocationOn, null, tint = Blue.copy(alpha = 0.5f), modifier = Modifier.size(11.dp))
                            Spacer(Modifier.width(3.dp))
                            Text(
                                result.address,
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = Border, thickness = 0.5.dp)
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            if (result.category == "room") "PER MONTH" else "PER NIGHT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextMuted,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            "₹${result.price}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Blue,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Phone
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .border(1.dp, Border, RoundedCornerShape(9.dp))
                                .clickable { onCallClick(result.phone ?: "") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Phone, null, tint = TextSec, modifier = Modifier.size(15.dp))
                        }
                        // Navigate
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .border(1.dp, Border, RoundedCornerShape(9.dp))
                                .clickable { onNavigateClick(result.address) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Navigation, null, tint = TextSec, modifier = Modifier.size(15.dp))
                        }
                        // Book CTA
                        val isAvailable = result.status == null || result.status == "AVAILABLE"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .background(if (isAvailable) Blue else Color.Gray)
                                .clickable(enabled = isAvailable) { onResultClick(result) }
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                        ) {
                            Text(
                                if (isAvailable) "Details" else "Booked",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Roommate strip ────────────────────────────────────────────────────────────
@Composable
fun RoommateStripComp(
    roommates: List<DiscoverRoommate>,
    onRoommateClick: (DiscoverRoommate) -> Unit
) {
    Surface(
        color = BgPage,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Blue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Group, null, tint = Blue, modifier = Modifier.size(13.dp))
                }
                Spacer(Modifier.width(7.dp))
                Text(
                    "ROOMMATES NEARBY",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPri,
                    letterSpacing = 0.9.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(roommates) { rm ->
                    val photoUrl = rm.photo?.takeIf { it.isNotBlank() }
                    Surface(
                        onClick = { onRoommateClick(rm) },
                        color = CardWhite,
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (photoUrl != null) {
                                AsyncImage(
                                    model = photoUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Border, RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                LetterAvatar(rm.fullName, size = 30)
                            }
                            Column {
                                Text(
                                    rm.fullName ?: "User",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPri,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    rm.roomStatus?.replace("_", " ")?.lowercase()
                                        ?.replaceFirstChar { it.uppercaseChar() } ?: "Seeking",
                                    fontSize = 9.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Message bubble ────────────────────────────────────────────────────────────
@Composable
fun PremiumAIMessage(
    message: AIChatMessage,
    onRoommateClick: (DiscoverRoommate) -> Unit,
    onResultClick: (AILocationAgentResult) -> Unit,
    onCallClick: (String) -> Unit,
    onNavigateClick: (String) -> Unit
) {
    if (!message.isFromAI) {
        // User bubble — right aligned
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                color = Blue,
                shape = RoundedCornerShape(18.dp).copy(bottomEnd = CornerSize(4.dp)),
                shadowElevation = 2.dp,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp)
                )
            }
        }
    } else {
        // AI bubble — left aligned
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.Top
        ) {
            AIAvatar()

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Text bubble
                Surface(
                    color = CardWhite,
                    shape = RoundedCornerShape(18.dp).copy(topStart = CornerSize(4.dp)),
                    border = BorderStroke(1.dp, Border),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = message.text,
                        color = TextPri,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 21.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp)
                    )
                }

                // Result cards
                message.results?.let { results ->
                    for (result in results) {
                        key(result.id.toString()) {
                            ResultCardComp(result, onResultClick, onCallClick, onNavigateClick)
                        }
                    }
                }

                // Roommate strip
                if (!message.nearbyRoommates.isNullOrEmpty()) {
                    RoommateStripComp(message.nearbyRoommates, onRoommateClick)
                }
            }
        }
    }
}

// ── Main screen ───────────────────────────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(
    email: String = "",
    onBackClick: () -> Unit = {},
    onRoommateClick: (DiscoverRoommate) -> Unit = {},
    onResultClick: (AILocationAgentResult) -> Unit = {},
    onNavigateClick: (String) -> Unit = {},
    checkSearchLimit: () -> Boolean = { true }
) {
    var messageText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isTyping by remember { mutableStateOf(false) }

    val chatMessages = remember {
        mutableStateListOf<AIChatMessage>(
            AIChatMessage(
                text = "👋 Hi! I'm your RoomShare AI Assistant. Ask me to find hotels, rooms, or roommates near any location — and I'll search in real time.",
                isFromAI = true
            )
        )
    }

    val quickPrompts = listOf(
        QuickPrompt("🏨 Hotels in Chennai under ₹2000", "Hotels in Chennai under ₹2000"),
        QuickPrompt("🏠 Rooms near Anna Nagar", "Rooms near Anna Nagar"),
        QuickPrompt("👥 Roommates in Chennai", "Find roommates in Chennai"),
        QuickPrompt("📍 PG near SRM University", "PG near SRM University")
    )

    val sendMessage = {
        val query = messageText.trim()
        if (query.isNotBlank() && !isTyping) {
            if (checkSearchLimit()) {
                chatMessages.add(AIChatMessage(query, false))
                messageText = ""
                isTyping = true
                scope.launch {
                    try {
                        val res = RetrofitClient.instance.aiLocationAgent(AILocationAgentRequest(query))
                        if (res.isSuccessful) {
                            val data = res.body()
                            chatMessages.add(
                                AIChatMessage(
                                    text = data?.text ?: "No results found.",
                                    isFromAI = true,
                                    results = data?.results,
                                    nearbyRoommates = data?.roommates
                                )
                            )
                        }
                    } catch (_: Exception) {
                        chatMessages.add(
                            AIChatMessage("Sorry, something went wrong. Please try again.", true)
                        )
                    } finally {
                        isTyping = false
                    }
                }
            }
        }
        Unit
    }

    Scaffold(
        containerColor = BgPage,
        topBar = {
            // ── Top bar ───────────────────────────────────────────────────────
            Surface(
                color = CardWhite,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, Border) // bottom border only via modifier below
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Back
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextSec,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // AI icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Title + status
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "AI Concierge",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPri,
                            letterSpacing = (-0.2).sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Emerald)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "ONLINE · v2.0",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextMuted,
                                letterSpacing = 0.6.sp
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ── Messages ──────────────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(chatMessages) { message ->
                    PremiumAIMessage(
                        message = message,
                        onRoommateClick = onRoommateClick,
                        onResultClick = onResultClick,
                        onCallClick = { ph ->
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$ph")))
                        },
                        onNavigateClick = onNavigateClick
                    )
                }
                if (isTyping) {
                    item { ThinkingIndicator() }
                }
            }

            // ── Input area ────────────────────────────────────────────────────
            Surface(
                color = CardWhite,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, Border) // top border effect
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Quick prompts — only on first message
                    if (chatMessages.count() <= 1 && !isTyping) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            quickPrompts.forEach { prompt ->
                                QuickChip(label = prompt.label) { messageText = prompt.query }
                            }
                        }
                    }

                    // Input row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(BgPage)
                            .border(1.dp, Border, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BasicTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPri
                            ),
                            decorationBox = { inner ->
                                Box {
                                    if (messageText.isEmpty()) {
                                        Text(
                                            "Ask about hotels, rooms, roommates…",
                                            fontSize = 13.sp,
                                            color = TextMuted,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                    inner()
                                }
                            }
                        )

                        // Send button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (messageText.isNotBlank() && !isTyping) Blue
                                    else Border
                                )
                                .clickable(enabled = messageText.isNotBlank() && !isTyping) { sendMessage() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (messageText.isNotBlank() && !isTyping) Color.White else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Disclaimer
                    Text(
                        "AI responses are for guidance only",
                        fontSize = 9.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}


data class AIChatMessage(
    val text: String,
    val isFromAI: Boolean,
    val results: List<AILocationAgentResult>? = null,
    val nearbyRoommates: List<DiscoverRoommate>? = null
)

data class QuickPrompt(val label: String, val query: String)

@Preview(showBackground = true)
@Composable
fun AIAssistantScreenPreview() {
    RoomshareAIRentalAppTheme {
        AIAssistantScreen()
    }
}