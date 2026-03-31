package com.simats.roomshareairentalapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    room: Room,
    onBackClick: () -> Unit = {},
    onBookNowClick: () -> Unit = {},
    onRoommateClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Property Details",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, null, tint = AppTheme.TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0F172A),
                shadowElevation = 24.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "MONTHLY RENT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                room.price,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                "/mo",
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                    Button(
                        onClick = onBookNowClick,
                        modifier = Modifier
                            .height(54.dp)
                            .width(180.dp),
                        shape = RoundedCornerShape(AppTheme.RadiusMd),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            "INITIATE BOOKING",
                            color = Color(0xFF0F172A),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        },
        containerColor = AppTheme.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Showcase
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = room.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(AppTheme.RadiusMd),
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Verified, null, tint = AppTheme.Success, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "VERIFIED LISTING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = AppTheme.Success,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                // Header
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = AppTheme.PrimaryAlpha8,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, AppTheme.PrimaryAlpha12)
                        ) {
                            Text(
                                "PREMIUM SHARED ROOM",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.Primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFACC15), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("4.7 Compatibility Score", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        room.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = AppTheme.TextPrimary,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = AppTheme.PrimaryAlpha20, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(room.location, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextSecondary)
                    }
                }

                // Specs Grid
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val specs = listOf(
                        Triple(Icons.Default.Payments, room.price, "MONTHLY"),
                        Triple(Icons.Default.Groups, "2 Total", "CAPACITY"),
                        Triple(Icons.Default.Bathtub, "Shared", "AMENITIES"),
                        Triple(Icons.Default.MeetingRoom, "Regular", "ACCESS")
                    )
                    specs.forEach { (icon, value, label) ->
                        Surface(
                            modifier = Modifier.weight(1f).height(100.dp),
                            color = Color.White,
                            shape = RoundedCornerShape(AppTheme.RadiusLg),
                            border = BorderStroke(1.dp, AppTheme.Divider)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(36.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(icon, null, tint = AppTheme.Primary, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary)
                                Text(label, fontSize = 8.sp, fontWeight = FontWeight.Black, color = AppTheme.TextTertiary, letterSpacing = 0.5.sp)
                            }
                        }
                    }
                }

                // Description
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionDividerComp(icon = Icons.Default.Info, title = "PROPERTY OVERVIEW", subtitle = "Living experience & highlights")
                    Text(
                        text = "This premium room in ${room.location} offers a perfect blend of modern comfort and seamless utility integration. Meticulously maintained and strategically located for high connectivity. Ideal for professionals seeking a refined shared living ecosystem with zero-compromise security benchmarks.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 24.sp
                    )
                }

                // Amenities
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionDividerComp(icon = Icons.Default.Inventory, title = "STANDARD AMENITIES", subtitle = "Inclusions & shared utilities")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        room.amenities.forEach { amenity ->
                            Surface(
                                shape = RoundedCornerShape(AppTheme.RadiusMd),
                                color = Color.White,
                                border = BorderStroke(1.dp, AppTheme.Divider)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when(amenity.lowercase()) {
                                            "wifi" -> Icons.Default.Wifi
                                            "laundry" -> Icons.Default.LocalLaundryService
                                            "kitchen" -> Icons.Default.Restaurant
                                            else -> Icons.Default.Bolt
                                        },
                                        null,
                                        modifier = Modifier.size(14.dp),
                                        tint = AppTheme.Primary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        amenity.uppercase(), 
                                        fontSize = 10.sp, 
                                        fontWeight = FontWeight.Black, 
                                        color = AppTheme.TextSecondary, 
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // AI Roommate Insights (Premium addition)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppTheme.RadiusLg))
                        .background(AppTheme.HeaderGradient)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("AI ANALYTICS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Based on your profile, this property has a 94% Compatibility Rating. The current residents share similar professional routines and social preferences.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Host Section
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionDividerComp(icon = Icons.Default.VerifiedUser, title = "VERIFIED HOST", subtitle = "Secure identity & track record")
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shape = RoundedCornerShape(AppTheme.RadiusLg),
                        border = BorderStroke(1.dp, AppTheme.Divider)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = "https://ui-avatars.com/api/?name=Alex+Johnson&background=1E63FF&color=fff&size=100",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, AppTheme.Divider, RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ALEX JOHNSON", fontSize = 14.sp, fontWeight = FontWeight.Black, color = AppTheme.TextPrimary)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFACC15), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Superhost • 48 Reviews", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextTertiary)
                                }
                            }
                            IconButton(onClick = { /* Contact */ }) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.ChatBubble, null, tint = AppTheme.Primary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionDividerComp(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(36.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = AppTheme.Primary, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Black, color = AppTheme.TextPrimary, letterSpacing = 0.5.sp)
            Text(subtitle, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AppTheme.TextTertiary)
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetailScreenPreview() {
    RoomshareAIRentalAppTheme {
        RoomDetailScreen(
            room = Room(
                id = "1",
                title = "Modern Shared Loft in Koramangala",
                location = "80 Feet Rd, Bengaluru, Karnataka",
                price = "₹28,500",
                imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=600",
                amenities = listOf("Wifi", "Kitchen", "Laundry")
            )
        )
    }
}
