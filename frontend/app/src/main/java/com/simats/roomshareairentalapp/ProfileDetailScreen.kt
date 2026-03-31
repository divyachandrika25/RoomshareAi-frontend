package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ─────────────────────────────────────────
//  PROFILE DETAIL SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    currentEmail: String,
    targetEmail: String,
    onBackClick: () -> Unit,
    onAICompatibilityClick: (String, String) -> Unit,
    onSendMessageClick: (String) -> Unit,
    onSaveToggle: (String) -> Unit = {}
) {
    var profileData by remember { mutableStateOf<RoommateProfileDetail?>(null) }
    var isLoading   by remember { mutableStateOf(true) }
    var isSaved     by remember { mutableStateOf(false) }

    LaunchedEffect(currentEmail, targetEmail) {
        try {
            val response = RetrofitClient.instance.getRoommateProfileDetail(currentEmail, targetEmail)
            if (response.isSuccessful) {
                profileData = response.body()?.data
                isSaved     = response.body()?.data?.isFavorite ?: false
            }
        } catch (_: Exception) { } finally { isLoading = false }
    }

    Scaffold(
        containerColor = AppTheme.Slate50,
        topBar = {
            DetailTopBar(
                isSaved     = isSaved,
                onBackClick = onBackClick,
                onShare     = { /* share */ },
                onFavorite  = { isSaved = !isSaved; onSaveToggle(targetEmail) }
            )
        }
    ) { padding ->
        when {
            isLoading -> DetailLoadingState(modifier = Modifier.padding(padding))

            profileData != null -> {
                val data = profileData!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // ── Hero ──
                    DetailHero(data = data)

                    // ── Content ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset(y = (-24).dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Action buttons
                        ActionButtons(
                            onMessageClick = { onSendMessageClick(targetEmail) },
                            onAIClick      = { onAICompatibilityClick(currentEmail, targetEmail) }
                        )

                        // Identity Narrative
                        DetailSection(
                            icon   = Icons.Default.Person,
                            iconBg = AppTheme.Blue600,
                            title  = "Identity Narrative",
                            subtitle = "About this person"
                        ) {
                            MarkdownText(
                                text     = data.aboutMe ?: "No description provided.",
                                fontSize = 14.sp,
                                color    = AppTheme.Slate700,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Core Parameters
                        DetailSection(
                            icon   = Icons.Default.Work,
                            iconBg = AppTheme.Indigo500,
                            title  = "Core Parameters",
                            subtitle = "Lifestyle & availability details"
                        ) {
                            CoreParametersGrid(data = data)
                        }

                        // Harmony Metrics
                        DetailSection(
                            icon   = Icons.AutoMirrored.Filled.TrendingUp,
                            iconBg = AppTheme.Blue500,
                            title  = "Harmony Metrics",
                            subtitle = "Compatibility breakdown"
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                MetricBar("Schedule Alignment", 85)
                                MetricBar("Hygiene Symmetry",   92)
                                MetricBar("Social Resonance",   78)
                            }
                        }

                        // Interlock Interests
                        InterlockInterests(data = data)
                    }

                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun DetailTopBar(
    isSaved: Boolean,
    onBackClick: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit
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
                        .background(AppTheme.Blue50, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = AppTheme.Blue700, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Text("Identity Protocol", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
            Spacer(Modifier.weight(1f))
            Row {
                IconButton(onClick = onShare) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(AppTheme.Slate100, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Share, null, tint = AppTheme.Slate600, modifier = Modifier.size(17.dp))
                    }
                }
                IconButton(onClick = onFavorite) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isSaved) Color(0xFFFFF1F2) else AppTheme.Slate100,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            null,
                            tint = if (isSaved) Color(0xFFE11D48) else AppTheme.Slate600,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = AppTheme.Slate100)
    }
}

// ─────────────────────────────────────────
//  HERO BANNER
// ─────────────────────────────────────────
@Composable
private fun DetailHero(data: RoommateProfileDetail) {
    Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {

        // Gradient background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(
                    Brush.linearGradient(listOf(AppTheme.Blue800, AppTheme.Blue600, AppTheme.Indigo500))
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .offset(x = (-55).dp, y = (-55).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 45.dp, y = (-35).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )

            // Match percentage badge
            data.matchPercentage?.let { score ->
                val scoreInt = score.toInt()
                val scoreColor = when {
                    scoreInt >= 85 -> AppTheme.Success
                    scoreInt >= 70 -> AppTheme.Blue400
                    else           -> Color(0xFFFBBF24)
                }
                Surface(
                    color  = Color.White.copy(alpha = 0.15f),
                    shape  = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("$scoreInt%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("MATCH", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White.copy(alpha = 0.75f), letterSpacing = 0.5.sp)
                    }
                }
            }
        }

        // Avatar overlapping
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box {
                AsyncImage(
                    model = data.photo
                        ?: "https://ui-avatars.com/api/?name=${data.fullName ?: "User"}&background=2563EB&color=fff&size=200",
                    contentDescription = data.fullName,
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .border(3.dp, Color.White, RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
                // Verified dot
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).dp, y = (-2).dp)
                        .background(AppTheme.Success, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(11.dp))
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    data.fullName ?: "Unknown",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.Slate900,
                    letterSpacing = (-0.3).sp
                )

                if (data.requestStatus != null) {
                    Surface(
                        color = Color(0xFFFFF7ED),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFEDD5))
                    ) {
                        Text(
                            "Roomshare ${data.requestStatus}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFD97706),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Surface(
                    color = AppTheme.Success.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "VERIFIED",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.Success,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        letterSpacing = 0.5.sp
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
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
                    data.targetArea ?: "Location not set",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.Slate600
                )
            }
        }
    }
}

