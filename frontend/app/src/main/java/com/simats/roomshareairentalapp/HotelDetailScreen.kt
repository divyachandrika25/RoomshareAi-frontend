package com.simats.roomshareairentalapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class HotelBookingDetails(
    val room: HotelRoomItem,
    val checkIn: String,
    val checkOut: String,
    val guests: Int,
    val nights: Int,
    val totalPrice: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelDetail: HotelDetailResponse?,
    isLoading: Boolean,
    isBooking: Boolean = false,
    activeBookingRoomId: String? = null,
    onBackClick: () -> Unit,
    onBookRoom: (HotelBookingDetails) -> Unit
) {
    var showBookingDialog by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf<HotelRoomItem?>(null) }

    if (isLoading || hotelDetail == null) {
        Scaffold(containerColor = Color(0xFFF8FAFC)) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1E63FF))
            }
        }
        return
    }

    val hotel = hotelDetail.hotel
    val rooms = hotelDetail.rooms ?: emptyList()

    // Booking Dialog
    if (showBookingDialog && selectedRoom != null) {
        HotelBookingDialog(
            room = selectedRoom!!,
            hotelName = hotel.name,
            onDismiss = { showBookingDialog = false },
            onConfirm = { details ->
                showBookingDialog = false
                onBookRoom(details)
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            // Header Image
            item {
                Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                    AsyncImage(
                        model = hotel.imageUrl,
                        contentDescription = hotel.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                                .size(40.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF0F172A), modifier = Modifier.size(20.dp))
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        if (hotel.stars != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(hotel.stars.toInt()) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFBBF24), modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(
                            hotel.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(hotel.city, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            // Hotel Info
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Property Overview", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E63FF))
                            Text("Premium Luxury Stay", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        }
                        if (hotel.rating != null) {
                            Surface(
                                color = Color(0xFFF0F9FF),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(hotel.rating.toString(), fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0E7490))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("About", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        MarkdownText(hotel.description ?: "No description available.", fontSize = 15.sp, color = Color(0xFF475569))
                    }

                    if (!hotel.amenities.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Amenities & Services", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            hotel.amenities.take(4).forEach { amenity ->
                                val icon = when {
                                    amenity.contains("wifi", true) -> Icons.Default.Wifi
                                    amenity.contains("pool", true) -> Icons.Default.Bed
                                    amenity.contains("gym", true) -> Icons.Default.Bed
                                    else -> Icons.Default.CheckCircle
                                }
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.weight(1f).aspectRatio(1f),
                                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(Color(0xFFEFF6FF), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(icon, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(16.dp))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            amenity,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF64748B),
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp, 24.dp).background(Color(0xFF1E63FF), RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Available Room Types", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (rooms.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Bed, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No rooms available at the moment.", color = Color(0xFF64748B), fontSize = 15.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                items(rooms) { room ->
                    HotelRoomCard(
                        room = room,
                        isBooking = isBooking && room.id == activeBookingRoomId,
                        onBookClick = { r ->
                            selectedRoom = r
                            showBookingDialog = true
                        }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(60.dp)) }
        }
    }
}

@Composable
fun HotelRoomCard(room: HotelRoomItem, isBooking: Boolean = false, onBookClick: (HotelRoomItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(room.roomType, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bed, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Up to ${room.capacity} Guests", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${room.pricePerNight.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E63FF))
                    Text("/night", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (!room.amenities.isNullOrEmpty()) {
                    room.amenities.take(3).forEach {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(it, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { if (!isBooking && room.available) onBookClick(room) },
                enabled = !isBooking && room.available,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBooking || !room.available) Color(0xFF94A3B8) else Color(0xFF1E63FF),
                    disabledContainerColor = Color(0xFF94A3B8)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                if (isBooking) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Processing Request...", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                } else if (!room.available) {
                    Icon(Icons.Default.Block, null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Booked", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                } else {
                    Text("Reserve Room", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelBookingDialog(
    room: HotelRoomItem,
    hotelName: String,
    onDismiss: () -> Unit,
    onConfirm: (HotelBookingDetails) -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displaySdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val cal = Calendar.getInstance()

    // Default: tomorrow as check-in
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val defaultCheckIn = sdf.format(cal.time)
    val defaultCheckInDisplay = displaySdf.format(cal.time)

    // Default: day after tomorrow as check-out
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val defaultCheckOut = sdf.format(cal.time)
    val defaultCheckOutDisplay = displaySdf.format(cal.time)

    var checkInDate by remember { mutableStateOf(defaultCheckIn) }
    var checkInDisplay by remember { mutableStateOf(defaultCheckInDisplay) }
    var checkOutDate by remember { mutableStateOf(defaultCheckOut) }
    var checkOutDisplay by remember { mutableStateOf(defaultCheckOutDisplay) }
    var guests by remember { mutableIntStateOf(1) }
    var showCheckInPicker by remember { mutableStateOf(false) }
    var showCheckOutPicker by remember { mutableStateOf(false) }

    // Calculate nights
    val nights = remember(checkInDate, checkOutDate) {
        try {
            val inDate = sdf.parse(checkInDate)
            val outDate = sdf.parse(checkOutDate)
            if (inDate != null && outDate != null && outDate.after(inDate)) {
                ((outDate.time - inDate.time) / (1000 * 60 * 60 * 24)).toInt()
            } else 1
        } catch (_: Exception) { 1 }
    }

    val roomTotal = room.pricePerNight * nights
    val taxRate = 0.18
    val taxes = roomTotal * taxRate
    val serviceFee = 199.0
    val totalPrice = roomTotal + taxes + serviceFee

    // Date Pickers
    if (showCheckInPicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showCheckInPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val c = Calendar.getInstance().apply { timeInMillis = millis }
                        checkInDate = sdf.format(c.time)
                        checkInDisplay = displaySdf.format(c.time)
                        // Auto-adjust check-out if it's before check-in
                        c.add(Calendar.DAY_OF_YEAR, 1)
                        val newCheckOut = sdf.format(c.time)
                        try {
                            val currentOut = sdf.parse(checkOutDate)
                            val currentIn = sdf.parse(checkInDate)
                            if (currentOut != null && currentIn != null && !currentOut.after(currentIn)) {
                                checkOutDate = newCheckOut
                                checkOutDisplay = displaySdf.format(c.time)
                            }
                        } catch (_: Exception) {
                            checkOutDate = newCheckOut
                            checkOutDisplay = displaySdf.format(c.time)
                        }
                    }
                    showCheckInPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showCheckInPicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showCheckOutPicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showCheckOutPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val c = Calendar.getInstance().apply { timeInMillis = millis }
                        checkOutDate = sdf.format(c.time)
                        checkOutDisplay = displaySdf.format(c.time)
                    }
                    showCheckOutPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showCheckOutPicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Reserve Your Stay", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        Text(hotelName, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Room Info
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(room.roomType, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("Up to ${room.capacity} guests", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Text("₹${room.pricePerNight.toInt()}/night", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E63FF))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Check-in / Check-out
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Check-in
                    Surface(
                        modifier = Modifier.weight(1f).clickable { showCheckInPicker = true },
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("CHECK-IN", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 0.5.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(checkInDisplay, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                        }
                    }
                    // Check-out
                    Surface(
                        modifier = Modifier.weight(1f).clickable { showCheckOutPicker = true },
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("CHECK-OUT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 0.5.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(checkOutDisplay, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Guest Selection
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("GUESTS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 0.5.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$guests Guest${if (guests > 1) "s" else ""}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (guests > 1) guests-- },
                                modifier = Modifier.size(32.dp).background(Color(0xFFE2E8F0), CircleShape)
                            ) {
                                Icon(Icons.Default.Remove, null, tint = Color(0xFF475569), modifier = Modifier.size(16.dp))
                            }
                            Text("$guests", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), modifier = Modifier.padding(horizontal = 16.dp))
                            IconButton(
                                onClick = { if (guests < room.capacity) guests++ },
                                modifier = Modifier.size(32.dp).background(Color(0xFF1E63FF), CircleShape)
                            ) {
                                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Budget Breakdown
                Surface(
                    color = Color(0xFFF0F9FF),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("BUDGET BREAKDOWN", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E63FF), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(14.dp))

                        BudgetRow("Room (₹${room.pricePerNight.toInt()} × $nights night${if (nights > 1) "s" else ""})", "₹${roomTotal.toInt()}")
                        Spacer(modifier = Modifier.height(8.dp))
                        BudgetRow("Taxes & GST (18%)", "₹${taxes.toInt()}")
                        Spacer(modifier = Modifier.height(8.dp))
                        BudgetRow("Service Fee", "₹${serviceFee.toInt()}")

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFDBEAFE))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                            Text("₹${totalPrice.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1E63FF))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    }
                    Button(
                        onClick = {
                            onConfirm(
                                HotelBookingDetails(
                                    room = room,
                                    checkIn = checkInDate,
                                    checkOut = checkOutDate,
                                    guests = guests,
                                    nights = nights,
                                    totalPrice = totalPrice
                                )
                            )
                        },
                        modifier = Modifier.weight(1.5f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E63FF)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirm Booking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
