package com.simats.roomshareairentalapp

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

private object MatchesDesign {
    val Blue50 = Color(0xFFEFF6FF)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue200 = Color(0xFFBFDBFE)
    val Blue400 = Color(0xFF60A5FA)
    val Blue500 = Color(0xFF3B82F6)
    val Blue600 = Color(0xFF2563EB)
    val Blue700 = Color(0xFF1D4ED8)
    val Blue800 = Color(0xFF1E40AF)
    val Blue900 = Color(0xFF1E3A8A)
    val Indigo500 = Color(0xFF6366F1)
    val Slate50 = Color(0xFFF8FAFC)
    val Slate100 = Color(0xFFF1F5F9)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate400 = Color(0xFF94A3B8)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate900 = Color(0xFF0F172A)
    val Success = Color(0xFF10B981)
}

// ─────────────────────────────────────────
//  MATCHES SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    onMenuClick: () -> Unit = {},
    email: String,
    onMatchClick: (MatchResult) -> Unit = {},
    onViewGroupClick: (String) -> Unit = {},
    onStartChatClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveRoommateToggle: (String) -> Unit = {},
    onSaveGroupToggle: (String) -> Unit = {},
    onSaveFavorite: (String) -> Unit = {}
) {
    var matches       by remember { mutableStateOf<List<MatchResult>>(emptyList()) }
    var isLoading     by remember { mutableStateOf(true) }
    var errorMessage  by remember { mutableStateOf<String?>(null) }
    var searchQuery   by remember { mutableStateOf("") }

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            try {
                val response = RetrofitClient.instance.getMatches(email)
                if (response.isSuccessful && response.body()?.success == true) {
                    matches = response.body()?.matches ?: emptyList()
                } else {
                    errorMessage = response.body()?.error ?: "Failed to load matches"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    val filteredMatches = remember(matches, searchQuery) {
        if (searchQuery.isBlank()) matches
        else matches.filter {
            (it.matchedUser?.fullName ?: "").contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = AppTheme.Background,
        topBar = {
            MatchesTopBar(onMenuClick = onMenuClick, onBackClick = onBackClick)
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
                MatchesHero(
                    totalMatches  = matches.size,
                    searchQuery   = searchQuery,
                    onSearchChange = { searchQuery = it }
                )
            }

            // ── Loading Skeletons ──
            if (isLoading) {
                items(3) { MatchSkeletonCard() }
                return@LazyColumn
            }

            // ── Error ──
            if (errorMessage != null) {
                item {
                    MatchesEmptyState(
                        icon     = Icons.Default.ErrorOutline,
                        title    = "Something went wrong",
                        subtitle = errorMessage ?: "Please try again later.",
                        showClear = false,
                        onClear  = {}
                    )
                }
                return@LazyColumn
            }

            // ── Empty ──
            if (filteredMatches.isEmpty()) {
                item {
                    MatchesEmptyState(
                        icon     = if (searchQuery.isNotBlank()) Icons.Default.SearchOff else Icons.Default.PeopleAlt,
                        title    = if (searchQuery.isNotBlank()) "No results found" else "Searching for matches…",
                        subtitle = if (searchQuery.isNotBlank())
                            "Try a different name or search term."
                        else
                            "Complete your profile to build compatibility scores.",
                        showClear = searchQuery.isNotBlank(),
                        onClear  = { searchQuery = "" }
                    )
                }
                return@LazyColumn
            }

            // ── Section header ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${filteredMatches.size} compatible matches",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextSecondary
                    )
                    Surface(
                        color = AppTheme.PrimaryAlpha8,
                        shape = RoundedCornerShape(AppTheme.RadiusSm),
                        border = BorderStroke(1.dp, AppTheme.PrimaryAlpha15)
                    ) {
                        Text(
                            "Sorted by compatibility",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.Primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // ── Match Cards ──
            items(filteredMatches, key = { it.matchedUser?.id ?: it.hashCode().toString() }) { match ->
                MatchCard(
                    match          = match,
                    onClick        = { onMatchClick(match) },
                    onMessageClick = { match.matchedUser?.let { onStartChatClick(it.id) } },
                    onFavoriteClick = { match.matchedUser?.let { onSaveFavorite(it.id) } }
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun MatchesTopBar(
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
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
            IconButton(onClick = onBackClick) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(AppTheme.RadiusMd)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = AppTheme.Primary, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Text("Compatibility Matches", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary)
            Spacer(Modifier.weight(1f))
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
        }
        HorizontalDivider(color = AppTheme.Divider)
    }
}

// ─────────────────────────────────────────
//  HERO HEADER
// ─────────────────────────────────────────
@Composable
private fun MatchesHero(
    totalMatches: Int,
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
                .size(240.dp)
                .offset(x = (-80).dp, y = (-70).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 36.dp)
        ) {
            // AI badge pill
            Surface(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "AI-POWERED MATCHING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.8.sp
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            Text(
                "Your Premium Roommates",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp,
                lineHeight = 34.sp
            )

            // Stat pills
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroStatPill("$totalMatches Total matches")
                HeroStatPill("Verified Profiles", highlight = true)
            }

            // Search + Filter row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search bar
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(AppTheme.RadiusLg),
                    shadowElevation = 10.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
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
                                if (searchQuery.isEmpty()) Text("Search matches…", fontSize = 14.sp, color = AppTheme.TextTertiary, fontWeight = FontWeight.Medium)
                                inner()
                            }
                        )
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Close, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // Filter button
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(AppTheme.RadiusLg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable { /* filter action */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FilterList, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStatPill(label: String, highlight: Boolean = false) {
    Surface(
        color = if (highlight) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp),
        border = if (highlight) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// ─────────────────────────────────────────
//  MATCH CARD
// ─────────────────────────────────────────
@Composable
fun MatchCard(
    match: MatchResult,
    onClick: () -> Unit,
    onMessageClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val user  = match.matchedUser ?: return
    val score = (match.compatibilityScore).toInt()

    val scoreColor = when {
        score >= 85 -> AppTheme.Success
        score >= 70 -> AppTheme.Primary
        else        -> AppTheme.Warning
    }
    val scoreLabel = when {
        score >= 85 -> "Excellent"
        score >= 70 -> "Good"
        else        -> "Fair"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation   = 3.dp,
                shape       = RoundedCornerShape(AppTheme.RadiusXl),
                ambientColor = AppTheme.PrimaryAlpha15,
                spotColor    = AppTheme.PrimaryAlpha8
            )
            .clickable { onClick() },
        shape  = RoundedCornerShape(AppTheme.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ── Top row: Avatar + Info + Score + Fav ──
            Row(verticalAlignment = Alignment.Top) {

                // Avatar
                Box(modifier = Modifier.size(62.dp)) {
                    AsyncImage(
                        model = "https://ui-avatars.com/api/?name=${user.fullName}&background=2563EB&color=fff&size=200",
                        contentDescription = user.fullName,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(AppTheme.RadiusMd))
                            .border(2.dp, AppTheme.Border, RoundedCornerShape(AppTheme.RadiusMd)),
                        contentScale = ContentScale.Crop
                    )
                    // Online dot
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 1.dp, y = 1.dp)
                            .background(AppTheme.Success, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }

                Spacer(Modifier.width(14.dp))

                // Name + location + score badge
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user.fullName ?: "Verified User",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, null, tint = AppTheme.Primary, modifier = Modifier.size(11.dp))
                        }
                        Spacer(Modifier.width(6.dp))
                        Text(
                            user.address ?: "Chennai, India",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextSecondary
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Score badge
                    Surface(
                        color = scoreColor.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, scoreColor.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = scoreColor, modifier = Modifier.size(11.dp))
                            Spacer(Modifier.width(5.dp))
                            Text(
                                "$score% · $scoreLabel",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = scoreColor
                            )
                        }
                    }

                    if (match.requestStatus != null) {
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFFFFF7ED),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFEDD5))
                        ) {
                            Text(
                                "Roomshare ${match.requestStatus}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFD97706),
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Favorite
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AppTheme.PrimaryAlpha8, RoundedCornerShape(10.dp))
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FavoriteBorder, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(17.dp))
                }
            }

            // ── Compatibility Bar ──
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(AppTheme.Divider)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(score / 100f)
                            .background(
                                Brush.horizontalGradient(listOf(scoreColor.copy(alpha = 0.7f), scoreColor)),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
                Text(
                    "$score%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = scoreColor
                )
            }

            // ── AI Insight ──
            if (match.reason != null) {
                Spacer(Modifier.height(14.dp))
                Surface(
                    color = AppTheme.PrimaryAlpha8,
                    shape = RoundedCornerShape(AppTheme.RadiusMd),
                    border = BorderStroke(1.dp, AppTheme.PrimaryAlpha15),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Brush.linearGradient(listOf(AppTheme.Primary, AppTheme.Violet)),
                                    RoundedCornerShape(7.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                "AI INSIGHT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.Primary,
                                letterSpacing = 0.7.sp
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                match.reason,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary,
                                lineHeight = 17.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = AppTheme.Divider)

            // ── Action Buttons ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Message button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(AppTheme.RadiusMd))
                        .clickable { onMessageClick() },
                    shape  = RoundedCornerShape(AppTheme.RadiusMd),
                    color  = Color.White,
                    border = BorderStroke(1.dp, AppTheme.Border)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Message, null, tint = AppTheme.Primary, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("MESSAGE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary)
                    }
                }

                // View Profile button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(AppTheme.RadiusMd))
                        .clickable { onClick() },
                    shape = RoundedCornerShape(AppTheme.RadiusMd),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppTheme.PremiumGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("VIEW PROFILE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(13.dp))
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  SKELETON CARD
// ─────────────────────────────────────────
@Composable
private fun MatchSkeletonCard() {
    val alpha by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0.4f,
        targetValue  = 0.85f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(AppTheme.RadiusXl),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(AppTheme.RadiusMd))
                )
                Spacer(Modifier.width(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.width(130.dp).height(15.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp)))
                    Box(Modifier.width(90.dp).height(11.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(6.dp)))
                    Box(Modifier.width(70.dp).height(20.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(7.dp)))
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth().height(5.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(3.dp)))
            Spacer(Modifier.height(14.dp))
            Box(Modifier.fillMaxWidth().height(56.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(AppTheme.RadiusMd)))
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.weight(1f).height(42.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(AppTheme.RadiusMd)))
                Box(Modifier.weight(1f).height(42.dp).background(AppTheme.Divider.copy(alpha = alpha), RoundedCornerShape(AppTheme.RadiusMd)))
            }
        }
    }
}

// ─────────────────────────────────────────
//  EMPTY STATE
// ─────────────────────────────────────────
@Composable
private fun MatchesEmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    showClear: Boolean,
    onClear: () -> Unit
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
            Icon(icon, null, tint = AppTheme.PrimaryLight, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, fontSize = 14.sp, color = AppTheme.TextSecondary, lineHeight = 20.sp)
        if (showClear) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onClear,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                shape  = RoundedCornerShape(AppTheme.RadiusMd),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("Clear Search", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ─────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchesScreenPreview() {
    MatchesScreen(email = "test@example.com")
}