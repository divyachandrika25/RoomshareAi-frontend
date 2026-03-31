package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalReviewScreen(
    finalReviewData: RoomShareFinalReviewData? = null,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onSendRequestClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Text(
                            "Available Rooms", 
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1E3C)
                        ) 
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5FE))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF0D1E3C)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                // Progress Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF1E63FF))
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (finalReviewData != null) {
                Button(
                    onClick = onSendRequestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E63FF)
                    )
                ) {
                    Text(
                        finalReviewData.buttonText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1E63FF))
            }
        } else if (finalReviewData != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = finalReviewData.title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D1E3C)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = finalReviewData.subtitle,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Review Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.LightGray
                            ) {
                                AsyncImage(
                                    model = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=400",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    finalReviewData.roomTitle,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0D1E3C)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = finalReviewData.ownerPhoto ?: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=100",
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "REQUESTING FROM ${finalReviewData.ownerName.uppercase()}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color(0xFFEEF2F6))
                        Spacer(modifier = Modifier.height(24.dp))

                        ReviewDetailItem("Preferred Move-in", finalReviewData.preferredMoveInDate)
                        ReviewDetailItem("Stay Duration", finalReviewData.durationOfStay)
                        ReviewDetailItem("Employment", finalReviewData.employmentStatus)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEF2F6))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Your Share (Monthly)", color = Color.Gray, fontSize = 16.sp)
                            Text("$${"%,.2f".format(finalReviewData.yourShareMonthly)}", fontWeight = FontWeight.Bold, color = Color(0xFF0D1E3C), fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Group Security Deposit", color = Color.Gray, fontSize = 16.sp)
                            Text("$${"%,.2f".format(finalReviewData.groupSecurityDeposit)}", fontWeight = FontWeight.Bold, color = Color(0xFF0D1E3C), fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color(0xFFEEF2F6))
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Move-in", fontWeight = FontWeight.Bold, color = Color(0xFF0D1E3C), fontSize = 20.sp)
                            Text("$${"%,.2f".format(finalReviewData.totalMoveIn)}", fontWeight = FontWeight.Bold, color = Color(0xFF1E63FF), fontSize = 20.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ReviewDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.SemiBold, color = Color(0xFF0D1E3C), fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun FinalReviewScreenPreview() {
    RoomshareAIRentalAppTheme {
        FinalReviewScreen()
    }
}
