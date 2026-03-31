package com.simats.roomshareairentalapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun SavedScreen(
    onBackClick: () -> Unit,
    savedRoommates: List<Roommate> = emptyList(),
    savedRooms: List<Room> = emptyList(),
    favoriteItems: List<FavoriteItem> = emptyList(),
    isLoading: Boolean = false,
    onRoommateClick: (Roommate) -> Unit = {},
    onFavoriteClick: (FavoriteItem) -> Unit = {},
    onRoomClick: (Room) -> Unit = {}
) {
            Scaffold(
                containerColor = AppTheme.Background
            ) { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    
                    // ── Premium Hero Header ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.HeaderGradient)
                    ) {
                        // Decorative circles
                        Box(modifier = Modifier.size(240.dp).offset(x = (-80).dp, y = (-70).dp).background(Color.White.copy(alpha = 0.05f), CircleShape))
                        Box(modifier = Modifier.size(160.dp).align(Alignment.TopEnd).offset(x = 60.dp, y = (-40).dp).background(Color.White.copy(alpha = 0.04f), CircleShape))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(top = 24.dp, bottom = 32.dp)
                        ) {
                            IconButton(onClick = onBackClick, modifier = Modifier.offset(x = (-12).dp)) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(AppTheme.RadiusMd)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                            Text(
                                "Digital Collection",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = (-1).sp
                            )
                            Text(
                                "Your hand-picked elite connections",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = AppTheme.Primary)
                        }
                    } else {
                        if (savedRoommates.isEmpty() && favoriteItems.isEmpty()) {
                            EmptySavedState("No matrix entries found.", "Explore compatible profiles to start your collection.")
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(favoriteItems) { item ->
                                    PremiumFavoriteCard(item = item, onClick = { onFavoriteClick(item) })
                                }
                                items(savedRoommates) { rm ->
                                    DiscoverRoommateCardComponent(
                                        roommate = DiscoverRoommate(
                                            id = rm.id,
                                            email = rm.email,
                                            fullName = rm.name,
                                            age = rm.age.toString(),
                                            roomStatus = rm.status,
                                            photos = listOf(ListedRoomPhotoData("0", rm.imageUrl)),
                                            isFavorite = true
                                        ),
                                        onClick = { onRoommateClick(rm) },
                                        onSaveToggle = { /* Toggled off */ }
                                    )
                                }
                            }
            }
        }
    }
    }
}

@Composable
fun PremiumFavoriteCard(item: FavoriteItem, onClick: () -> Unit) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.photo.takeIf { !it.isNullOrBlank() }
                        ?: "https://ui-avatars.com/api/?name=${item.name ?: "U"}&background=1E63FF&color=fff",
                    contentDescription = null,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(AppTheme.RadiusMd)).border(1.5.dp, AppTheme.Border, RoundedCornerShape(AppTheme.RadiusMd)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name ?: "Elite Member", 
                        fontSize = 17.sp, 
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = item.email, 
                        fontSize = 12.sp, 
                        color = AppTheme.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = AppTheme.PrimaryAlpha8,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "95% COMPATIBILITY", 
                            fontSize = 8.sp, 
                            fontWeight = FontWeight.Black,
                            color = AppTheme.Primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Icon(Icons.Default.Favorite, null, tint = Color(0xFFE11D48), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Divider),
                elevation = null
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("OPEN PROFILE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AppTheme.Primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = AppTheme.Primary, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun EmptySavedState(title: String, subtitle: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Box(
                modifier = Modifier.size(72.dp).background(AppTheme.PrimaryAlpha8, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.FavoriteBorder, null, tint = AppTheme.TextTertiary, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AppTheme.TextPrimary)
            Text(subtitle, fontSize = 14.sp, color = AppTheme.TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedScreenPreview() {
    RoomshareAIRentalAppTheme {
        SavedScreen(onBackClick = {})
    }
}
