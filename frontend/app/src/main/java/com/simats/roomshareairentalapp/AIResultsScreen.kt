package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ─────────────────────────────────────────
//  AI RESULTS SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIResultsScreen(
    aiMatches: List<MatchResult> = emptyList(),
    homeMatches: List<HomeRoomItem> = emptyList(),
    hotelResults: List<AILocationAgentResult> = emptyList(),
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onViewMatchesClick: () -> Unit = {},
    onProfileClick: (MatchResult) -> Unit = {},
    onHomeClick: (HomeRoomItem) -> Unit = {}
) {
    Scaffold(
        containerColor = AppTheme.Slate50,
        topBar = { AIResultsTopBar(onBackClick = onBackClick) }
    ) { padding ->

        if (isLoading) {
            AIResultsLoadingState(modifier = Modifier.padding(padding))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero ──
            AIResultsHero()

            // ── AI Strategy Card ──
            AIStrategyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp)
            )

            // ── Stats Row ──
            AIStatsRow(
                matchCount = aiMatches.size,
                homeCount  = homeMatches.size,
                hotelCount = hotelResults.size,
                modifier   = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-8).dp)
            )

            Spacer(Modifier.height(8.dp))

            // ── Hotel Matches ──
            AISection(
                icon     = Icons.Default.Star,
                iconBg   = AppTheme.Blue600,
                title    = "AI Smart Stays",
                subtitle = "Curated accommodations for you"
            )
            if (hotelResults.isNotEmpty()) {
                LazyRow(
                    modifier            = Modifier.fillMaxWidth(),
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(hotelResults) { hotel -> HotelMatchCard(hotel = hotel) }
                }
            } else {
                AIEmptyPlaceholder(
                    text = "Searching worldwide stays…",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Profile Matches ──
            AISection(
                icon     = Icons.Default.People,
                iconBg   = AppTheme.Indigo500,
                title    = "Top Profile Matches",
                subtitle = "People who share your lifestyle"
            )
            if (aiMatches.isNotEmpty()) {
                LazyRow(
                    modifier            = Modifier.fillMaxWidth(),
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(aiMatches) { match ->
                        ProfileMatchCard(match = match, onClick = { onProfileClick(match) })
                    }
                }
            } else {
                AIEmptyPlaceholder(
                    text = "Analyzing compatibility profiles…",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Home Matches ──
            AISection(
                icon     = Icons.Default.Home,
                iconBg   = AppTheme.Blue500,
                title    = "Premium Home Matches",
                subtitle = "Verified listings near you"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (homeMatches.isNotEmpty()) {
                    homeMatches.take(3).forEach { room ->
                        HomeRoomListItemComponent(
                            room         = room,
                            onClick      = { onHomeClick(room) }
                        )
                    }
                } else {
                    AIEmptyPlaceholder(
                        text = "No verified homes found in this area yet.",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── CTA Button ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onViewMatchesClick() },
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(AppTheme.Blue800, AppTheme.Blue600, AppTheme.Indigo500))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Dashboard, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Go to Dashboard", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Spacer(Modifier.width(10.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun AIResultsTopBar(onBackClick: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 0.dp) {
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
                        .background(AppTheme.Blue50, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = AppTheme.Blue700, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Text("AI Smart Matches", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
            Spacer(Modifier.weight(1f))
            // Placeholder to balance layout
            Spacer(Modifier.size(48.dp))
        }
        HorizontalDivider(color = AppTheme.Slate100)
    }
}

// ─────────────────────────────────────────
//  HERO SECTION
// ─────────────────────────────────────────
@Composable
private fun AIResultsHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(listOf(AppTheme.Blue800, AppTheme.Blue600, AppTheme.Indigo500))
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-55).dp, y = (-55).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-40).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                    .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "AI Engine Success!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "We've analyzed 500+ data points to find\nyour perfect matches.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.78f),
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
        }
    }
}

// ─────────────────────────────────────────
//  AI STRATEGY CARD
// ─────────────────────────────────────────
@Composable
private fun AIStrategyCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(22.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.12f),
                spotColor   = AppTheme.Indigo500.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(AppTheme.Blue700, AppTheme.Indigo600))
                )
        ) {
            // Subtle pattern circle
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(15.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "AI STRATEGY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = Color.White.copy(alpha = 0.12f)
                )
                Text(
                    "Based on your 'Balanced' sleep schedule, we prioritized roommates with similar habits to ensure a harmonious living experience.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  STATS ROW
// ─────────────────────────────────────────
@Composable
private fun AIStatsRow(
    matchCount: Int,
    homeCount: Int,
    hotelCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AIStatTile(value = "$matchCount", label = "People Matched", icon = Icons.Default.People,   iconBg = AppTheme.Indigo500, modifier = Modifier.weight(1f))
        AIStatTile(value = "$homeCount",  label = "Homes Found",    icon = Icons.Default.Home,     iconBg = AppTheme.Blue500,   modifier = Modifier.weight(1f))
        AIStatTile(value = "$hotelCount", label = "Hotels Found",   icon = Icons.Default.Business, iconBg = AppTheme.Blue600,   modifier = Modifier.weight(1f))
    }
}

@Composable
private fun AIStatTile(
    value: String,
    label: String,
    icon: ImageVector,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(iconBg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(15.dp))
            }
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = AppTheme.Slate900)
            Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.Slate400, textAlign = TextAlign.Center, lineHeight = 12.sp)
        }
    }
}

