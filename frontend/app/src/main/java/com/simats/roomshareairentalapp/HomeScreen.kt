@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.simats.roomshareairentalapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

// ── Design tokens (mirrors web design system) ─────────────────────────────────
private object DS {
    // Primary brand blue — matches #1E63FF
    val Primary        = Color(0xFF1E63FF)
    val PrimaryDark    = Color(0xFF0D1E3C)
    val PrimaryAlpha8  = Color(0x141E63FF)   // primary/8
    val PrimaryAlpha15 = Color(0x261E63FF)   // primary/15

    // Surfaces
    val Background     = Color(0xFFF8F8F6)
    val Card           = Color.White
    val CardBorder     = Color(0x1A000000)   // border/50

    // Emerald (success / verified / high match)
    val Emerald        = Color(0xFF10B981)
    val EmeraldAlpha10 = Color(0x1A10B981)

    // Violet (AI / recommendations)
    val Violet         = Color(0xFF7C3AED)
    val VioletAlpha10  = Color(0x1A7C3AED)

    // Amber (mid match / stars)
    val Amber          = Color(0xFFF59E0B)
    val AmberAlpha10   = Color(0x1AF59E0B)

    // Text
    val TextPrimary    = Color(0xFF0F0F0E)
    val TextSecondary  = Color(0xFF6B6B68)
    val TextMuted      = Color(0xFF9E9E9A)

    // Corner radii
    val RadiusSm  = 8.dp
    val RadiusMd  = 12.dp
    val RadiusLg  = 16.dp
    val RadiusXl  = 20.dp

    // Type scale
    val LabelCaps   = 9.sp   // text-[9px] uppercase tracking-widest
    val LabelXs     = 10.sp  // text-[10px]
    val BodySm      = 12.sp
    val Body        = 13.sp
    val Title       = 15.sp
    val Heading     = 18.sp
    val Display     = 24.sp
}

// ── Reusable: Section Header ──────────────────────────────────────────────────
// Mirrors SectionHeader from HomePage: icon container + extrabold title + caps subtitle + action
@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color = DS.Primary,
    iconBg: Color = DS.PrimaryAlpha8,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Icon container — w-9 h-9 rounded-xl
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(DS.RadiusMd))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = title,
                    fontSize = DS.Title,
                    fontWeight = FontWeight.ExtraBold,
                    color = DS.TextPrimary,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = subtitle.uppercase(),
                    fontSize = DS.LabelCaps,
                    fontWeight = FontWeight.Bold,
                    color = DS.TextMuted,
                    letterSpacing = 1.2.sp
                )
            }
        }
        if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(DS.RadiusMd)
            ) {
                Text(
                    text = actionLabel,
                    fontSize = DS.LabelXs,
                    fontWeight = FontWeight.ExtraBold,
                    color = DS.Primary,
                    letterSpacing = 0.sp
                )
                Spacer(Modifier.width(2.dp))
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = DS.Primary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

// ── Reusable: Tag chip — text-[9px] uppercase tracking-wider bg-primary/8 ─────
@Composable
private fun TagChip(label: String, color: Color = DS.Primary, bg: Color = DS.PrimaryAlpha8) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(DS.RadiusSm))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = DS.LabelCaps,
            fontWeight = FontWeight.ExtraBold,
            color = color.copy(alpha = 0.7f),
            letterSpacing = 1.sp
        )
    }
}

// ── Reusable: Skeleton shimmer block ──────────────────────────────────────────
@Composable
private fun ShimmerBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(DS.RadiusMd))
            .background(DS.CardBorder)
    )
}

@Composable
private fun RoommateSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(DS.RadiusXl))
            .background(DS.Card)
            .border(1.dp, DS.CardBorder, RoundedCornerShape(DS.RadiusXl))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ShimmerBlock(Modifier.size(44.dp).clip(RoundedCornerShape(DS.RadiusMd)))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ShimmerBlock(Modifier.width(120.dp).height(12.dp))
                ShimmerBlock(Modifier.width(80.dp).height(10.dp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ShimmerBlock(Modifier.width(56.dp).height(20.dp))
            ShimmerBlock(Modifier.width(48.dp).height(20.dp))
        }
        ShimmerBlock(Modifier.fillMaxWidth().height(36.dp))
    }
}

