package com.simats.roomshareairentalapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableRoomDetailScreen(
    room: Room? = null,
    homeRoomDetail: HomeRoomDetailData? = null,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onChatWithRoommate: (String) -> Unit = {},
    onRequestShare: () -> Unit = {},
    onSaveToggle: (String) -> Unit = {}
) {
    val defaultImages = emptyList<String>()

    val images = remember(room, homeRoomDetail) {
        if (homeRoomDetail != null) {
            homeRoomDetail.photos?.map { it.image } ?: defaultImages
        } else if (room != null) {
            listOf(room.imageUrl) + defaultImages
        } else {
            defaultImages
        }
    }
    val pagerState = rememberPagerState(pageCount = { images.size })
    var isFullScreenOpen by remember { mutableStateOf(false) }

    if (isFullScreenOpen) {
        FullScreenImageViewer(
            images = images,
            initialPage = pagerState.currentPage,
            onDismiss = { isFullScreenOpen = false }
        )
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Room Details",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            RoomDetailBottomBar(
                onRequestClick = onRequestShare,
                isAvailable = homeRoomDetail?.status == "AVAILABLE" || (room?.isAvailable ?: true)
            )
        },
        containerColor = AppTheme.Background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Image Pager
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize().clickable { isFullScreenOpen = true }
                    ) { page ->
                        AsyncImage(
                            model = images[page],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Indicators
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(images.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                    
                    // Match Badge
                    val matchPerc = homeRoomDetail?.matchPercentage ?: room?.matchPercentage ?: 0
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                    ) {
                        Text(
                            "$matchPerc% MATCH",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = AppTheme.Primary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = homeRoomDetail?.apartmentTitle ?: room?.title ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        IconButton(onClick = { 
                            val id = homeRoomDetail?.id ?: room?.id ?: ""
                            if (id.isNotEmpty()) onSaveToggle(id)
                        }) {
                            val saved = homeRoomDetail?.isFavorite ?: room?.isSaved ?: false
                            Icon(
                                imageVector = if (saved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (saved) Color.Red else AppTheme.TextTertiary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = AppTheme.Primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        val loc = if (homeRoomDetail != null) "${homeRoomDetail.city}, ${homeRoomDetail.address}" else (room?.location ?: "")
                        Text(loc, color = AppTheme.TextSecondary, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = homeRoomDetail?.monthlyRent ?: room?.price ?: "",
                        color = AppTheme.Primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AppTheme.PrimaryAlpha12,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, null, tint = AppTheme.Primary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("VERIFIED LISTING", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        RoomAttrCard(Icons.Default.Bathtub, "Bath", homeRoomDetail?.bathroomType ?: room?.bathType ?: "Shared", Modifier.weight(1f))
                        RoomAttrCard(Icons.Default.Group, "Roommates", "${homeRoomDetail?.roommateCount ?: room?.roommateCount ?: 1}", Modifier.weight(1f))
                        RoomAttrCard(Icons.Default.Security, "Entry", homeRoomDetail?.entryType ?: "Private", Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Move-in Budget Section
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Payments, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Projected Move-in Budget", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            val rentStr = homeRoomDetail?.monthlyRent ?: room?.price ?: "0"
                            val rent = rentStr.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
                            
                            BudgetRow("First Month Rent", "₹$rent")
                            BudgetRow("Security Deposit (1.5x)", "₹${(rent * 1.5).toInt()}")
                            BudgetRow("Utility Advance", "₹2,500")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = AppTheme.Divider)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Estimated", fontWeight = FontWeight.Black, fontSize = 15.sp, color = AppTheme.TextPrimary)
                                Text("₹${rent + (rent * 1.5).toInt() + 2500}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = AppTheme.Primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    StandardSectionHeader("About the Room")
                    MarkdownText(
                        text = homeRoomDetail?.description ?: (room?.title ?: ""),
                        color = AppTheme.TextSecondary,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (homeRoomDetail?.potentialRoommates?.isNotEmpty() == true) {
                        StandardSectionHeader("Potential Roommates")
                        homeRoomDetail.potentialRoommates.forEach { roommate ->
                            DetailPotentialRoommateItem(roommate) { onChatWithRoommate(roommate.email) }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun DetailPotentialRoommateItem(roommate: PotentialRoommateInfo, onChatClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = roommate.photo,
                contentDescription = null,
                modifier = Modifier.size(50.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(roommate.fullName ?: "", fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
                MarkdownText("Match: ${roommate.matchPercentage}%", fontSize = 12.sp, color = AppTheme.Primary, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onChatClick) {
                Icon(Icons.Default.ChatBubbleOutline, null, tint = AppTheme.Primary)
            }
        }
    }
}

@Composable
fun RoomAttrCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = AppTheme.Primary, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 10.sp, color = AppTheme.TextTertiary, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
        }
    }
}

@Composable
fun DetailPotentialRoommateCard(roommate: Roommate, onChatClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = roommate.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(roommate.name, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
                Text("Match: ${roommate.matchPercentage}%", fontSize = 12.sp, color = AppTheme.Primary, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onChatClick) {
                Icon(Icons.Default.ChatBubbleOutline, null, tint = AppTheme.Primary)
            }
        }
    }
}

@Composable
fun RoomDetailBottomBar(onRequestClick: () -> Unit, isAvailable: Boolean) {
    Surface(color = AppTheme.Surface, shadowElevation = 8.dp) {
        Button(
            onClick = onRequestClick,
            enabled = isAvailable,
            modifier = Modifier.fillMaxWidth().padding(20.dp).height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary)
        ) {
            Text(if (isAvailable) "Request to Share" else "Booked", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
