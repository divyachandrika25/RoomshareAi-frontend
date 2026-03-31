package com.simats.roomshareairentalapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : ComponentActivity(), PaymentResultListener {

    private var currentScreenState = mutableStateOf("landing")
    private var isBookingLoadingState = mutableStateOf(false)
    private var activeBookingRoomIdState = mutableStateOf<String?>(null)

    private var pendingPaymentData: Any? = null
    private var pendingPaymentType: String? = null

    private lateinit var sessionManager: SessionManager

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file
        } catch (_: Exception) {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        RetrofitClient.init(this)
        sessionManager = SessionManager(this)

        setContent {
            RoomshareAIRentalAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var sessionEmail by remember { mutableStateOf(sessionManager.fetchUserEmail() ?: "") }
                var currentScreen by currentScreenState
                if (sessionManager.isLoggedIn() && currentScreen == "landing") {
                    currentScreen = "home"
                }

                

                
                // --- PERSISTENT USER DATA ---
                var userFullName by remember { mutableStateOf("") }
                var userBio by remember { mutableStateOf("") }
                var userProfileImage by remember { mutableStateOf("") }
                
                var userAge by remember { mutableStateOf("") }
                var userOccupation by remember { mutableStateOf("") }
                var userTargetArea by remember { mutableStateOf("") }
                var userBudget by remember { mutableStateOf("") }
                var userMoveInDate by remember { mutableStateOf("") }
                
                var userListedRoom by remember { mutableStateOf<ListedRoomData?>(null) }
                var userAccountSettings by remember { mutableStateOf<AccountSettingsData?>(null) }

                val scope = rememberCoroutineScope()
                val context = LocalContext.current

                // API States
                val favoriteItems = remember { mutableStateListOf<FavoriteItem>() }
                var isFavoritesLoading by remember { mutableStateOf(false) }
                
                val discoverRoommates = remember { mutableStateListOf<DiscoverRoommate>() }
                var isDiscoverLoading by remember { mutableStateOf(false) }

                val homeRooms = remember { mutableStateListOf<HomeRoomItem>() }
                var isRoomsLoading by remember { mutableStateOf(false) }
                var isHomeError by remember { mutableStateOf(false) }

                var activeHomeRoomDetail by remember { mutableStateOf<HomeRoomDetailData?>(null) }
                var isHomeRoomDetailLoading by remember { mutableStateOf(false) }

                var activeRoomShareForm by remember { mutableStateOf<RoomShareFormData?>(null) }
                var isRoomShareFormLoading by remember { mutableStateOf(false) }

                var activeRoomShareRequestDetail by remember { mutableStateOf<RoomShareRequestDetailData?>(null) }
                var isRoomShareRequestDetailLoading by remember { mutableStateOf(false) }

                var activeRoomShareVerification by remember { mutableStateOf<RoomShareVerificationData?>(null) }
                var isRoomShareVerificationLoading by remember { mutableStateOf(false) }

                var activeRoomShareFinalReview by remember { mutableStateOf<RoomShareFinalReviewData?>(null) }
                var isRoomShareFinalReviewLoading by remember { mutableStateOf(false) }

                var activeSendRoomShareRequestData by remember { mutableStateOf<SendRoomShareRequestData?>(null) }
                var isSendRoomShareRequestLoading by remember { mutableStateOf(false) }

                var activeBookingConfirmation by remember { mutableStateOf<RoomBookingConfirmationData?>(null) }

                val notificationsList = remember { mutableStateListOf<ApiNotification>() }
                
                // Selection States
                var selectedRoomId by remember { mutableStateOf("") }
                var selectedRequestId by remember { mutableStateOf("") }
                var selectedMatchId by remember { mutableStateOf("") }
                var selectedTargetEmail by remember { mutableStateOf("") }
                var selectedGroupId by remember { mutableStateOf("") }
                
                // Global State
                val aiMatches = remember { mutableStateListOf<MatchResult>() }
                var isAiMatchesLoading by remember { mutableStateOf(false) }
                val aiHotelResults = remember { mutableStateListOf<AILocationAgentResult>() }
                var isAiHotelsLoading by remember { mutableStateOf(false) }
                val bookingHistory = remember { mutableStateListOf<BookingHistoryItem>() }
                var isBookingHistoryLoading by remember { mutableStateOf(false) }
                var activeBookingHistoryItem by remember { mutableStateOf<BookingHistoryItem?>(null) }
                var isBookingLoading by isBookingLoadingState
                var activeBookingRoomId by activeBookingRoomIdState
                val roommates = remember { mutableStateListOf<Roommate>() }
                val rooms = remember { mutableStateListOf<Room>() }

                // Auth States
                var isPasswordResetFlow by remember { mutableStateOf(false) }
                var userOtp by remember { mutableStateOf("") }
                var selectedOtpEmail by remember { mutableStateOf("") }

                fun fetchBookingHistory() {
                    if (sessionEmail.isNotEmpty()) {
                        scope.launch {
                            isBookingHistoryLoading = true
                            try {
                                val response = RetrofitClient.instance.getBookingHistory(sessionEmail)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    bookingHistory.clear()
                                    response.body()?.bookings?.let { bookings -> bookingHistory.addAll(bookings) }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to load booking history: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally { isBookingHistoryLoading = false }
                        }
                    }
                }

                fun fetchAiMatches(location: String? = null) {
                    if (sessionEmail.isNotEmpty()) {
                        scope.launch {
                            isAiMatchesLoading = true
                            try {
                                val response = RetrofitClient.instance.getMatches(sessionEmail, location)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    aiMatches.clear()
                                    response.body()?.matches?.let { matches -> aiMatches.addAll(matches) }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to load matches: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isAiMatchesLoading = false
                            }
                        }
                    }
                }

                fun fetchAiHotels(query: String) {
                    scope.launch {
                        isAiHotelsLoading = true
                        try {
                            val response = RetrofitClient.instance.aiLocationAgent(AILocationAgentRequest(query))
                            if (response.isSuccessful && response.body()?.success == true) {
                                aiHotelResults.clear()
                                response.body()?.results?.let { results -> aiHotelResults.addAll(results) }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Hotel search failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isAiHotelsLoading = false
                        }
                    }
                }

                fun refreshHomeData(location: String? = null) {
                    if (sessionEmail.isNotEmpty()) {
                        isHomeError = false
                        scope.launch {
                            isDiscoverLoading = true
                            try {
                                val response = RetrofitClient.instance.discoverRoommates(sessionEmail, location)
                                if (response.isSuccessful) {
                                    discoverRoommates.clear()
                                    response.body()?.roommates?.let { roommates -> discoverRoommates.addAll(roommates) }
                                } else {
                                    isHomeError = true
                                }
                            } catch (e: Exception) {
                                isHomeError = true
                                Toast.makeText(context, "Discover failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally { isDiscoverLoading = false }
                        }
                        scope.launch {
                            isRoomsLoading = true
                            try {
                                val response = RetrofitClient.instance.getHomeRooms(sessionEmail, location)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    homeRooms.clear()
                                    response.body()?.rooms?.let { rooms -> homeRooms.addAll(rooms) }
                                } else {
                                    isHomeError = true
                                }
                            } catch (e: Exception) {
                                isHomeError = true
                                Toast.makeText(context, "Rooms failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally { isRoomsLoading = false }
                        }
                        scope.launch {
                            try {
                                val response = RetrofitClient.instance.getNotifications(sessionEmail)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    notificationsList.clear()
                                    response.body()?.notifications?.let { notifs -> notificationsList.addAll(notifs) }
                                }
                            } catch (_: Exception) {}
                        }
                    }
                }


                var isAccountSettingsLoading by remember { mutableStateOf(false) }

                // --- AI Chatbot & Hotels State ---
                val aiChatbotMessages = remember { mutableStateListOf<AIChatbotMessage>() }
                var aiChatbotSuggestions by remember { mutableStateOf<List<String>?>(null) }
                var isAiChatbotLoading by remember { mutableStateOf(false) }
                var isAiChatbotSending by remember { mutableStateOf(false) }

                val hotelList = remember { mutableStateListOf<HotelItem>() }
                var recommendedHotels by remember { mutableStateOf<List<HotelItem>?>(null) }
                var hotelUserAreas by remember { mutableStateOf<List<String>?>(null) }
                var isHotelListLoading by remember { mutableStateOf(false) }

                var activeHotelDetail by remember { mutableStateOf<HotelDetailResponse?>(null) }
                var isHotelDetailLoading by remember { mutableStateOf(false) }
                var selectedHotelId by remember { mutableStateOf("") }
                // --- Room Share Request Flow State ---
                var rsIntroduction by remember { mutableStateOf("") }
                var rsMoveInDate by remember { mutableStateOf("") }
                var rsStayDuration by remember { mutableStateOf("") }
                var rsEmploymentStatus by remember { mutableStateOf("") }

                val directChatMessages = remember { mutableStateMapOf<String, SnapshotStateList<ChatMessage>>() }
                var activeDirectChatData by remember { mutableStateOf<DirectChatData?>(null) }
                
                val inboxItems = remember { mutableStateListOf<InboxItem>() }
                var isInboxLoading by remember { mutableStateOf(false) }

                // Notifications state moved higher

                // --- HELPER FUNCTIONS ---

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                
                fun openDrawer() { scope.launch { drawerState.open() } }
                fun closeDrawer() { scope.launch { drawerState.close() } }

                fun navigateTo(screen: String) {
                    closeDrawer()
                    if (screen == "subscription") {
                        startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                        return
                    }
                    when (screen) {
                        "saved" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isFavoritesLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getFavorites(sessionEmail)
                                        if (response.isSuccessful) {
                                            favoriteItems.clear()
                                            response.body()?.favorites?.let { favs -> favoriteItems.addAll(favs) }
                                        }
                                    } catch (_: Exception) {} finally { isFavoritesLoading = false }
                                }
                            }
                        }
                        "home" -> {
                            refreshHomeData()
                        }
                        "messages_list" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isInboxLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getMessagesInbox(sessionEmail, null)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            inboxItems.clear()
                                            response.body()?.messages?.let { msgs -> inboxItems.addAll(msgs) }
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to load chats: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally { isInboxLoading = false }
                                }
                            }
                        }

                        "notifications" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    try {
                                        val response = RetrofitClient.instance.getNotifications(sessionEmail)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            notificationsList.clear()
                                            response.body()?.notifications?.let { notifs -> notificationsList.addAll(notifs) }
                                        }
                                    } catch (_: Exception) {}
                                }
                            }
                        }
                        "account_settings" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isAccountSettingsLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getAccountSettings(sessionEmail)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            userAccountSettings = response.body()?.data
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to load settings: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally { isAccountSettingsLoading = false }
                                }
                            }
                        }
                        "booking_history" -> {
                            fetchBookingHistory()
                        }
                        "hotel_list" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isHotelListLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getHotels(sessionEmail, null)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            hotelList.clear()
                                            response.body()?.hotels?.let { hotels -> hotelList.addAll(hotels) }
                                            recommendedHotels = response.body()?.recommended
                                            hotelUserAreas = response.body()?.userAreas
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to load hotels: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally { isHotelListLoading = false }
                                }
                            }
                        }
                        "ai_chatbot" -> {
                            if (sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isAiChatbotLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getChatbotHistory(sessionEmail)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            aiChatbotMessages.clear()
                                            response.body()?.messages?.let { messages -> aiChatbotMessages.addAll(messages) }
                                            aiChatbotSuggestions = response.body()?.suggestions
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to load chatbot history: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally { isAiChatbotLoading = false }
                                }
                            }
                        }
                        "available_room_detail" -> {
                            if (selectedRoomId.isNotEmpty() && sessionEmail.isNotEmpty()) {
                                scope.launch {
                                    isHomeRoomDetailLoading = true
                                    try {
                                        val response = RetrofitClient.instance.getHomeRoomDetail(selectedRoomId, sessionEmail)
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            activeHomeRoomDetail = response.body()?.data
                                            currentScreen = "available_room_detail"
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to load room details: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally { isHomeRoomDetailLoading = false }
                                }
                                return
                            }
                        }
                    }
                    currentScreen = screen
                }

                fun fetchProfileDashboard(email: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.getProfileDashboard(email)
                            if (response.isSuccessful && response.body()?.success == true) {
                                val dashboardData = response.body()?.data
                                val profile = dashboardData?.profile
                                if (profile != null) {
                                    userFullName = profile.fullName ?: userFullName
                                    userBio = profile.aboutMe ?: userBio
                                    userAge = profile.age ?: userAge
                                    userOccupation = profile.occupation ?: userOccupation
                                    userTargetArea = profile.targetArea ?: userTargetArea
                                    userBudget = profile.budgetRange ?: userBudget
                                    userMoveInDate = profile.moveInDate ?: userMoveInDate
                                    userProfileImage = profile.photo ?: userProfileImage
                                }
                                userAccountSettings = dashboardData?.accountSettings ?: userAccountSettings
                            }
                        } catch (_: Exception) {}
                    }
                }

                fun fetchHomeRoomDetail(roomId: String) {
                    if (sessionEmail.isEmpty()) return
                    scope.launch {
                        isHomeRoomDetailLoading = true
                        try {
                            val response = RetrofitClient.instance.getHomeRoomDetail(roomId, sessionEmail)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeHomeRoomDetail = response.body()?.data
                                currentScreen = "available_room_detail"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load room details: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isHomeRoomDetailLoading = false }
                    }
                }

                fun fetchRoomShareForm(roomId: String) {
                    if (sessionEmail.isEmpty()) return
                    scope.launch {
                        isRoomShareFormLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomShareForm(roomId, sessionEmail)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeRoomShareForm = response.body()?.data
                                currentScreen = "introduce_yourself"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load request form: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isRoomShareFormLoading = false }
                    }
                }

                fun fetchRoomShareRequestDetail(requestId: String) {
                    selectedRequestId = requestId
                    scope.launch {
                        isRoomShareRequestDetailLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomShareRequestDetail(requestId)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeRoomShareRequestDetail = response.body()?.data
                                currentScreen = "room_share_request_detail"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load request detail: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isRoomShareRequestDetailLoading = false }
                    }
                }

                fun fetchRoomShareRequestSent(requestId: String) {
                    selectedRequestId = requestId
                    scope.launch {
                        isSendRoomShareRequestLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomShareRequestSent(requestId)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeSendRoomShareRequestData = response.body()?.data
                                currentScreen = "request_sent"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load request status: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isSendRoomShareRequestLoading = false }
                    }
                }

                fun fetchRoomShareVerification(requestId: String) {
                    selectedRequestId = requestId
                    scope.launch {
                        isRoomShareVerificationLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomShareVerification(requestId)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeRoomShareVerification = response.body()?.data
                                currentScreen = "safety_verification"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load verification info: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isRoomShareVerificationLoading = false }
                    }
                }

                fun fetchRoomShareFinalReview(requestId: String) {
                    selectedRequestId = requestId
                    scope.launch {
                        isRoomShareFinalReviewLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomShareFinalReview(requestId)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeRoomShareFinalReview = response.body()?.data
                                currentScreen = "final_review"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load review details: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally { isRoomShareFinalReviewLoading = false }
                    }
                }



                fun handleUploadIdentityDocument(requestId: String, source: String, uri: String) {
                    scope.launch {
                        try {
                            val requestIdPart = requestId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                            val sourcePart = source.toRequestBody("text/plain".toMediaTypeOrNull())
                            
                            val documentPart = getFileFromUri(uri.toUri())?.let { file ->
                                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("identity_document", file.name, requestFile)
                            } ?: return@launch

                            val response = RetrofitClient.instance.uploadIdentityDocument(requestIdPart, sourcePart, documentPart)
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, response.body()?.message ?: "Identity Uploaded!", Toast.LENGTH_SHORT).show()
                                navigateTo("home")
                            } else {
                                Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleUpdateRoomShareStatus(requestId: String, status: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.updateRoomShareStatus(requestId, UpdateRoomShareStatusRequest(status))
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Request $status", Toast.LENGTH_SHORT).show()
                                activeRoomShareRequestDetail = response.body()?.data
                            } else {
                                Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleRoomShareSubmit(idUri: String? = null) {
                    val form = activeRoomShareForm ?: return
                    scope.launch {
                        try {
                            val request = RoomShareSubmitRequest(
                                roomId = form.roomId,
                                userEmail = sessionEmail,
                                introMessage = rsIntroduction,
                                preferredMoveInDate = rsMoveInDate,
                                durationOfStay = rsStayDuration,
                                employmentStatus = rsEmploymentStatus
                            )
                            val response = RetrofitClient.instance.submitRoomShareRequest(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                val rid = response.body()?.data?.requestId
                                if (rid != null) {
                                    if (idUri != null) {
                                        val source = if (idUri.startsWith("content://com.simats.roomshareairentalapp.fileprovider")) "camera" else "gallery"
                                        handleUploadIdentityDocument(rid, source, idUri)
                                    } else {
                                        fetchRoomShareFinalReview(rid)
                                    }
                                } else {
                                    currentScreen = "request_sent"
                                }
                            } else {
                                Toast.makeText(context, response.body()?.error ?: "Submission failed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleSendRoomShareRequest(requestId: String) {
                    scope.launch {
                        isSendRoomShareRequestLoading = true
                        try {
                            val response = RetrofitClient.instance.sendRoomShareRequest(mapOf("request_id" to requestId))
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeSendRoomShareRequestData = response.body()?.data
                                currentScreen = "request_sent"
                            } else {
                                Toast.makeText(context, response.body()?.error ?: "Failed to send request", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isSendRoomShareRequestLoading = false
                        }
                    }
                }



                fun fetchHotelDetail(hotelId: String) {
                    if (sessionEmail.isEmpty()) return
                    selectedHotelId = hotelId
                    activeHotelDetail = null // Reset state to prevent showing old details
                    scope.launch {
                        isHotelDetailLoading = true
                        try {
                            val response = RetrofitClient.instance.getHotelDetail(hotelId)
                            if (response.isSuccessful && response.body()?.success == true) {
                                activeHotelDetail = response.body()
                                currentScreen = "hotel_detail"
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to load hotel detail: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isHotelDetailLoading = false
                        }
                    }
                }

                fun handleToggleHotelFavorite(hotelId: String) {
                    if (sessionEmail.isEmpty()) return
                    if (hotelId.startsWith("ext-")) {
                        Toast.makeText(context, "External hotels cannot be favorited yet", Toast.LENGTH_SHORT).show()
                        return
                    }
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.toggleHotelFavorite(mapOf("email" to sessionEmail, "hotel_id" to hotelId))
                            if (response.isSuccessful && response.body()?.success == true) {
                                val itemIndex = hotelList.indexOfFirst { it.id == hotelId }
                                if (itemIndex != -1) {
                                    val h = hotelList[itemIndex]
                                    hotelList[itemIndex] = h.copy(isFavorite = !(h.isFavorite ?: false))
                                }
                                val recommendedIndex = recommendedHotels?.indexOfFirst { it.id == hotelId } ?: -1
                                if (recommendedIndex != -1) {
                                    val lst = recommendedHotels!!.toMutableList()
                                    lst[recommendedIndex] = lst[recommendedIndex].copy(isFavorite = !(lst[recommendedIndex].isFavorite ?: false))
                                    recommendedHotels = lst
                                }
                                Toast.makeText(context, "Favorite updated", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {}
                    }
                }

                fun handleBookHotelRoom(details: HotelBookingDetails) {
                    val totalPaise = (details.totalPrice * 100).toLong().toString()
                    isBookingLoading = true
                    activeBookingRoomId = details.room.id
                    pendingPaymentType = "hotel_booking"
                    pendingPaymentData = HotelRoomBookingRequest(
                        hotelId = selectedHotelId,
                        roomId = details.room.id,
                        email = sessionEmail,
                        checkIn = details.checkIn,
                        checkOut = details.checkOut,
                        guests = details.guests,
                        totalPrice = details.totalPrice,
                        hotelName = activeHotelDetail?.hotel?.name,
                        hotelAddress = activeHotelDetail?.hotel?.address,
                        price = details.room.pricePerNight
                    )
                    startRazorpayPayment(totalPaise, "Hotel Stay: ${activeHotelDetail?.hotel?.name}")
                }

                fun handleSendChatbotMessage(text: String) {
                    aiChatbotMessages.add(AIChatbotMessage(role = "user", content = text))
                    isAiChatbotSending = true
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.sendChatbotMessage(sessionEmail, mapOf("message" to text))
                            if (response.isSuccessful && response.body()?.success == true) {
                                aiChatbotMessages.add(AIChatbotMessage(role = "assistant", content = response.body()?.response ?: ""))
                            } else {
                                aiChatbotMessages.add(AIChatbotMessage(role = "assistant", content = "Sorry, I couldn't process that. Please try again."))
                            }
                        } catch (e: Exception) {
                            aiChatbotMessages.add(AIChatbotMessage(role = "assistant", content = "Network error. Please try again."))
                        } finally {
                            isAiChatbotSending = false
                        }
                    }
                }

                fun handleClearChatbotHistory() {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.clearChatbotHistory(sessionEmail)
                            if (response.isSuccessful && response.body()?.success == true) {
                                aiChatbotMessages.clear()
                                Toast.makeText(context, "Chat cleared", Toast.LENGTH_SHORT).show()
                            }
                        } catch (_: Exception) {}
                    }
                }

                fun fetchRoomBookingDetail(bookingId: String) {
                    scope.launch {
                        isBookingLoading = true
                        try {
                            val response = RetrofitClient.instance.getRoomBookingDetail(bookingId)
                            if (response.isSuccessful) {
                                val detail = response.body()
                                if (detail != null) {
                                    activeBookingConfirmation = RoomBookingConfirmationData(
                                        bookingId = detail.bookingId,
                                        roomTitle = detail.roomTitle,
                                        monthlyRent = detail.monthlyRent,
                                        securityDeposit = detail.securityDeposit,
                                        serviceFee = detail.serviceFee,
                                        totalDueNow = detail.totalDueNow,
                                        paymentMethodLast4 = detail.paymentMethodLast4,
                                        paymentStatus = detail.paymentStatus,
                                        confirmationTitle = "Booking Details",
                                        confirmationMessage = "Details for your booking of ${detail.roomTitle}",
                                        nextSteps = listOf("Contact your roommates", "Plan move-in date"),
                                        backButtonText = "Back to Dashboard"
                                    )
                                    navigateTo("booking_success")
                                }
                            }
                        } catch (_: Exception) {} finally { isBookingLoading = false }
                    }
                }

                fun handleUpdateProfile(n: String, b: String, o: String, a: String, t: String, bu: String, m: String, i: String) {
                    scope.launch {
                        try {
                            val emailPart = sessionEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                            val fullNamePart = n.toRequestBody("text/plain".toMediaTypeOrNull())
                            val agePart = a.toRequestBody("text/plain".toMediaTypeOrNull())
                            val roomStatusPart = "SEEKING_ROOM".toRequestBody("text/plain".toMediaTypeOrNull())
                            val aboutMePart = b.toRequestBody("text/plain".toMediaTypeOrNull())
                            val occupationPart = o.toRequestBody("text/plain".toMediaTypeOrNull())
                            val targetAreaPart = t.toRequestBody("text/plain".toMediaTypeOrNull())
                            val budgetRangePart = bu.toRequestBody("text/plain".toMediaTypeOrNull())
                            val moveInDatePart = m.toRequestBody("text/plain".toMediaTypeOrNull())

                            var photoPart: MultipartBody.Part? = null
                            if (i.startsWith("content://") || i.startsWith("file://")) {
                                getFileFromUri(i.toUri())?.let { file ->
                                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                    photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                                }
                            }

                            val response = RetrofitClient.instance.updateProfile(
                                emailPart, fullNamePart, agePart, roomStatusPart,
                                aboutMePart, occupationPart, targetAreaPart,
                                budgetRangePart, moveInDatePart, photoPart
                            )

                            if (response.isSuccessful && response.body()?.success == true) {
                                response.body()?.data?.let { updated ->
                                    userFullName = updated.fullName ?: n
                                    userBio = updated.aboutMe ?: b
                                    userAge = updated.age ?: a
                                    userOccupation = updated.occupation ?: o
                                    userTargetArea = updated.targetArea ?: t
                                    userBudget = updated.budgetRange ?: bu
                                    userMoveInDate = updated.moveInDate ?: m
                                    if (updated.photo != null) userProfileImage = updated.photo
                                }
                                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleUpdateProfilePhoto(uri: String) {
                    scope.launch {
                        try {
                            val emailPart = sessionEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                            val sourcePart = "gallery".toRequestBody("text/plain".toMediaTypeOrNull())
                            getFileFromUri(uri.toUri())?.let { file ->
                                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                    val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                                val response = RetrofitClient.instance.uploadProfilePhoto(emailPart, sourcePart, photoPart)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    userProfileImage = response.body()?.data?.photo ?: uri
                                }
                            }
                        } catch (_: Exception) {}
                    }
                }
                
                fun handleUpdateAccountSettings(notif: Boolean?, lang: String?, privacy: String?) {
                    scope.launch {
                        try {
                            val request = UpdateAccountSettingsRequest(
                                email = sessionEmail,
                                notificationsEnabled = notif,
                                language = lang,
                                privacySettings = privacy
                            )
                            val response = RetrofitClient.instance.updateAccountSettings(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                userAccountSettings = response.body()?.data ?: userAccountSettings
                                Toast.makeText(context, response.body()?.message ?: "Settings Updated!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (_: Exception) {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleChangeEmail(current: String, next: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.changeEmail(ChangeEmailRequest(current, next))
                            if (response.isSuccessful && response.body()?.success == true) {
                                val newEmail = response.body()?.newEmail ?: next
                                sessionEmail = newEmail
                                sessionManager.saveUserEmail(newEmail)
                                Toast.makeText(context, response.body()?.message ?: "Email changed!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, response.body()?.error ?: "Failed to change email", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleChangePassword(email: String, old: String, next: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.changePassword(ChangePasswordRequest(email, old, next))
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, response.body()?.message ?: "Password changed!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, response.body()?.error ?: "Failed to change password", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleDeleteAccount(email: String, pass: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.deleteAccount(DeleteAccountRequest(email, pass))
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Account deleted. Goodbye!", Toast.LENGTH_LONG).show()
                                sessionEmail = ""
                                sessionManager.logout()
                                navigateTo("landing")
                            } else {
                                Toast.makeText(context, response.body()?.error ?: "Failed to delete account", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleMarkNotificationAsRead(notificationId: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.markNotificationRead(MarkNotificationReadRequest(notificationId))
                            if (response.isSuccessful && response.body()?.success == true) {
                                // Update local list
                                val index = notificationsList.indexOfFirst { it.id == notificationId }
                                if (index != -1) {
                                    val old = notificationsList[index]
                                    notificationsList[index] = old.copy(isRead = true)
                                }
                            }
                        } catch (_: Exception) {}
                    }
                }

                fun handleSaveFavorite(matchedUserEmail: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.saveFavorite(SaveFavoriteRequest(sessionEmail, matchedUserEmail))
                            if (response.isSuccessful) {
                                Toast.makeText(context, response.body()?.message ?: "Saved!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (_: Exception) {}
                    }
                }

                fun handleToggleRoomFavorite(roomId: String) {
                    scope.launch {
                        try {
                            val requestBody = mapOf("email" to sessionEmail, "room_id" to roomId)
                            val response = RetrofitClient.instance.toggleRoomFavorite(requestBody)
                            if (response.isSuccessful) {
                                val msg = response.body()?.message ?: "Updated!"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                // Update local rooms list if possible
                                val index = rooms.indexOfFirst { it.id == roomId }
                                if (index != -1) {
                                    rooms[index] = rooms[index].copy(isSaved = response.body()?.success == true)
                                }
                            }
                        } catch (_: Exception) {}
                    }
                }



                fun handleCreateDirectChat(otherEmail: String, roomId: String? = null) {
                    if (sessionEmail.isEmpty()) {
                        Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                        return
                    }
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.createDirectChat(CreateDirectChatRequest(sessionEmail, otherEmail, roomId))
                            if (response.isSuccessful && response.body()?.success == true) {
                                response.body()?.data?.let { data ->
                                    activeDirectChatData = data
                                    val list = directChatMessages.getOrPut(data.chatId ?: "0") { mutableStateListOf<ChatMessage>() }
                                    list.clear()
                                    data.messages?.forEach { msg ->
                                        list.add(ChatMessage(
                                            senderName = msg.senderName ?: "Unknown",
                                            text = msg.content ?: "",
                                            isFromMe = msg.isCurrentUser,
                                            time = msg.createdAt ?: "",
                                            isRoomAction = msg.messageType == "ROOM_SHARE",
                                            isImage = msg.messageType == "IMAGE",
                                            imageUrl = msg.image,
                                            roomTitle = msg.roomTitle,
                                            roomPrice = msg.roomPrice,
                                            roomBeds = msg.roomBeds,
                                            roomBaths = msg.roomBaths
                                        ))
                                    }
                                    navigateTo("direct_chat")
                                } ?: run {
                                    Toast.makeText(context, "Failed to initialize chat data", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val errorMsg = response.errorBody()?.string() ?: "Failed to create chat"
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleRequestRoomShareForUser(otherEmail: String) {
                    if (sessionEmail.isEmpty()) {
                        Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                        return
                    }
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.sendRoomShareRequest(mapOf<String, String>("user_email" to sessionEmail, "target_email" to otherEmail))
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, response.body()?.message ?: "Request sent!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleGetDirectChatDetail(chatId: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.getDirectChatDetail(chatId, sessionEmail)
                            if (response.isSuccessful) {
                                val data = response.body()
                                if (data != null) {
                                    activeDirectChatData = data
                                    val list = directChatMessages.getOrPut(data.chatId ?: chatId) { mutableStateListOf() }
                                    list.clear()
                                    data.messages?.forEach { msg ->
                                        list.add(ChatMessage(
                                            senderName = msg.senderName ?: "Unknown",
                                            text = msg.content ?: "",
                                            isFromMe = msg.isCurrentUser,
                                            time = msg.createdAt ?: "",
                                            isRoomAction = msg.messageType == "ROOM_SHARE",
                                            isImage = msg.messageType == "IMAGE",
                                            imageUrl = msg.image,
                                            roomTitle = msg.roomTitle,
                                            roomPrice = msg.roomPrice,
                                            roomBeds = msg.roomBeds,
                                            roomBaths = msg.roomBaths
                                        ))
                                    }
                                    navigateTo("direct_chat")
                                } else {
                                    Toast.makeText(context, "No chat data found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Failed to load chat: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleSendDirectChatMessage(chatId: String, message: String) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.sendDirectChatMessage(SendDirectChatMessageRequest(chatId, sessionEmail, message))
                            if (response.isSuccessful && response.body()?.success == true) {
                                response.body()?.data?.let { data ->
                                    activeDirectChatData = data
                                    val list = directChatMessages.getOrPut(chatId) { mutableStateListOf() }
                                    list.clear()
                                    data.messages?.forEach { msg ->
                                        list.add(ChatMessage(
                                            senderName = msg.senderName ?: "Unknown",
                                            text = msg.content ?: "",
                                            isFromMe = msg.isCurrentUser,
                                            time = msg.createdAt ?: "",
                                            isRoomAction = msg.messageType == "ROOM_SHARE",
                                            isImage = msg.messageType == "IMAGE",
                                            imageUrl = msg.image,
                                            roomTitle = msg.roomTitle,
                                            roomPrice = msg.roomPrice,
                                            roomBeds = msg.roomBeds,
                                            roomBaths = msg.roomBaths
                                        ))
                                    }
                                }
                            }
                        } catch (_: Exception) {}
                    }
                }

                fun handlePublishRoomListing(title: String, address: String, city: String, rent: String, desc: String, roommates: Int, images: List<String>) {
                    scope.launch {
                        try {
                            val emailPart = sessionEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
                            val addressPart = address.toRequestBody("text/plain".toMediaTypeOrNull())
                            val cityPart = city.toRequestBody("text/plain".toMediaTypeOrNull())
                            val rentPart = rent.toRequestBody("text/plain".toMediaTypeOrNull())
                            val descPart = desc.toRequestBody("text/plain".toMediaTypeOrNull())
                            val roommatesPart = roommates.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                            
                            val photoParts = images.mapNotNull { uri ->
                                if (uri.startsWith("http")) return@mapNotNull null // Skip already uploaded photos
                                getFileFromUri(uri.toUri())?.let { file ->
                                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                    MultipartBody.Part.createFormData("photos", file.name, requestFile)
                                }
                            }

                            val response = RetrofitClient.instance.createOrUpdateListedRoom(
                                emailPart, titlePart, addressPart, cityPart, rentPart, descPart,
                                null, null, roommatesPart, null, photoParts
                            )

                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, response.body()?.message ?: "Published!", Toast.LENGTH_SHORT).show()
                                navigateTo("home")
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Listing failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                fun handleLogout() {
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.logout()
                            if (response.isSuccessful) {
                                Toast.makeText(context, response.body()?.message ?: "Logged out", Toast.LENGTH_SHORT).show()
                            }
                        } catch (_: Exception) {} finally {
                            sessionEmail = ""
                            sessionManager.logout()
                            favoriteItems.clear()
                            discoverRoommates.clear()
                            inboxItems.clear()
                            userListedRoom = null
                            userAccountSettings = null
                            homeRooms.clear()
                            activeHomeRoomDetail = null
                            activeRoomShareForm = null
                            activeRoomShareRequestDetail = null
                            activeRoomShareVerification = null
                            activeRoomShareFinalReview = null
                            activeSendRoomShareRequestData = null
                            activeBookingConfirmation = null
                            navigateTo("login")
                        }
                    }
                }

                // Initial data fetch if logged in
                LaunchedEffect(sessionEmail) {
                    if (sessionEmail.isNotEmpty()) {
                        fetchProfileDashboard(sessionEmail)
                        refreshHomeData()
                        // Pop subscription card on app start if logged in
                        if (!sessionManager.isPremiumUser()) {
                            startActivity(Intent(context, SubscriptionActivity::class.java))
                        }
                    }
                }

                val onSaveRoommate: (String) -> Unit = { id ->
                    val index = roommates.indexOfFirst { it.id == id }
                    if (index != -1) {
                        val isSaving = !roommates[index].isSaved
                        roommates[index] = roommates[index].copy(isSaved = isSaving)
                    }
                }

                val onSaveRoom: (String) -> Unit = { id ->
                    val index = rooms.indexOfFirst { it.id == id }
                    if (index != -1) rooms[index] = rooms[index].copy(isSaved = !rooms[index].isSaved)
                }

                val noDrawerScreens = listOf("landing", "login", "signup", "lifestyle_habits", "budget_location", "ai_processing", "ai_results", "otp_verification", "forgot_password", "reset_password", "email_verification", "new_password")

                // Force close drawer if on a no-drawer screen
                LaunchedEffect(currentScreen) {
                    if (currentScreen in noDrawerScreens && drawerState.isOpen) {
                        drawerState.close()
                    }
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        if (currentScreen !in noDrawerScreens) {
                            AppDrawer(
                                currentScreen = currentScreen,
                                onNavigate = ::navigateTo,
                                userEmail = sessionEmail,
                                userName = userFullName,
                                userProfileImage = userProfileImage,
                                onLogout = { handleLogout() }
                            )
                        } else {
                            // Empty box to satisfy drawerContent requirement while hidden
                            Box(Modifier.size(0.dp))
                        }
                    },
                    gesturesEnabled = currentScreen !in noDrawerScreens
                ) {
                    Scaffold(
                        bottomBar = {
                            if (currentScreen in listOf("home", "matches", "messages_list", "profile")) {
                                BottomNavigationBar(currentScreen = currentScreen, onNavigate = { navigateTo(it) })
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()).fillMaxSize()) {
                            when (currentScreen) {
                                "landing" -> LandingScreen(onGetStarted = { navigateTo("login") })
                                "login" -> LoginScreen(
                                    onBackClick = { navigateTo("landing") }, 
                                    onSignUpClick = { navigateTo("signup") }, 
                                    onForgotPasswordClick = { navigateTo("forgot_password") }, 
                                    onLoginClick = { email, token -> 
                                        sessionEmail = email
                                        sessionManager.saveAuthToken(token)
                                        sessionManager.saveUserEmail(email)
                                        fetchProfileDashboard(email)
                                        // Show subscription on every login
                                        startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                                        navigateTo("home") 
                                    },
                                    onVerifyRequired = { email ->
                                        sessionEmail = email
                                        isPasswordResetFlow = false
                                        navigateTo("otp_verification")
                                    }
                                )
                                "signup" -> SignUpScreen(
                                    onBackClick = { navigateTo("login") }, 
                                    onLoginClick = { navigateTo("login") }, 
                                    onCreateAccountClick = { email -> 
                                        sessionEmail = email
                                        sessionManager.saveUserEmail(email)
                                        isPasswordResetFlow = false
                                        navigateTo("otp_verification") 
                                    }
                                )
                                
                                "lifestyle_habits" -> LifestyleHabitsScreen(email = sessionEmail, onBackClick = { navigateTo("signup") }, onNextStepClick = { navigateTo("budget_location") })
                                "budget_location" -> BudgetLocationScreen(
                                    email = sessionEmail, 
                                    onBackClick = { navigateTo("lifestyle_habits") }, 
                                    onGenerateProfileClick = { city -> 
                                        userTargetArea = city
                                        navigateTo("ai_processing") 
                                    }
                                )
                                "ai_processing" -> AIProcessingScreen(onProcessingFinished = { 
                                    fetchAiMatches(userTargetArea)
                                    refreshHomeData(userTargetArea)
                                    fetchAiHotels("hotels in $userTargetArea")
                                    navigateTo("ai_results") 
                                })
                                "ai_results" -> AIResultsScreen(
                                    aiMatches = aiMatches,
                                    homeMatches = homeRooms,
                                    hotelResults = aiHotelResults,
                                    isLoading = isAiMatchesLoading || isAiHotelsLoading,
                                    onBackClick = { navigateTo("budget_location") }, 
                                    onViewMatchesClick = { navigateTo("home") }
                                )
                                
                                "forgot_password" -> ForgotPasswordScreen(onBackClick = { navigateTo("login") }, onContinueClick = { navigateTo("email_verification") })
                                "email_verification" -> EmailVerificationScreen(onBackClick = { navigateTo("forgot_password") }, onSendCodeClick = { email -> sessionEmail = email; isPasswordResetFlow = true; navigateTo("otp_verification") })
                                "otp_verification" -> OTPVerificationScreen(
                                    email = sessionEmail, 
                                    isPasswordResetFlow = isPasswordResetFlow,
                                    onBackClick = { if (isPasswordResetFlow) navigateTo("email_verification") else navigateTo("signup") }, 
                                    onVerifyClick = { otp -> 
                                        userOtp = otp
                                        if (isPasswordResetFlow) navigateTo("reset_password") else navigateTo("lifestyle_habits") 
                                    }
                                )
                                "reset_password" -> ResetPasswordScreen(
                                    email = sessionEmail, 
                                    otp = userOtp,
                                    onBackClick = { navigateTo("otp_verification") }, 
                                    onResetSuccess = { navigateTo("login") }
                                )

                                "home" -> HomeScreen(
                                    onMenuClick = { openDrawer() },
                                    onProfileClick = { navigateTo("profile") },
                                    onNotificationsClick = { navigateTo("notifications") },
                                    discoverRoommates = discoverRoommates,
                                    isDiscoverLoading = isDiscoverLoading,
                                    homeRooms = homeRooms,
                                    isRoomsLoading = isRoomsLoading,
                                    onViewMatchesClick = { navigateTo("matches") },
                                    onDiscoverRoommateClick = { dr -> selectedTargetEmail = dr.email; navigateTo("profile_detail") },
                                    onHomeRoomClick = { hr -> fetchHomeRoomDetail(hr.id) },
                                    onSaveFavorite = { handleSaveFavorite(it) },
                                    onAIButtonClick = { 
                                        if (sessionManager.isPremiumUser() || sessionManager.getAiUsageCount() < 5) {
                                            if (!sessionManager.isPremiumUser()) sessionManager.incrementAiUsage()
                                            navigateTo("ai_assistant") 
                                        } else {
                                            startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                                        }
                                    },
                                    onSearch = { query -> 
                                        if (sessionEmail.isNotEmpty()) {
                                            scope.launch {
                                                isDiscoverLoading = true
                                                try {
                                                    val response = RetrofitClient.instance.discoverRoommates(sessionEmail, query)
                                                    if (response.isSuccessful) {
                                                        discoverRoommates.clear()
                                                        response.body()?.roommates?.let { roommates -> discoverRoommates.addAll(roommates) }
                                                    }
                                                } catch (_: Exception) {} finally { isDiscoverLoading = false }
                                            }
                                            scope.launch {
                                                isRoomsLoading = true
                                                try {
                                                    val response = RetrofitClient.instance.getHomeRooms(sessionEmail, query)
                                                    if (response.isSuccessful && response.body()?.success == true) {
                                                        homeRooms.clear()
                                                        response.body()?.rooms?.let { data -> homeRooms.addAll(data) }
                                                    }
                                                } catch (_: Exception) {} finally { isRoomsLoading = false }
                                            }
                                        }
                                    },
                                    isError = isHomeError,
                                    onRetry = { refreshHomeData() },
                                    refreshHomeData = { refreshHomeData() }
                                )
                                "matches" -> MatchesScreen(
                                    onMenuClick = { openDrawer() },
                                    email = sessionEmail,
                                    onMatchClick = { match -> 
                                        match.matchedUser?.email?.let { targetEmail ->
                                            selectedTargetEmail = targetEmail
                                            navigateTo("profile_detail")
                                        }
                                    }, 
                                    onBackClick = { navigateTo("home") }, 
                                    onSaveRoommateToggle = onSaveRoommate,
                                    onSaveFavorite = { handleSaveFavorite(it) }
                                )
                                "match_detail" -> MatchDetailScreen(matchId = selectedMatchId, onBackClick = { navigateTo("matches") }, onChatClick = { handleCreateDirectChat(it) })
                                
                                "messages_list" -> {
                                        val summaries = inboxItems.map { item -> 
                                            ChatSummary(
                                                id = "${item.conversationType}_${item.conversationId ?: "0"}", 
                                                name = item.title ?: "Unknown", 
                                                lastMessage = item.subtitle ?: "", 
                                                time = item.time ?: "", 
                                                imageUrl = item.avatar ?: "", 
                                                unreadCount = item.unreadCount ?: 0, 
                                                isGroup = item.conversationType == "group"
                                            ) 
                                        }
                                    MessagesListScreen(
                                        onMenuClick = { openDrawer() },
                                        chatSummaries = summaries, isLoading = isInboxLoading, 
                                        onSearch = { query -> 
                                            if (sessionEmail.isNotEmpty()) {
                                                scope.launch {
                                                    isInboxLoading = true
                                                    try {
                                                        val response = RetrofitClient.instance.getMessagesInbox(sessionEmail, query)
                                                        if (response.isSuccessful && response.body()?.success == true) {
                                                            inboxItems.clear()
                                                            response.body()?.messages?.let { msgs -> inboxItems.addAll(msgs) }
                                                        }
                                                     } catch (e: Exception) {
                                                        Toast.makeText(context, "Search failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                                    } finally { isInboxLoading = false }
                                                }
                                            }
                                        }, 
                                        onChatClick = { chat -> 
                                            val cleanId = chat.id.substringAfter("_")
                                            if(chat.isGroup) { 
                                                selectedGroupId = cleanId; navigateTo("chat") 
                                            } else { 
                                                handleGetDirectChatDetail(cleanId) 
                                            } 
                                        }, 
                                        onRefresh = { 
                                            if (sessionEmail.isNotEmpty()) {
                                                scope.launch {
                                                    isInboxLoading = true
                                                    try {
                                                        val response = RetrofitClient.instance.getMessagesInbox(sessionEmail, null)
                                                        if (response.isSuccessful && response.body()?.success == true) {
                                                            inboxItems.clear()
                                                            response.body()?.messages?.let { msgs -> inboxItems.addAll(msgs) }
                                                        }
                                                    } catch (e: Exception) {
                                                        Toast.makeText(context, "Refresh failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                                    } finally { isInboxLoading = false }
                                                }
                                            }
                                        },
                                        onBackClick = { navigateTo("home") }
                                    )
                                }



                                "direct_chat" -> activeDirectChatData?.let { data ->
                                    DirectChatScreen(
                                        roommate = Roommate(
                                            id = data.chatId.toString(), 
                                            name = data.otherUserName ?: "Unknown Roommate", 
                                            email = "dummy@email.com", 
                                            age = 25, 
                                            location = "", 
                                            tags = emptyList(), 
                                            price = "", 
                                            imageUrl = data.otherUserPhoto ?: "", 
                                            matchPercentage = 95, 
                                            status = "SEEKING ROOM", 
                                            isSaved = false
                                        ),
                                        messages = directChatMessages.getOrPut(data.chatId ?: "0") { mutableStateListOf() },
                                        onSendMessage = { text, _ -> handleSendDirectChatMessage(data.chatId ?: "0", text) },
                                        onBackClick = { navigateTo("messages_list") }
                                    )
                                }
                                
                                "profile_detail" -> ProfileDetailScreen(
                                    currentEmail = sessionEmail, targetEmail = selectedTargetEmail, onBackClick = { navigateTo("home") },
                                    onAICompatibilityClick = { _, t -> selectedTargetEmail = t; navigateTo("ai_compatibility") },
                                    onSendMessageClick = { handleRequestRoomShareForUser(it) }, onSaveToggle = { handleSaveFavorite(it) }
                                )
                                "ai_compatibility" -> AICompatibilityScreen(currentEmail = sessionEmail, targetEmail = selectedTargetEmail, onBackClick = { navigateTo("profile_detail") })
                                "available_room_detail" -> AvailableRoomDetailScreen(
                                    homeRoomDetail = activeHomeRoomDetail,
                                    isLoading = isHomeRoomDetailLoading,
                                    onBackClick = { 
                                        activeHomeRoomDetail = null
                                        navigateTo("home") 
                                    }, 
                                    onChatWithRoommate = { handleCreateDirectChat(it) }, 
                                    onSaveToggle = { handleToggleRoomFavorite(activeHomeRoomDetail?.id?.toString() ?: "") }, 
                                    onRequestShare = { 
                                        activeHomeRoomDetail?.let { fetchRoomShareForm(it.id.toString()) }
                                    }
                                )
                                "introduce_yourself" -> IntroduceYourselfScreen(
                                    formData = activeRoomShareForm,
                                    isLoading = isRoomShareFormLoading,
                                    onBackClick = { currentScreen = "available_room_detail" },
                                    onContinueClick = { message ->
                                        rsIntroduction = message
                                        currentScreen = "details"
                                    }
                                )
                                "details" -> DetailsScreen(
                                    formData = activeRoomShareForm,
                                    onBackClick = { currentScreen = "introduce_yourself" },
                                    onContinueClick = { moveIn, duration, employment ->
                                        rsMoveInDate = moveIn
                                        rsStayDuration = duration
                                        rsEmploymentStatus = employment
                                        handleRoomShareSubmit(null)
                                    }
                                )
                                "safety_verification" -> SafetyVerificationScreen(
                                    verificationData = activeRoomShareVerification,
                                    isLoading = isRoomShareVerificationLoading,
                                    onBackClick = { 
                                        if (activeRoomShareVerification != null) navigateTo("home")
                                        else currentScreen = "details" 
                                    },
                                    onVerifyClick = { uri ->
                                        if (activeRoomShareVerification != null) {
                                            if (uri != null) {
                                                val source = if (uri.startsWith("content://com.simats.roomshareairentalapp.fileprovider")) "camera" else "gallery"
                                                handleUploadIdentityDocument(selectedRequestId, source, uri)
                                            } else {
                                                Toast.makeText(context, "No document selected", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            handleRoomShareSubmit(uri)
                                        }
                                    }
                                )
                                "final_review" -> FinalReviewScreen(
                                    finalReviewData = activeRoomShareFinalReview,
                                    isLoading = isRoomShareFinalReviewLoading || isSendRoomShareRequestLoading,
                                    onBackClick = { currentScreen = "safety_verification" },
                                    onSendRequestClick = {
                                        activeRoomShareFinalReview?.id?.let { handleSendRoomShareRequest(it) }
                                    }
                                )
                                "request_sent" -> RequestSentScreen(
                                    data = activeSendRoomShareRequestData,
                                    onBackClick = { navigateTo("home") },
                                    onBackToHomeClick = { navigateTo("home") },
                                    onMessageOwnerClick = { email, rid -> handleCreateDirectChat(email, rid) }
                                )
                                "notifications" -> {
                                    NotificationScreen(
                                        email = sessionEmail,
                                        onBackClick = { navigateTo("home") },
                                        onNotificationClick = { notif ->
                                            notif.relatedId?.let { rid ->
                                                if (notif.notificationType == "ROOM") {
                                                    if (notif.title == "Request Sent") {
                                                        fetchRoomShareRequestSent(rid)
                                                    } else {
                                                        fetchRoomShareRequestDetail(rid)
                                                    }
                                                } else if (notif.notificationType == "VERIFY") {
                                                    fetchRoomShareVerification(rid)
                                                }
                                            }
                                            if (notif.notificationType == "MESSAGE") {
                                                navigateTo("messages_list")
                                            }
                                        }
                                    )
                                }

                            "room_share_request_detail" -> RoomShareRequestDetailScreen(
                                request = activeRoomShareRequestDetail,
                                isLoading = isRoomShareRequestDetailLoading,
                                onBackClick = { navigateTo("notifications") },
                                onChatClick = { handleCreateDirectChat(it) },
                                onAcceptClick = { handleUpdateRoomShareStatus(it, "ACCEPTED") },
                                onDeclineClick = { handleUpdateRoomShareStatus(it, "DECLINED") }
                            )


                            "profile" -> ProfileScreen(
                                onBackClick = { navigateTo("home") }, onLogout = { handleLogout() },
                                onAccountSettingsClick = { navigateTo("account_settings") }, onHelpClick = { navigateTo("ai_assistant") }, 
                                onBookingHistoryClick = { navigateTo("booking_history") }, onSavedRoomsClick = { navigateTo("saved") }, 
                                onPrivacyClick = { navigateTo("privacy_security") }, 
                                onAIChatbotClick = { 
                                    if (sessionManager.isPremiumUser() || sessionManager.getAiUsageCount() < 5) {
                                        if (!sessionManager.isPremiumUser()) sessionManager.incrementAiUsage()
                                        navigateTo("ai_chatbot") 
                                    } else {
                                        startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                                    }
                                },
                                fullName = userFullName, bio = userBio, age = userAge, occupation = userOccupation, targetArea = userTargetArea, budget = userBudget, moveInDate = userMoveInDate, profileImage = userProfileImage, isPremium = sessionManager.isPremiumUser(),
                                onUpdateProfile = { n, b, o, a, t, bu, m, i -> if (i != userProfileImage) handleUpdateProfilePhoto(i); handleUpdateProfile(n, b, o, a, t, bu, m, i) }
                            )
                            "account_settings" -> AccountSettingsScreen(
                                initialSettings = userAccountSettings,
                                userEmail = sessionEmail,
                                isLoading = isAccountSettingsLoading,
                                onBackClick = { navigateTo("profile") },
                                onUpdateSettings = { notif, lang, privacy -> handleUpdateAccountSettings(notif, lang, privacy) },
                                onChangeEmail = { current, next -> handleChangeEmail(current, next) },
                                onChangePassword = { email, old, next -> handleChangePassword(email, old, next) },
                                onDeleteAccount = { email, pass -> handleDeleteAccount(email, pass) }
                            )
                            "saved" -> SavedScreen(
                                onBackClick = { navigateTo("home") }, savedRoommates = roommates.filter { it.isSaved }, savedRooms = rooms.filter { it.isSaved }, 
                                favoriteItems = favoriteItems, isLoading = isFavoritesLoading,
                                onRoommateClick = { _ -> navigateTo("profile_detail") },
                                onFavoriteClick = { f -> selectedTargetEmail = f.email; navigateTo("profile_detail") },
                                onRoomClick = { r -> selectedRoomId = r.id; navigateTo("available_room_detail") }
                            )
                            "ai_assistant" -> AIAssistantScreen(
                                email = sessionEmail,
                                onBackClick = { navigateTo("home") },
                                onRoommateClick = { roommate ->
                                    selectedTargetEmail = roommate.email
                                    navigateTo("profile_detail")
                                },
                                checkSearchLimit = {
                                    if (sessionManager.isPremiumUser()) true
                                    else {
                                        val count = sessionManager.getAiUsageCount()
                                        if (count >= 5) {
                                            startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                                            Toast.makeText(this@MainActivity, "Daily AI limit reached. Please upgrade to Premium!", Toast.LENGTH_LONG).show()
                                            false
                                        } else {
                                            sessionManager.incrementAiUsage()
                                            true
                                        }
                                    }
                                },
                                onResultClick = { result ->
                                    // Gson deserializes JSON int as Double (3.0), so strip trailing ".0"
                                    val cleanId = when (val raw = result.id) {
                                        is Double -> raw.toLong().toString()
                                        is Float -> raw.toLong().toString()
                                        is Number -> raw.toLong().toString()
                                        else -> raw.toString()
                                    }
                                    if (result.category == "room") {
                                        selectedRoomId = cleanId
                                        navigateTo("available_room_detail")
                                    } else if (result.category == "hotel" || result.category == "external") {
                                        fetchHotelDetail(cleanId)
                                    } else {
                                        selectedHotelId = cleanId
                                        navigateTo("hotel_detail")
                                    }
                                },
                                onNavigateClick = { address ->
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${Uri.encode(address)}"))
                                    )
                                }
                            )
                            "booking_history" -> BookingHistoryScreen(
                                bookings = bookingHistory,
                                isLoading = isBookingHistoryLoading,
                                onBackClick = { navigateTo("profile") },
                                onBookingClick = { 
                                    activeBookingHistoryItem = it
                                    navigateTo("booking_detail")
                                }
                            )
                            "booking_detail" -> BookingDetailScreen(
                                booking = activeBookingHistoryItem,
                                onBackClick = { navigateTo("booking_history") }
                            )
                            "privacy_security" -> PrivacySecurityScreen(onBackClick = { navigateTo("profile") })

                            
                            "hotel_list" -> HotelListScreen(
                                onMenuClick = { openDrawer() },
                                hotels = hotelList,
                                recommended = recommendedHotels,
                                userAreas = hotelUserAreas,
                                isLoading = isHotelListLoading,
                                isNavigating = isHotelDetailLoading,
                                onSearchClick = { navigateTo("hotel_list") },
                                onHotelClick = { h -> fetchHotelDetail(h.id) },
                                onToggleFavorite = { id -> handleToggleHotelFavorite(id) },
                                onBackClick = { navigateTo("home") }
                            )

                            "hotel_detail" -> HotelDetailScreen(
                                hotelDetail = activeHotelDetail,
                                isLoading = isHotelDetailLoading,
                                isBooking = isBookingLoading,
                                activeBookingRoomId = activeBookingRoomId,
                                onBackClick = { navigateTo("hotel_list") },
                                onBookRoom = { details -> handleBookHotelRoom(details) }
                            )

                            "ai_chatbot" -> AIChatbotScreen(
                                email = sessionEmail,
                                onBackClick = { navigateTo("profile") },
                                messages = aiChatbotMessages,
                                suggestions = aiChatbotSuggestions,
                                isLoadingHistory = isAiChatbotLoading,
                                isSending = isAiChatbotSending,
                                onSendMessage = { text -> handleSendChatbotMessage(text) },
                                onClearChat = { handleClearChatbotHistory() }
                            )

                                "booking_success" -> BookingSuccessScreen(
                                    data = activeBookingConfirmation,
                                    onBackToHome = { navigateTo("home") }
                                )

                                else -> Box(modifier = Modifier.fillMaxSize()) { Text("Screen $currentScreen not found") }
                        }
                    }
                }
                
                // --- GLOBAL NAVIGATION & ACTION LOADING OVERLAY ---
                if (isInboxLoading || isHotelDetailLoading || isHomeRoomDetailLoading || isRoomShareFormLoading || isRoomShareRequestDetailLoading || isSendRoomShareRequestLoading) {
                    FullScreenLoadingOverlay()
                }
            }
        }
    }
}
}

    private fun startRazorpayPayment(amountPaise: String, description: String) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_SWKknvSDD1VSWN")
        try {
            val options = JSONObject()
            options.put("name", "RoomShare AI")
            options.put("description", description)
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#1E63FF")
            options.put("currency", "INR")
            options.put("amount", amountPaise)
            
            val prefill = JSONObject()
            prefill.put("email", sessionManager.fetchUserEmail() ?: "user@roomshare.ai")
            options.put("prefill", prefill)
            
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Successful: $razorpayPaymentId", Toast.LENGTH_SHORT).show()
        val type = pendingPaymentType ?: return
        val data = pendingPaymentData ?: return
        
        lifecycleScope.launch {
            try {
                when (type) {
                    "hotel_booking" -> {
                        val req = data as HotelRoomBookingRequest
                        val res = RetrofitClient.instance.bookHotelRoom(req)
                        if (res.isSuccessful && (res.body()?.success == true || res.body()?.success == null)) {
                            Toast.makeText(this@MainActivity, "Hotel Booking Confirmed!", Toast.LENGTH_LONG).show()
                            isBookingLoadingState.value = false
                            activeBookingRoomIdState.value = null
                            currentScreenState.value = "booking_history"
                        } else {
                            isBookingLoadingState.value = false
                            activeBookingRoomIdState.value = null
                            val errorBody = res.errorBody()?.string()
                            Toast.makeText(this@MainActivity, "Booking failed: $errorBody", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Booking error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                pendingPaymentType = null
                pendingPaymentData = null
            }
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed ($code): $response", Toast.LENGTH_SHORT).show()
        pendingPaymentType = null
        pendingPaymentData = null
    }
}