// ── Reusable: Roommate card — mirrors RoommateCard from HomePage ──────────────
@Composable
fun DiscoverRoommateCardComponent(
    roommate: DiscoverRoommate,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val score = roommate.matchPercentage?.toInt() ?: 0
    val displayName = roommate.fullName?.trim() ?: "User"
    val initials = displayName
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
    val photoUrl = roommate.photos?.firstOrNull()?.image
  
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DS.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = DS.Card),
        border = BorderStroke(1.dp, DS.CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Avatar row ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar: photo or letter fallback
                    Box(modifier = Modifier.size(46.dp)) {
                        if (!photoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(DS.RadiusMd))
                                    .border(1.5.dp, DS.CardBorder, RoundedCornerShape(DS.RadiusMd))
                            )
                        } else {
                            // Letter avatar — navy gradient bg, white initial(s)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(DS.RadiusMd))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1E63FF), Color(0xFF0D1E3C))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }

                        // Online dot
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(DS.Emerald)
                                .border(2.dp, DS.Card, CircleShape)
                        )
                    }

                    // Name + location
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = buildString {
                                append(displayName)
                                if (roommate.age != null) append(", ${roommate.age}")
                            },
                            fontSize = DS.Body,
                            fontWeight = FontWeight.ExtraBold,
                            color = DS.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (roommate.requestStatus != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(DS.RadiusSm))
                                    .background(Color(0xFFFFF7ED))
                                    .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(DS.RadiusSm))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "Roomshare ${roommate.requestStatus}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFD97706),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = DS.Primary.copy(alpha = 0.5f),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = roommate.city ?: "Chennai",
                                fontSize = DS.BodySm,
                                fontWeight = FontWeight.Medium,
                                color = DS.TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Match % badge
                if (score > 0) {
                    val (badgeBg, badgeText) = when {
                        score >= 85 -> DS.EmeraldAlpha10 to DS.Emerald
                        score >= 70 -> DS.PrimaryAlpha8 to DS.Primary
                        else        -> DS.AmberAlpha10 to DS.Amber
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(DS.RadiusSm))
                            .background(badgeBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = badgeText,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "$score%",
                            fontSize = DS.LabelCaps,
                            fontWeight = FontWeight.ExtraBold,
                            color = badgeText,
                            letterSpacing = 0.sp
                        )
                    }
                }
            }

            // ── Tags ──────────────────────────────────────────────────────────
            val tags = roommate.tags
                ?.filter { it.isNotBlank() }
                ?.take(3)
                ?.takeIf { it.isNotEmpty() }
                ?: listOf("Verified", "Searching")

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach { tag -> TagChip(tag) }
            }

            // ── CTA ───────────────────────────────────────────────────────────
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(DS.RadiusMd),
                border = BorderStroke(1.dp, DS.PrimaryAlpha15),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DS.PrimaryAlpha8,
                    contentColor = DS.Primary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = null, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Chat Now",
                    fontSize = DS.LabelXs,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp
                )
            }
        }
    }
}
// ── Reusable: Room card — mirrors RoomCard from HomePage ──────────────────────
@Composable
fun HomeRoomListItemComponent(
    room: HomeRoomItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DS.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = DS.Card),
        border = BorderStroke(1.dp, DS.CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        Column {
            // Image
            Box(modifier = Modifier.fillMaxWidth().height(176.dp)) {
                AsyncImage(
                    model = room.photos?.firstOrNull()?.image
                        ?: "https://images.unsplash.com/photo-1522770179533-24471fcdba45?w=500&auto=format",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Verified badge — top left
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(DS.RadiusSm))
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Verified, null, tint = DS.Emerald, modifier = Modifier.size(12.dp))
                    Text(
                        text = "VERIFIED",
                        fontSize = DS.LabelCaps,
                        fontWeight = FontWeight.ExtraBold,
                        color = DS.Emerald,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Body
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Micro label + rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Apartment".uppercase(), // room doesn't have type yet, defaulting
                        fontSize = DS.LabelCaps,
                        fontWeight = FontWeight.ExtraBold,
                        color = DS.Primary,
                        letterSpacing = 1.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Default.Star, null, tint = DS.Amber, modifier = Modifier.size(12.dp))
                        Text(
                            text = "4.9", // rating not in model yet
                            fontSize = DS.LabelCaps,
                            fontWeight = FontWeight.ExtraBold,
                            color = DS.TextPrimary
                        )
                    }
                }

                // Title
                Text(
                    text = room.apartmentTitle ?: "Apartment",
                    fontSize = DS.Body,
                    fontWeight = FontWeight.ExtraBold,
                    color = DS.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Outlined.LocationOn, null, tint = DS.Primary.copy(alpha = 0.6f), modifier = Modifier.size(12.dp))
                    Text(
                        text = room.address ?: room.city ?: "Chennai",
                        fontSize = DS.BodySm,
                        fontWeight = FontWeight.Medium,
                        color = DS.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(4.dp))
                HorizontalDivider(color = DS.CardBorder, thickness = 1.dp)
                Spacer(Modifier.height(4.dp))

                // Price + arrow CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "MONTHLY",
                            fontSize = DS.LabelCaps,
                            fontWeight = FontWeight.ExtraBold,
                            color = DS.TextMuted,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "₹${room.monthlyRent ?: "—"}",
                                fontSize = DS.Heading,
                                fontWeight = FontWeight.ExtraBold,
                                color = DS.Primary,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "/mo",
                                fontSize = DS.BodySm,
                                fontWeight = FontWeight.Medium,
                                color = DS.TextMuted,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                    // Arrow button — w-8 h-8 rounded-xl bg-primary/8 group-hover:bg-primary
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(DS.RadiusMd))
                            .background(DS.PrimaryAlpha8),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowForward,
                            null,
                            tint = DS.Primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Reusable: AI CTA banner — mirrors HomePage AI CTA ────────────────────────