// ─────────────────────────────────────────
//  ACTION BUTTONS
// ─────────────────────────────────────────
@Composable
private fun ActionButtons(
    onMessageClick: () -> Unit,
    onAIClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Message — gradient fill
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onMessageClick() },
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(listOf(AppTheme.Blue700, AppTheme.Blue600))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Chat, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Request Roomshare", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
                }
            }
        }

        // AI Report — white with blue border
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp),
                    ambientColor = AppTheme.Blue900.copy(alpha = 0.05f),
                    spotColor = AppTheme.Blue600.copy(alpha = 0.08f))
                .clip(RoundedCornerShape(16.dp))
                .clickable { onAIClick() },
            shape  = RoundedCornerShape(16.dp),
            color  = Color.White,
            border = BorderStroke(1.5.dp, AppTheme.Blue200)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            Brush.linearGradient(listOf(AppTheme.Blue600, AppTheme.Indigo500)),
                            RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text("AI Report", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = AppTheme.Slate900)
            }
        }
    }
}

// ─────────────────────────────────────────
//  DETAIL SECTION CARD
// ─────────────────────────────────────────
@Composable
private fun DetailSection(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
                    Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AppTheme.Slate400)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = AppTheme.Slate100)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                content()
            }
        }
    }
}

// ─────────────────────────────────────────
//  CORE PARAMETERS GRID
// ─────────────────────────────────────────
@Composable
private fun CoreParametersGrid(data: RoommateProfileDetail) {
    val params = listOf(
        Triple(Icons.Default.Work,          "OCCUPATION", data.occupation  ?: "N/A"),
        Triple(Icons.Default.Payments,      "BUDGET",     data.budgetRange ?: "N/A"),
        Triple(Icons.Default.CalendarToday, "MOVE-IN",    data.moveInDate  ?: "Flexible")
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        params.forEach { (icon, label, value) ->
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(88.dp),
                color  = AppTheme.Blue50,
                shape  = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, AppTheme.Blue100)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.White, RoundedCornerShape(9.dp))
                            .border(1.dp, AppTheme.Blue100, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, null, tint = AppTheme.Blue600, modifier = Modifier.size(15.dp))
                    }
                    Spacer(Modifier.height(7.dp))
                    Text(
                        label,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.Slate400,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        value,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.Slate900,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  METRIC BAR
// ─────────────────────────────────────────
@Composable
fun MetricBar(label: String, score: Int) {
    val barColor = when {
        score >= 85 -> AppTheme.Success
        score >= 70 -> AppTheme.Blue600
        else        -> Color(0xFFF59E0B)
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.Slate900)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    color = barColor.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "$score%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = barColor,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(AppTheme.Slate100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(listOf(barColor.copy(alpha = 0.7f), barColor)),
                        RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

// ─────────────────────────────────────────
//  INTERLOCK INTERESTS
// ─────────────────────────────────────────
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InterlockInterests(data: RoommateProfileDetail) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(AppTheme.Blue900, AppTheme.Blue800))
                )
        ) {
            // Decorative circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
                    .background(Color.White.copy(alpha = 0.04f), CircleShape)
            )
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = AppTheme.Blue200, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Interlock Interests", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Shared lifestyle signals", fontSize = 11.sp, color = Color.White.copy(alpha = 0.55f))
                    }
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    val tags = listOf("Cooking", "Travel", "Tech", "Fitness", "Music", "Reading", "Yoga")
                    tags.forEach { tag ->
                        Surface(
                            color  = Color.White.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
                            shape  = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    tag,
                                    fontSize   = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  DETAIL SECTION HEADER  (kept for back-compat)
// ─────────────────────────────────────────
@Composable
fun DetailSectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(AppTheme.Blue50, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = AppTheme.Blue600, modifier = Modifier.size(15.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900, letterSpacing = 0.5.sp)
    }
}

// ─────────────────────────────────────────
//  METRIC BAR COMP  (kept for back-compat)
// ─────────────────────────────────────────
@Composable
fun MetricBarComp(label: String, score: Int) = MetricBar(label, score)

// ─────────────────────────────────────────
//  HABIT CARD
// ─────────────────────────────────────────
@Composable
fun HabitCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(88.dp),
        shape  = RoundedCornerShape(16.dp),
        color  = AppTheme.Blue50,
        border = BorderStroke(1.dp, AppTheme.Blue100)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White, RoundedCornerShape(9.dp))
                    .border(1.dp, AppTheme.Blue100, RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, modifier = Modifier.size(16.dp), tint = AppTheme.Blue600)
            }
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 9.sp, color = AppTheme.Slate400, fontWeight = FontWeight.Black, letterSpacing = 0.3.sp)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
        }
    }
}

// ─────────────────────────────────────────
//  LOADING STATE
// ─────────────────────────────────────────
@Composable
private fun DetailLoadingState(modifier: Modifier = Modifier) {
    val alpha by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0.4f,
        targetValue  = 0.85f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Banner skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(AppTheme.Slate100.copy(alpha = alpha))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(Modifier.size(90.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(24.dp)))
            Box(Modifier.width(160.dp).height(20.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(6.dp)))
            Box(Modifier.width(110.dp).height(14.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(6.dp)))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.weight(1f).height(52.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(16.dp)))
                Box(Modifier.weight(1f).height(52.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(16.dp)))
            }
            Box(Modifier.fillMaxWidth().height(120.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(20.dp)))
            Box(Modifier.fillMaxWidth().height(100.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(20.dp)))
        }
    }
}