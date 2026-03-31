@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.Chat
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ── Design tokens (mirrors web palette) ──────────────────────────────────────
private val Blue = Color(0xFF1E63FF)
private val BlueDark = Color(0xFF0D1E3C)
private val BlueDeep = Color(0xFF080F1E)
private val BlueSurface = Color(0xFFF0F5FF)
private val Emerald = Color(0xFF10B981)
private val Amber = Color(0xFFF59E0B)
private val Rose = Color(0xFFF43F5E)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF475569)
private val TextMuted = Color(0xFF94A3B8)
private val Border = Color(0xFFE2E8F0)
private val CardBg = Color(0xFFFFFFFF)
private val MutedBg = Color(0xFFF8FAFC)

private val NavyGradient = Brush.linearGradient(
    colors = listOf(Blue, BlueDark, BlueDeep)
)

// ── Shared card surface ───────────────────────────────────────────────────────
@Composable
private fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val baseModifier = modifier
        .shadow(
            elevation = 2.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Blue.copy(alpha = 0.06f),
            spotColor = Blue.copy(alpha = 0.06f)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(CardBg)
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)

    Column(modifier = baseModifier, content = content)
}

// ── Section header ────────────────────────────────────────────────────────────
@Composable
private fun SectionHeader(
    icon: ImageVector,
    iconTint: Color = Blue,
    iconBg: Color = BlueSurface,
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )
            }
        }
        if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    actionLabel,
                    color = Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ── Status badge ──────────────────────────────────────────────────────────────
@Composable
private fun StatusBadge(label: String, containerColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AI AGENT SEARCH CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AIAgentSearchContent(onAIButtonClick: () -> Unit) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        // Navy gradient header band
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyGradient)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Subtle grid overlay
            Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {}

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "AI",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "AI Concierge",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
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
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Find hotels, rooms & roommates near any location — just ask.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 19.sp
                )
            }
        }

        // Body
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "QUICK SEARCH",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextMuted,
                letterSpacing = 1.2.sp
            )
            Spacer(Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val suggestions = listOf(
                    "Hotels in Mumbai",
                    "Rooms under ₹5000",
                    "Top rated stays",
                    "Nearby locations"
                )
                suggestions.forEach { s ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(BlueSurface)
                            .border(1.dp, Blue.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .clickable { onAIButtonClick() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            s,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Blue
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onAIButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Start AI Chat",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ROOMMATE CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeRoommateCardComponent(
    roommate: Roommate,
    onViewProfileClick: (Roommate) -> Unit,
    onSaveToggle: (String) -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onViewProfileClick(roommate) }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(modifier = Modifier.size(52.dp)) {
                AsyncImage(
                    model = roommate.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Border, RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                )
                // Online dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.BottomEnd)
                        .border(2.dp, CardBg, CircleShape)
                        .clip(CircleShape)
                        .background(Emerald)
                )
            }

            Spacer(Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${roommate.name}${if (roommate.age > 0) ", ${roommate.age}" else ""}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onSaveToggle(roommate.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (roommate.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (roommate.isSaved) Rose else TextMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Blue.copy(alpha = 0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = roommate.location,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tags
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        roommate.tags.take(2).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BlueSurface)
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    tag,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Blue.copy(alpha = 0.7f),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = roommate.price,
                        color = Blue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Divider + CTA
        HorizontalDivider(color = Border, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewProfileClick(roommate) }
                .padding(horizontal = 14.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "View Profile",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Blue,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// STAT CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeStatCardComponent(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AI INSIGHT / MATCH CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeAIInsightCardComponent(
    discoverRoommates: List<DiscoverRoommate>,
    onViewMatchesClick: () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyGradient)
                .padding(20.dp)
        ) {
            Column {
                // Header row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "AI",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "AI SMART MATCH",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "You have ${discoverRoommates.size} new potential roommate matches this week!",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.2).sp
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)
                Spacer(Modifier.height(14.dp))

                // Avatars + CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stacked avatars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val avatarUrls = discoverRoommates.take(3).map { it.photo }
                        avatarUrls.forEachIndexed { index, url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .offset(x = (index * -8).dp)
                                    .border(2.dp, Color.White, CircleShape)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                            )
                        }
                        if (discoverRoommates.size > 3) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .offset(x = (avatarUrls.size * -8).dp)
                                    .border(2.dp, Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .background(Blue.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "+${discoverRoommates.size - 3}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    // CTA
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(11.dp))
                            .background(Color.White)
                            .clickable { onViewMatchesClick() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "View Matches",
                            color = Blue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB BUTTON
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeTabButtonComponent(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .then(
                if (isSelected) Modifier.shadow(2.dp, RoundedCornerShape(10.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Blue else TextMuted,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ROOM CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeRoomCardComponent(
    room: Room,
    onClick: (Room) -> Unit,
    onSaveToggle: (String) -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(room) }
    ) {
        // Image area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = room.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery)
            )

            // Gradient scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)),
                            startY = 80f
                        )
                    )
            )

            // Top badges row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (room.matchPercentage > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(7.dp))
                                .background(Color.White.copy(alpha = 0.92f))
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Text(
                                "${room.matchPercentage}% Match",
                                color = Blue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                if (room.isAvailable) Emerald.copy(alpha = 0.9f)
                                else Rose.copy(alpha = 0.9f)
                            )
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Text(
                            if (room.isAvailable) "Available" else "Sold Out",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Fav button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .clickable { onSaveToggle(room.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (room.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = if (room.isSaved) Rose else TextMuted,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }

        // Details
        Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        room.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.2).sp
                    )
                    Spacer(Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Blue.copy(alpha = 0.5f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            room.location,
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        room.price,
                        color = Blue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        "/mo",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Border, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Meta chips
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${room.roommateCount} Roommates",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            room.bathType,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted
                        )
                    }
                }

                // Book CTA
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (room.isAvailable) Blue else Border)
                        .then(
                            if (room.isAvailable) Modifier.clickable { onClick(room) }
                            else Modifier
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        if (room.isAvailable) "Book Room" else "Unavailable",
                        color = if (room.isAvailable) Color.White else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}