// ─────────────────────────────────────────
//  SECTION HEADER
// ─────────────────────────────────────────
@Composable
private fun AISection(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(iconBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
            Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AppTheme.Slate400)
        }
    }
}

// ─────────────────────────────────────────
//  HOTEL MATCH CARD
// ─────────────────────────────────────────
@Composable
fun HotelMatchCard(hotel: AILocationAgentResult) {
    Surface(
        modifier = Modifier
            .width(210.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.08f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column {
            // Image / Placeholder header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.linearGradient(listOf(AppTheme.Blue700, AppTheme.Blue500))
                    )
            ) {
                // Decorative circle
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-20).dp)
                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                )
                Icon(
                    Icons.Default.Business, null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(44.dp).align(Alignment.Center)
                )
                // Stars badge
                if (hotel.stars != null && hotel.stars > 0) {
                    Surface(
                        color = Color.White.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.TopStart).padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, null, tint = AppTheme.Gold, modifier = Modifier.size(11.dp))
                            Spacer(Modifier.width(3.dp))
                            Text(hotel.stars.toInt().toString(), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
                // AI Choice badge
                Surface(
                    color = AppTheme.Success.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
                ) {
                    Text(
                        "AI PICK",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    hotel.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = AppTheme.Slate900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Surface(
                    color = AppTheme.Blue50,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        hotel.type ?: "Hotel",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.Blue600,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        letterSpacing = 0.4.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(AppTheme.Blue50, RoundedCornerShape(5.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = AppTheme.Blue600, modifier = Modifier.size(11.dp))
                    }
                    Spacer(Modifier.width(5.dp))
                    Text(
                        hotel.address,
                        fontSize = 11.sp,
                        color = AppTheme.Slate600,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (hotel.dist_km != null) {
                    Spacer(Modifier.height(3.dp))
                    Text("${hotel.dist_km} km from center", fontSize = 10.sp, color = AppTheme.Slate400)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = AppTheme.Slate100)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("from", fontSize = 9.sp, color = AppTheme.Slate400)
                        Text("₹${hotel.price}", fontWeight = FontWeight.Black, fontSize = 17.sp, color = AppTheme.Slate900)
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(AppTheme.Blue600, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(15.dp))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  PROFILE MATCH CARD
// ─────────────────────────────────────────
@Composable
fun ProfileMatchCard(match: MatchResult, onClick: () -> Unit) {
    val score = match.compatibilityScore.toInt()
    val scoreColor = when {
        score >= 85 -> AppTheme.Success
        score >= 70 -> AppTheme.Blue600
        else        -> Color(0xFFF59E0B)
    }

    Surface(
        modifier = Modifier
            .width(160.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.08f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar + score ring
            Box(modifier = Modifier.size(72.dp)) {
                AsyncImage(
                    model = match.matchedUser?.fullName?.let {
                        "https://ui-avatars.com/api/?name=$it&background=2563EB&color=fff&size=200"
                    } ?: "https://ui-avatars.com/api/?name=User&background=2563EB&color=fff&size=200",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.5.dp, AppTheme.Blue100, CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Score badge
                Surface(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd),
                    color  = scoreColor,
                    shape  = CircleShape,
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "$score%",
                            color = Color.White,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                match.matchedUser?.fullName ?: "Anonymous",
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 14.sp,
                color      = AppTheme.Slate900,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                match.matchedUser?.occupation ?: "User",
                fontSize   = 11.sp,
                color      = AppTheme.Slate400,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            // Score bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.Slate100)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(score / 100f)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(listOf(scoreColor.copy(alpha = 0.6f), scoreColor)),
                            RoundedCornerShape(2.dp)
                        )
                )
            }

            Spacer(Modifier.height(10.dp))

            // Best Match pill
            Surface(
                color  = AppTheme.Blue50,
                shape  = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, AppTheme.Blue100)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = AppTheme.Indigo500, modifier = Modifier.size(11.dp))
                    Spacer(Modifier.width(5.dp))
                    Text("Best Match", color = AppTheme.Blue700, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  EMPTY PLACEHOLDER
// ─────────────────────────────────────────
@Composable
private fun AIEmptyPlaceholder(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        shape  = RoundedCornerShape(16.dp),
        color  = AppTheme.Blue50,
        border = BorderStroke(1.dp, AppTheme.Blue100)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(
                    color       = AppTheme.Blue500,
                    strokeWidth = 2.dp,
                    modifier    = Modifier.size(18.dp)
                )
                Text(text, color = AppTheme.Slate400, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─────────────────────────────────────────
//  LOADING STATE
// ─────────────────────────────────────────
@Composable
private fun AIResultsLoadingState(modifier: Modifier = Modifier) {
    val alpha by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0.4f,
        targetValue  = 0.85f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(AppTheme.Slate100.copy(alpha = alpha))
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.fillMaxWidth().height(130.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(22.dp)))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) {
                    Box(Modifier.weight(1f).height(80.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(16.dp)))
                }
            }
            Box(Modifier.fillMaxWidth(0.5f).height(14.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(6.dp)))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(2) {
                    Box(Modifier.width(160.dp).height(260.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(20.dp)))
                }
            }
        }
    }
}