@Composable
private fun AiCtaBanner(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(DS.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = DS.Card),
        border = BorderStroke(1.dp, DS.PrimaryAlpha15),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(DS.RadiusMd))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(DS.Primary, DS.PrimaryDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Need help finding a flatmate?",
                    fontSize = DS.Body,
                    fontWeight = FontWeight.ExtraBold,
                    color = DS.TextPrimary
                )
                Text(
                    text = "ASK AI FOR PERSONALISED PICKS",
                    fontSize = DS.LabelCaps,
                    fontWeight = FontWeight.Bold,
                    color = DS.TextMuted,
                    letterSpacing = 1.sp
                )
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(DS.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = DS.Primary),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Ask AI", fontSize = DS.LabelXs, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.sp)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, modifier = Modifier.size(12.dp))
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────
@Composable
private fun EmptyState(
    title: String,
    subtitle: String,
    icon: ImageVector,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(DS.RadiusXl))
            .background(DS.Card)
            .border(1.dp, DS.CardBorder, RoundedCornerShape(DS.RadiusXl))
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(DS.RadiusMd))
                .background(DS.PrimaryAlpha8),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = DS.TextSecondary, modifier = Modifier.size(20.dp))
        }
        Text(
            text = title,
            fontSize = DS.Title,
            fontWeight = FontWeight.ExtraBold,
            color = DS.TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            fontSize = DS.BodySm,
            fontWeight = FontWeight.Medium,
            color = DS.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(DS.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = DS.Primary),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(actionLabel, fontSize = DS.LabelXs, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.sp)
            }
        }
    }
}

