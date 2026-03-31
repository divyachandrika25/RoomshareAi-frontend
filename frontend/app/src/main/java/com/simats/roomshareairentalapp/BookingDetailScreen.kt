package com.simats.roomshareairentalapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    booking: BookingHistoryItem?,
    onBackClick: () -> Unit
) {
    if (booking == null) {
        onBackClick()
        return
    }

    val statusColor = when (booking.status.uppercase()) {
        "COMPLETED", "PAID" -> Color(0xFF10B981)
        "PENDING", "PROCESSING" -> Color(0xFFF59E0B)
        "CANCELLED" -> Color(0xFFF43F5E)
        else -> Color(0xFF1E63FF)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Transaction Details",
                onBackClick = onBackClick
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Icon & Badge
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (booking.status.uppercase() == "CANCELLED") Icons.Default.Cancel else Icons.Default.CheckCircle,
                        null,
                        tint = statusColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                color = statusColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    booking.status.uppercase(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        if (booking.isHotel) "Hotel Booking" else "Room Share Agreement",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF6366F1),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        booking.roomTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
                        lineHeight = 28.sp
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(20.dp))

                    DetailRow(Icons.Default.LocationOn, "Location", booking.location ?: "N/A")
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailRow(Icons.Default.CalendarToday, "Booking Date", booking.createdAt)
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailRow(Icons.Default.Receipt, "Transaction ID", "#${booking.id.take(8).uppercase()}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Payment Summary", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Paid", fontSize = 15.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                        Text(booking.amount, fontSize = 20.sp, color = Color(0xFF1E63FF), fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Payment Method", fontSize = 14.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                        Text("Verified Credit Card", fontSize = 14.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("DONE", fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = Color(0xFFF1F5F9),
            shape = CircleShape,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
        }
    }
}
