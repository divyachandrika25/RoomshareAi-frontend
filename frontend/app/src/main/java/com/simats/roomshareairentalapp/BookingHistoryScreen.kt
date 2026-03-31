package com.simats.roomshareairentalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────
//  BOOKING HISTORY SCREEN
// ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    bookings: List<BookingHistoryItem> = emptyList(),
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onBookingClick: (BookingHistoryItem) -> Unit = {}
) {
    Scaffold(
        containerColor = AppTheme.Slate50,
        topBar = {
            BookingHistoryTopBar(onBackClick = onBackClick)
        }
    ) { padding ->
        when {
            isLoading -> BookingHistoryLoadingState(modifier = Modifier.padding(padding))

            bookings.isEmpty() -> BookingHistoryEmptyState(
                modifier  = Modifier.padding(padding),
                onExplore = onBackClick
            )

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Hero ──
                BookingHistoryHero(count = bookings.size)

                // ── Cards ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-20).dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Summary stats
                    BookingSummaryRow(bookings = bookings)

                    // Transaction label
                    Text(
                        "${bookings.size} TRANSACTIONS FOUND",
                        fontSize     = 10.sp,
                        fontWeight   = FontWeight.Black,
                        color        = AppTheme.Slate400,
                        letterSpacing = 1.2.sp,
                        modifier     = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )

                    // Booking cards
                    bookings.forEach { booking ->
                        BookingHistoryCard(booking = booking, onClick = { onBookingClick(booking) })
                    }

                    // Escrow protection notice
                    EscrowNotice()

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  TOP BAR
// ─────────────────────────────────────────
@Composable
private fun BookingHistoryTopBar(onBackClick: () -> Unit) {
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
            Text("Booking History", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900)
            Spacer(Modifier.weight(1f))
            // Export button
            IconButton(onClick = { /* Export PDF */ }) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(AppTheme.Blue50, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ReceiptLong, null, tint = AppTheme.Blue600, modifier = Modifier.size(17.dp))
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
private fun BookingHistoryHero(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.History, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        "Booking History",
                        fontSize = 22.sp, fontWeight = FontWeight.Black,
                        color = Color.White, letterSpacing = (-0.3).sp
                    )
                    Text(
                        "$count transactions recorded",
                        fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f), fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  SUMMARY STATS ROW
// ─────────────────────────────────────────
@Composable
private fun BookingSummaryRow(bookings: List<BookingHistoryItem>) {
    val completed = bookings.count { it.status.uppercase() in listOf("COMPLETED", "PAID") }
    val pending   = bookings.count { it.status.uppercase() in listOf("PENDING", "PROCESSING") }
    val hotels    = bookings.count { it.isHotel }

    Surface(
        modifier  = Modifier.fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.06f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.08f)),
        shape     = RoundedCornerShape(18.dp),
        color     = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryTile(value = "$completed", label = "Completed", color = AppTheme.Success)
            VerticalDivider(modifier = Modifier.height(40.dp), color = AppTheme.Slate100)
            SummaryTile(value = "$pending",   label = "Pending",   color = AppTheme.Gold)
            VerticalDivider(modifier = Modifier.height(40.dp), color = AppTheme.Slate100)
            SummaryTile(value = "$hotels",    label = "Hotels",    color = AppTheme.Blue600)
        }
    }
}

@Composable
private fun SummaryTile(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.Slate400)
    }
}