// ── Main screen ───────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    discoverRoommates: List<DiscoverRoommate> = emptyList(),
    isDiscoverLoading: Boolean = false,
    homeRooms: List<HomeRoomItem> = emptyList(),
    isRoomsLoading: Boolean = false,
    onViewMatchesClick: () -> Unit = {},
    onDiscoverRoommateClick: (DiscoverRoommate) -> Unit = {},
    onHomeRoomClick: (HomeRoomItem) -> Unit = {},
    onSaveFavorite: (String) -> Unit = {},
    onAIButtonClick: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    isError: Boolean = false,
    onRetry: () -> Unit = {},
    refreshHomeData: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = DS.Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAIButtonClick,
                containerColor = DS.Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, "AI Assistant", modifier = Modifier.size(22.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Hero ──────────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(DS.Primary, Color(0xFF0A1628), Color(0xFF080F1E)),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        )
                        .statusBarsPadding()
                        .padding(top = 16.dp, bottom = 40.dp, start = 20.dp, end = 20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Top nav
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 28.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onMenuClick) {
                                Icon(Icons.Default.Menu, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = onNotificationsClick) {
                                    Icon(Icons.Default.Notifications, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(22.dp))
                                }
                                IconButton(onClick = onProfileClick) {
                                    Icon(Icons.Default.AccountCircle, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(22.dp))
                                }
                            }
                        }

                        // AI pill badge — bg-primary/8 border border-primary/15 rounded-full
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color.White.copy(alpha = 0.10f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(11.dp))
                            Text(
                                "AI-POWERED MATCHING",
                                fontSize = DS.LabelCaps,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White.copy(alpha = 0.9f),
                                letterSpacing = 1.2.sp
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // Greeting
                        Text(
                            "Hey There 👋",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(6.dp))

                        // Headline — font-extrabold tracking-tight
                        Text(
                            "Find Your Perfect\nRoommate & Home.",
                            color = Color.White,
                            fontSize = DS.Display,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(Modifier.height(12.dp))

                        // Sub copy — text-[10px] font-medium
                        Text(
                            "RoomShare AI connects you with compatible people based on lifestyle, preferences, and location.",
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(Modifier.height(28.dp))

                        // Search bar — bg-white/10 border border-white/15 rounded-xl
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it; onSearch(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            placeholder = {
                                Text(
                                    "Search by area, name, or budget…",
                                    color = Color.White.copy(alpha = 0.4f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                            },
                            shape = RoundedCornerShape(DS.RadiusLg),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = Color.White.copy(alpha = 0.30f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedContainerColor   = Color.White.copy(alpha = 0.12f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                                focusedTextColor   = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            )
                        )
                    }
                }
            }

            // ── Spacing after hero ────────────────────────────────────────────
            item { Spacer(Modifier.height(28.dp)) }

            // ── Roommate Matches section header ───────────────────────────────
            item {
                SectionHeader(
                    title = "Top Roommate Matches",
                    subtitle = "Based on your compatibility profile",
                    icon = Icons.Default.Group,
                    actionLabel = "View All",
                    onAction = onViewMatchesClick
                )
                Spacer(Modifier.height(12.dp))
            }

            // Roommate cards or states
            if (isDiscoverLoading) {
                items(3) {
                    RoommateSkeleton(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp)
                    )
                }
            } else if (discoverRoommates.isEmpty()) {
                item {
                    EmptyState(
                        title = "No roommates found",
                        subtitle = "Complete your profile for better recommendations.",
                        icon = Icons.Default.Group
                    )
                }
            } else {
                items(discoverRoommates.take(3)) { rm ->
                    DiscoverRoommateCardComponent(
                        roommate = rm,
                        onClick = { onDiscoverRoommateClick(rm) },
                        onSaveToggle = { onSaveFavorite(rm.email ?: "") },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp)
                    )
                }
            }

            // ── Verified Listings section header ──────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                SectionHeader(
                    title = "Verified Listings",
                    subtitle = "Top rated housing near you",
                    icon = Icons.Outlined.Home,
                    iconTint = Color(0xFFF59E0B),
                    iconBg = Color(0x1AF59E0B)
                )
                Spacer(Modifier.height(12.dp))
            }

            // Room cards or states
            if (isRoomsLoading) {
                items(3) {
                    // Simple shimmer for room card
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(DS.RadiusXl))
                            .background(DS.Card)
                            .border(1.dp, DS.CardBorder, RoundedCornerShape(DS.RadiusXl))
                    ) {
                        ShimmerBlock(Modifier.fillMaxWidth().height(176.dp))
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ShimmerBlock(Modifier.width(60.dp).height(10.dp))
                            ShimmerBlock(Modifier.fillMaxWidth(0.8f).height(14.dp))
                            ShimmerBlock(Modifier.fillMaxWidth(0.6f).height(11.dp))
                            Spacer(Modifier.height(4.dp))
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                ShimmerBlock(Modifier.width(80.dp).height(20.dp))
                                ShimmerBlock(Modifier.size(32.dp))
                            }
                        }
                    }
                }
            } else if (homeRooms.isEmpty()) {
                item {
                    EmptyState(
                        title = "No listings found",
                        subtitle = "Check back soon for new properties.",
                        icon = Icons.Outlined.Home
                    )
                }
            } else {
                items(homeRooms.take(3)) { room ->
                    HomeRoomListItemComponent(
                        room = room,
                        onClick = { onHomeRoomClick(room) },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp)
                    )
                }
            }

            // ── AI CTA banner ─────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                AiCtaBanner(onClick = onAIButtonClick)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
fun HomeScreenPreview() {
    RoomshareAIRentalAppTheme {
        HomeScreen(
            discoverRoommates = emptyList(),
            homeRooms = emptyList()
        )
    }
}