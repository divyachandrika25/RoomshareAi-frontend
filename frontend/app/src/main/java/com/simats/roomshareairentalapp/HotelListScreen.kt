package com.simats.roomshareairentalapp

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ─────────────────────────────────────────
//  DESIGN TOKENS — Single source of truth
// ─────────────────────────────────────────
// ─────────────────────────────────────────
//  MAIN SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelListScreen(
    onMenuClick: () -> Unit = {},
    hotels: List<HotelItem>,
    recommended: List<HotelItem>?,
    userAreas: List<String>?,
    isLoading: Boolean,
    isNavigating: Boolean = false,
    onSearchClick: (String) -> Unit,
    onHotelClick: (HotelItem) -> Unit,
    onToggleFavorite: (String) -> Unit = {},
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val allResults = remember(hotels, recommended) {
        buildList {
            recommended?.let { addAll(it) }
            addAll(hotels)
        }.distinctBy { it.id }
    }

    val filtered = remember(searchQuery, allResults, isLoading) {
        if (searchQuery.isNotBlank() && !isLoading) {
            allResults.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.city.contains(searchQuery, ignoreCase = true)
            }
        } else null
    }

    Scaffold(
        containerColor = AppTheme.Background,
        topBar = {
            HotelTopBar(
                onMenuClick = onMenuClick,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Hero Header ──
            item {
                HeroHeader(
                    userAreas = userAreas,
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it }
                )
            }

            // ── Loading Skeletons ──
            if (isLoading) {
                items(3) { SkeletonCard() }
                return@LazyColumn
            }

            // ── Search Results ──
            if (filtered != null) {
                item {
                    SearchResultsHeader(
                        count = filtered.size,
                        query = searchQuery,
                        onClear = { searchQuery = "" }
                    )
                }
                if (filtered.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.SearchOff,
                            title = "No hotels found",
                            subtitle = "Try searching for another city or hotel name",
                            actionLabel = "Clear Search",
                            onAction = { searchQuery = "" }
                        )
                    }
                } else {
                    items(filtered, key = { it.id }) { hotel ->
                        HotelCard(
                            hotel = hotel,
                            isRecommended = hotel.reason != null,
                            isNavigating = isNavigating,
                            onHotelClick = onHotelClick
                        )
                    }
                }
                return@LazyColumn
            }

            // ── Recommended Section ──
            if (!recommended.isNullOrEmpty()) {
                item {
                    SectionHeader(
                        icon = Icons.Default.AutoAwesome,
                        iconBg = AppTheme.Violet,
                        title = "Recommended for You",
                        subtitle = "Curated based on your preferences"
                    )
                }
                items(recommended, key = { it.id }) { hotel ->
                    HotelCard(
                        hotel = hotel,
                        isRecommended = true,
                        isNavigating = isNavigating,
                        onHotelClick = onHotelClick
                    )
                }
            }

            // ── All Hotels Section ──
            if (hotels.isNotEmpty()) {
                item {
                    SectionHeader(
                        icon = Icons.Default.Business,
                        iconBg = AppTheme.Primary,
                        title = "All Hotels",
                        subtitle = "${hotels.size} available properties"
                    )
                }
                items(hotels, key = { it.id }) { hotel ->
                    HotelCard(
                        hotel = hotel,
                        isRecommended = false,
                        isNavigating = isNavigating,
                        onHotelClick = onHotelClick
                    )
                }
            }

            // ── Empty State ──
            if (hotels.isEmpty() && recommended.isNullOrEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Default.Business,
                        title = "No Hotels Available",
                        subtitle = "New listings are added regularly. Check back soon."
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotelTopBar(
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
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
            Spacer(Modifier.weight(1f))
            // Logo pill
            Surface(
                color = AppTheme.PrimaryAlpha8,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(AppTheme.PremiumGradient, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                         Icon(Icons.Default.HomeWork, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "StayFinder",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.Primary
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onMenuClick) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AppTheme.Divider, RoundedCornerShape(AppTheme.RadiusMd)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = AppTheme.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        HorizontalDivider(color = AppTheme.Divider)
    }
}

// ─────────────────────────────────────────
//  HERO HEADER
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeroHeader(
    userAreas: List<String>?,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.HeaderGradient)
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
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
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 32.dp)
        ) {
            // Greeting pill
            if (userAreas?.isNotEmpty() == true) {
                Surface(
                    color = Color.White.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = AppTheme.PrimaryLight,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            userAreas.joinToString(" · "),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Text(
                "Hotels & Stays",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp,
                lineHeight = 34.sp
            )
            Text(
                if (userAreas?.isNotEmpty() == true)
                    "Personalized picks, just for you"
                else
                    "Find and book premium rooms",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Search Bar
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.RadiusLg),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    placeholder = {
                        Text(
                            "Search hotels, cities…",
                            fontSize = 14.sp,
                            color = AppTheme.TextTertiary,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = AppTheme.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = AppTheme.TextTertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = AppTheme.Primary,
                        focusedTextColor = AppTheme.TextPrimary,
                        unfocusedTextColor = AppTheme.TextPrimary
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  SECTION HEADER
// ─────────────────────────────────────────
@Composable
private fun SectionHeader(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(iconBg, RoundedCornerShape(AppTheme.RadiusMd)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppTheme.TextPrimary,
                lineHeight = 20.sp
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextTertiary
            )
        }
    }
}

// ─────────────────────────────────────────
//  SEARCH RESULTS HEADER
// ─────────────────────────────────────────
@Composable
private fun SearchResultsHeader(
    count: Int,
    query: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "$count results",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppTheme.TextPrimary
            )
            Text(
                "for \"$query\"",
                fontSize = 13.sp,
                color = AppTheme.TextTertiary,
                fontWeight = FontWeight.Medium
            )
        }
        TextButton(
            onClick = onClear,
            colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.Primary)
        ) {
            Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Clear", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ─────────────────────────────────────────
//  HOTEL CARD
// ─────────────────────────────────────────
@Composable
fun HotelCard(
    hotel: HotelItem,
    isRecommended: Boolean,
    isNavigating: Boolean,
    onHotelClick: (HotelItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(AppTheme.RadiusLg),
                ambientColor = AppTheme.Primary.copy(alpha = 0.1f),
                spotColor = AppTheme.Primary.copy(alpha = 0.2f)
            )
            .clickable(enabled = !isNavigating) { onHotelClick(hotel) },
        shape = RoundedCornerShape(AppTheme.RadiusLg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            // ── Image Section ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                if (hotel.imageUrl != null) {
                    AsyncImage(
                        model = hotel.imageUrl,
                        contentDescription = hotel.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(AppTheme.PrimaryAlpha8, AppTheme.Divider)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.HomeWork,
                            null,
                            tint = AppTheme.Primary.copy(alpha = 0.3f),
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )

                // ── Top Row Badges ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Rating badge
                    if (hotel.rating != null) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    null,
                                    tint = AppTheme.Warning,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    hotel.rating.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AppTheme.TextPrimary
                                )
                            }
                        }
                    } else {
                        Spacer(Modifier.size(1.dp))
                    }

                    // Right side: Rec badge
                    if (isRecommended) {
                        Surface(
                            color = AppTheme.Violet,
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "FOR YOU",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }
                    }
                }

                // ── Price Tag (bottom right) ──
                Surface(
                    color = AppTheme.Primary,
                    shape = RoundedCornerShape(topStart = 14.dp, bottomEnd = 0.dp),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "₹${hotel.startingPrice.toInt()}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            "/night",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(start = 2.dp, bottom = 1.dp)
                        )
                    }
                }
            }

            // ── Content Section ──
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                // Name & City
                Text(
                    hotel.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppTheme.TextPrimary,
                    lineHeight = 22.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            null,
                            tint = AppTheme.Primary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        hotel.city,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextSecondary
                    )
                }

                // Amenity Chips
                if (!hotel.amenities.isNullOrEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        hotel.amenities.take(3).forEach { amenity ->
                            AmenityChip(label = amenity)
                        }
                    }
                }

                // Recommendation Reason
                if (isRecommended && hotel.reason != null) {
                    Spacer(Modifier.height(14.dp))
                    Surface(
                        color = AppTheme.PrimaryAlpha8,
                        shape = RoundedCornerShape(AppTheme.RadiusMd),
                        border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                null,
                                tint = AppTheme.Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                hotel.reason,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.Primary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = AppTheme.Divider
                )

                // Footer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Home,
                            null,
                            tint = AppTheme.TextTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "${hotel.totalRooms} property types",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextTertiary
                        )
                    }

                    // CTA Button
                    Surface(
                        color = AppTheme.Primary,
                        shape = RoundedCornerShape(AppTheme.RadiusMd)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "View Rooms",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  AMENITY CHIP
// ─────────────────────────────────────────
@Composable
private fun AmenityChip(label: String) {
    Surface(
        color = AppTheme.PrimaryAlpha8,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12)
    ) {
        Text(
            label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = AppTheme.Primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            letterSpacing = 0.6.sp
        )
    }
}

// ─────────────────────────────────────────
//  SKELETON LOADING CARD
// ─────────────────────────────────────────
@Composable
private fun SkeletonCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(AppTheme.RadiusLg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(AppTheme.Divider.copy(alpha = alpha))
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp))
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp)
                        .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp))
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(24.dp)
                                .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  EMPTY STATE
// ─────────────────────────────────────────
@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
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
                icon,
                null,
                tint = AppTheme.Primary,
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppTheme.TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            subtitle,
            fontSize = 14.sp,
            color = AppTheme.TextTertiary,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(actionLabel, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}