// ─────────────────────────────────────────
//  BOOKING HISTORY CARD
// ─────────────────────────────────────────
@Composable
fun BookingHistoryCard(booking: BookingHistoryItem, onClick: () -> Unit = {}) {
    val statusColor = when (booking.status.uppercase()) {
        "COMPLETED", "PAID"      -> AppTheme.Success
        "PENDING", "PROCESSING"  -> AppTheme.Gold
        "CANCELLED"              -> AppTheme.Rose
        else                     -> AppTheme.Blue600
    }
    val statusLabel = booking.status.replaceFirstChar { it.uppercase() }
    val typeIcon    = if (booking.isHotel) Icons.Default.Business else Icons.Default.HomeWork
    val typeBg      = if (booking.isHotel) AppTheme.Blue50 else AppTheme.Indigo500.copy(alpha = 0.08f)
    val typeColor   = if (booking.isHotel) AppTheme.Blue600 else AppTheme.Indigo500
    val typeLabel   = if (booking.isHotel) "HOTEL STAY" else "ROOM SHARE"

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.07f),
                spotColor   = AppTheme.Blue700.copy(alpha = 0.09f))
            .clickable { onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            // ── Main row ──
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon box
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(typeBg, RoundedCornerShape(14.dp))
                        .border(1.dp,
                            if (booking.isHotel) AppTheme.Blue100 else AppTheme.Indigo500.copy(alpha = 0.15f),
                            RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(typeIcon, null, tint = typeColor, modifier = Modifier.size(22.dp))
                }

                Spacer(Modifier.width(14.dp))

                // Text block
                Column(modifier = Modifier.weight(1f)) {
                    // Type chip
                    Surface(
                        color  = typeBg,
                        shape  = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, typeColor.copy(alpha = 0.2f))
                    ) {
                        Text(
                            typeLabel,
                            fontSize = 8.sp, fontWeight = FontWeight.Black, color = typeColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(
                        booking.roomTitle,
                        fontSize   = 14.sp, fontWeight = FontWeight.ExtraBold,
                        color      = AppTheme.Slate900, maxLines = 1,
                        overflow   = TextOverflow.Ellipsis, letterSpacing = (-0.2).sp
                    )
                    Spacer(Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(AppTheme.Blue50, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, null, tint = AppTheme.Blue600, modifier = Modifier.size(9.dp))
                        }
                        Spacer(Modifier.width(5.dp))
                        Text(
                            booking.location ?: "Location Unknown",
                            fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AppTheme.Slate600
                        )
                    }
                }

                // Right: status + amount
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color  = statusColor.copy(alpha = 0.10f),
                        shape  = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.2f))
                    ) {
                        Text(
                            statusLabel,
                            fontSize = 9.sp, fontWeight = FontWeight.Black, color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            letterSpacing = 0.4.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        booking.amount,
                        fontSize = 16.sp, fontWeight = FontWeight.Black,
                        color = AppTheme.Slate900, letterSpacing = (-0.5).sp
                    )
                }
            }

            // ── Footer ──
            HorizontalDivider(color = AppTheme.Slate100, modifier = Modifier.padding(horizontal = 18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(AppTheme.Slate100, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Schedule, null, tint = AppTheme.Slate400, modifier = Modifier.size(12.dp))
                    }
                    Spacer(Modifier.width(7.dp))
                    Text(booking.createdAt, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.Slate400)
                }

                // View details CTA
                Surface(
                    color  = AppTheme.Blue600,
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "View Details",
                            fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
                            tint = Color.White, modifier = Modifier.size(11.dp))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  ESCROW NOTICE
// ─────────────────────────────────────────
@Composable
private fun EscrowNotice() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp),
                ambientColor = AppTheme.Blue900.copy(alpha = 0.05f),
                spotColor   = AppTheme.Success.copy(alpha = 0.07f)),
        shape  = RoundedCornerShape(18.dp),
        color  = Color.White,
        border = BorderStroke(1.dp, AppTheme.Success.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(AppTheme.Success.copy(alpha = 0.10f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VerifiedUser, null, tint = AppTheme.Success, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Escrow Protected",
                    fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.Slate900
                )
                Text(
                    "All transactions secured via bank-grade encryption.",
                    fontSize = 11.sp, fontWeight = FontWeight.Medium,
                    color = AppTheme.Slate400, lineHeight = 15.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(AppTheme.Success.copy(alpha = 0.10f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = AppTheme.Success, modifier = Modifier.size(14.dp))
            }
        }
    }
}

// ─────────────────────────────────────────
//  EMPTY STATE
// ─────────────────────────────────────────
@Composable
private fun BookingHistoryEmptyState(modifier: Modifier = Modifier, onExplore: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(AppTheme.Blue50, RoundedCornerShape(26.dp))
                .border(1.dp, AppTheme.Blue100, RoundedCornerShape(26.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.EventNote, null, tint = AppTheme.Blue400, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "No Bookings Yet",
            fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
            color = AppTheme.Slate900, letterSpacing = (-0.3).sp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Your transaction history will appear here once you start exploring premium stays.",
            fontSize   = 14.sp, color = AppTheme.Slate400,
            textAlign  = TextAlign.Center, lineHeight = 21.sp
        )
        Spacer(Modifier.height(28.dp))
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { onExplore() },
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(listOf(AppTheme.Blue700, AppTheme.Blue600, AppTheme.Indigo500))
                    )
                    .padding(horizontal = 32.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Start Exploring", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}

// ─────────────────────────────────────────
//  LOADING STATE
// ─────────────────────────────────────────
@Composable
private fun BookingHistoryLoadingState(modifier: Modifier = Modifier) {
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
        modifier = modifier.fillMaxSize()
    ) {
        // Hero shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(AppTheme.Slate100.copy(alpha = alpha))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.fillMaxWidth().height(72.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(18.dp)))
            repeat(3) {
                Box(Modifier.fillMaxWidth().height(110.dp).background(AppTheme.Slate100.copy(alpha = alpha), RoundedCornerShape(20.dp)))
            }
        }
    }
}

// ─────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingHistoryScreenPreview() {
    BookingHistoryScreen(
        bookings = listOf(
            BookingHistoryItem("1", "Premium Loft Koramangala", "Bengaluru, KA", "Oct 12, 2024", "₹28,500", "COMPLETED", isHotel = false),
            BookingHistoryItem("2", "The Leela Palace",        "Chennai, TN",   "Nov 01, 2024", "₹8,200",  "PENDING",   isHotel = true),
            BookingHistoryItem("3", "Velachery Shared Flat",   "Chennai, TN",   "Sep 18, 2024", "₹12,000", "CANCELLED", isHotel = false),
        )
    )
}