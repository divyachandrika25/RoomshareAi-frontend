package com.simats.roomshareairentalapp

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName

data class Roommate(
    val id: String,
    val name: String,
    val email: String = "member@roomshare.com",
    val age: Int,
    val location: String,
    val tags: List<String>,
    val price: String,
    val imageUrl: String,
    val matchPercentage: Int = 95,
    val status: String = "SEEKING ROOM",
    val isSaved: Boolean = false
)

data class Room(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val imageUrl: String,
    val amenities: List<String>,
    val matchPercentage: Int = 95,
    val matchedWithName: String = "Sarah",
    val matchedWithAvatar: String = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=100",
    val roommateCount: Int = 2,
    val bathType: String = "PRIVATE BATH",
    val isSaved: Boolean = false,
    val isAvailable: Boolean = true
)



data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val isRead: Boolean = false
)

data class ChatMessage(
    val senderName: String,
    val text: String,
    val isFromMe: Boolean,
    val time: String,
    val avatarUrl: String? = null,
    val isRoomAction: Boolean = false,
    val isImage: Boolean = false,
    val imageUrl: String? = null,
    val roomTitle: String? = null,
    val roomPrice: String? = null,
    val roomBeds: String? = null,
    val roomBaths: String? = null
)

// For backward compatibility while migrating
typealias DirectMessage = ChatMessage

data class RoomAttribute(val icon: ImageVector, val label: String, val value: String)

data class PotentialRoommate(
    val id: String,
    val name: String,
    val role: String,
    val match: Int,
    val tags: List<String>,
    val bio: String,
    val avatar: String
)

// API Models
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("age") val age: Int,
    @SerializedName("occupation") val occupation: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirm_password") val confirmPassword: String
)

data class RegisterResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("error") val error: String?
)

data class SubscriptionUpdateRequest(
    @SerializedName("email") val email: String,
    @SerializedName("is_premium") val isPremium: Boolean
)
data class SubscriptionUpdateResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: UserData?,
    @SerializedName("next_screen") val nextScreen: String? = null,
    @SerializedName("error") val error: String? = null
)

data class UserData(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("occupation") val occupation: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: UserData?,
    @SerializedName("error") val error: String? = null
)

data class SendOtpRequest(
    @SerializedName("email") val email: String
)

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

data class VerifyOtpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String
)

data class CommonResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: String? = null
)

data class ResetPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("new_password") val newPassword: String
)

data class LifestyleRequest(
    @SerializedName("email") val email: String,
    @SerializedName("sleep_schedule") val sleepSchedule: String,
    @SerializedName("cleanliness") val cleanliness: String,
    @SerializedName("social_interaction") val socialInteraction: String
)

data class LifestyleResponse(
    val success: Boolean,
    val message: String,
    val data: LifestyleData?,
    val error: String? = null
)

data class LifestyleData(
    val id: Int,
    @SerializedName("sleep_schedule") val sleepSchedule: String,
    val cleanliness: String,
    @SerializedName("social_interaction") val socialInteraction: String
)

data class BudgetLocationRequest(
    val email: String,
    @SerializedName("monthly_budget") val monthlyBudget: String,
    @SerializedName("preferred_city") val preferredCity: String
)

data class BudgetLocationResponse(
    val success: Boolean,
    val message: String,
    val data: BudgetLocationData?,
    val error: String? = null
)

data class BudgetLocationData(
    val id: Int,
    @SerializedName("monthly_budget") val monthlyBudget: String,
    @SerializedName("preferred_city") val preferredCity: String
)

data class UserProfileResponse(
    val success: Boolean,
    val message: String,
    val data: UserProfileData? = null,
    val error: String? = null
)

data class UserProfileData(
    @SerializedName("full_name") val fullName: String? = null,
    val age: String? = null,
    @SerializedName("room_status") val roomStatus: String? = null,
    @SerializedName("about_me") val aboutMe: String? = null,
    val occupation: String? = null,
    @SerializedName("target_area") val targetArea: String? = null,
    @SerializedName("budget_range") val budgetRange: String? = null,
    @SerializedName("move_in_date") val moveInDate: String? = null,
    val photo: String? = null,
    @SerializedName("is_premium") val isPremium: Boolean = false
)

data class MatchResult(
    val id: String,
    @SerializedName("matched_user") val matchedUser: UserData?,
    @SerializedName("compatibility_score") val compatibilityScore: Double,
    val reason: String? = null,
    @SerializedName("ai_explanation") val aiExplanation: String? = null,
    @SerializedName("request_status") val requestStatus: String? = null
)

data class MatchListResponse(
    val success: Boolean,
    val count: Int,
    val matches: List<MatchResult>,
    val error: String? = null
)

data class MatchDetailResponse(
    @SerializedName("match_id") val matchId: String,
    val email: String,
    @SerializedName("full_name") val fullName: String?,
    val age: String?,
    @SerializedName("room_status") val roomStatus: String?,
    val photo: String?,
    @SerializedName("sleep_schedule") val sleepSchedule: String?,
    val cleanliness: String?,
    @SerializedName("social_interaction") val socialInteraction: String?,
    @SerializedName("monthly_budget") val monthlyBudget: String?,
    @SerializedName("preferred_city") val preferredCity: String?,
    @SerializedName("compatibility_score") val compatibilityScore: Double,
    @SerializedName("ai_explanation") val aiExplanation: String?
)

data class ProfileDashboardResponse(
    val success: Boolean,
    val data: ProfileDashboardData?,
    val error: String? = null
)

data class ProfileDashboardData(
    val email: String,
    val profile: UserProfileData,
    @SerializedName("account_settings") val accountSettings: AccountSettingsData,
    @SerializedName("listed_room") val listedRoom: RoomData?
)

data class AccountSettingsData(
    val id: String,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean,
    @SerializedName("privacy_mode") val privacyMode: Boolean? = null,
    val language: String? = null,
    @SerializedName("privacy_settings") val privacySettings: String? = null
)

data class AccountSettingsDetailResponse(
    val success: Boolean,
    val data: AccountSettingsData,
    val message: String? = null
)

data class UpdateAccountSettingsRequest(
    val email: String,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean? = null,
    val language: String? = null,
    @SerializedName("privacy_settings") val privacySettings: String? = null
)

data class RoomData(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val photo: String?
)

data class SaveFavoriteRequest(
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("matched_user_email") val matchedUserEmail: String
)

data class FavoriteListResponse(
    val success: Boolean = true,
    val count: Int,
    @SerializedName("favorites") val data: List<FavoriteItem>
)

val FavoriteListResponse.favorites: List<FavoriteItem> get() = data

data class FavoriteItem(
    val email: String,
    val name: String?,
    val age: String?,
    @SerializedName("room_status") val roomStatus: String?,
    val photo: String?
)

data class DiscoverRoommateResponse(
    val success: Boolean,
    val count: Int,
    val roommates: List<DiscoverRoommate>
)

data class DiscoverRoommate(
    val id: String,
    val email: String,
    @SerializedName("full_name") val fullName: String?,
    val age: String?,
    @SerializedName("room_status") val roomStatus: String?,
    val city: String? = null,
    val tags: List<String>? = null,
    @SerializedName("match_percentage") val matchPercentage: Double? = null,
    val photos: List<ListedRoomPhotoData>? = null,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("request_status") val requestStatus: String? = null
) {
    val photo: String? get() = photos?.firstOrNull()?.image
}

data class RoommateProfileDetailResponse(
    val success: Boolean,
    val data: RoommateProfileDetail
)

data class RoommateProfileDetail(
    val email: String,
    @SerializedName("full_name") val fullName: String?,
    val age: String?,
    @SerializedName("room_status") val roomStatus: String?,
    val photo: String?,
    @SerializedName("about_me") val aboutMe: String?,
    val occupation: String?,
    @SerializedName("target_area") val targetArea: String?,
    val budgetRange: String?,
    @SerializedName("move_in_date") val moveInDate: String?,
    @SerializedName("sleep_schedule") val sleepSchedule: String?,
    val cleanliness: String?,
    @SerializedName("social_interaction") val socialInteraction: String?,
    @SerializedName("match_percentage") val matchPercentage: Double?,
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("request_status") val requestStatus: String? = null,
    @SerializedName("ai_compatibility_button_label") val aiCompatibilityButtonLabel: String? = "AI Compatibility",
    @SerializedName("message_button_label") val messageButtonLabel: String? = "Message"
)

data class AICompatibilityResponse(
    val success: Boolean,
    val data: AICompatibilityData
)

data class AICompatibilityData(
    @SerializedName("target_email") val targetEmail: String,
    @SerializedName("target_name") val targetName: String,
    @SerializedName("total_match") val totalMatch: Double,
    val headline: String,
    val breakdown: List<CompatibilityBreakdown>,
    @SerializedName("conflict_detection") val conflictDetection: ConflictDetection
)

data class CompatibilityBreakdown(
    val title: String,
    val score: Int,
    val note: String
)

data class ConflictDetection(
    val title: String,
    val message: String
)



data class CreateDirectChatRequest(
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("other_user_email") val otherUserEmail: String,
    @SerializedName("room_id") val roomId: String? = null
)

data class CreateDirectChatResponse(
    val success: Boolean,
    val message: String,
    val data: DirectChatData?
)

data class DirectChatData(
    @SerializedName("chat_id") val chatId: String?,
    @SerializedName("other_user_name") val otherUserName: String?,
    @SerializedName("other_user_photo") val otherUserPhoto: String?,
    val messages: List<ApiDirectChatMessage>?
)

data class ApiDirectChatMessage(
    val id: String?,
    @SerializedName("sender_name") val senderName: String?,
    val content: String?,
    @SerializedName("is_current_user") val isCurrentUser: Boolean,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("message_type") val messageType: String? = "TEXT",
    val image: String? = null,
    @SerializedName("room_title") val roomTitle: String? = null,
    @SerializedName("room_price") val roomPrice: String? = null,
    @SerializedName("room_beds") val roomBeds: String? = null,
    @SerializedName("room_baths") val roomBaths: String? = null
)

data class SendDirectChatMessageRequest(
    @SerializedName("chat_id") val chatId: String,
    @SerializedName("sender_email") val senderEmail: String,
    val message: String,
    @SerializedName("message_type") val messageType: String? = "TEXT",
    @SerializedName("room_title") val roomTitle: String? = null,
    @SerializedName("room_price") val roomPrice: String? = null,
    @SerializedName("room_beds") val roomBeds: String? = null,
    @SerializedName("room_baths") val roomBaths: String? = null
)

data class MessagesInboxResponse(
    val success: Boolean,
    val count: Int,
    val search: String?,
    val messages: List<InboxItem>
)

data class InboxItem(
    @SerializedName("conversation_type") val conversationType: String?,
    @SerializedName("conversation_id") val conversationId: String?,
    val title: String?,
    val subtitle: String?,
    val avatar: String?,
    val time: String?,
    @SerializedName("unread_count") val unreadCount: Int,
    @SerializedName("user_email") val userEmail: String? = null
)

data class ListedRoomResponse(
    val success: Boolean,
    val message: String,
    val data: ListedRoomData?
)

data class ListedRoomData(
    val id: String,
    @SerializedName("apartment_title") val apartmentTitle: String,
    val address: String,
    val city: String,
    @SerializedName("monthly_rent") val monthlyRent: String,
    val description: String,
    val status: String,
    @SerializedName("bathroom_type") val bathroomType: String,
    @SerializedName("roommate_count") val roommateCount: Int,
    @SerializedName("entry_type") val entryType: String,
    val photos: List<ListedRoomPhotoData>?
)

data class ListedRoomPhotoData(
    val id: String? = null,
    val image: String
)

data class HomeRoomListResponse(
    val success: Boolean,
    val count: Int,
    val rooms: List<HomeRoomItem>
)

data class HomeRoomItem(
    val id: String,
    @SerializedName("apartment_title") val apartmentTitle: String,
    val address: String?,
    val city: String?,
    @SerializedName("monthly_rent") val monthlyRent: String,
    val photos: List<ListedRoomPhotoData>? = null,
    @SerializedName("match_percentage") val matchPercentage: Int = 95,
    @SerializedName("owner_name") val ownerName: String?,
    @SerializedName("owner_photo") val ownerPhoto: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean = false
) {
    val photo: String? get() = photos?.firstOrNull()?.image
}

data class HomeRoomDetailResponse(
    val success: Boolean,
    val data: HomeRoomDetailData
)

data class HomeRoomDetailData(
    val id: String,
    @SerializedName("apartment_title") val apartmentTitle: String,
    val address: String?,
    val city: String?,
    @SerializedName("monthly_rent") val monthlyRent: String,
    val description: String?,
    val status: String?,
    @SerializedName("bathroom_type") val bathroomType: String?,
    @SerializedName("roommate_count") val roommateCount: Int,
    @SerializedName("entry_type") val entryType: String?,
    val photos: List<ListedRoomPhotoData>?,
    @SerializedName("match_percentage") val matchPercentage: Int = 95,
    @SerializedName("owner_name") val ownerName: String?,
    @SerializedName("owner_photo") val ownerPhoto: String?,
    @SerializedName("owner_email") val ownerEmail: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("potential_roommates") val potentialRoommates: List<PotentialRoommateInfo>?
)

data class PotentialRoommateInfo(
    val email: String,
    @SerializedName("full_name") val fullName: String?,
    val photo: String?,
    @SerializedName("match_percentage") val matchPercentage: Int
)

data class RoomShareFormResponse(
    val success: Boolean,
    val data: RoomShareFormData
)

data class RoomShareFormData(
    @SerializedName("room_id") val roomId: String,
    @SerializedName("room_title") val roomTitle: String,
    @SerializedName("owner_email") val ownerEmail: String,
    @SerializedName("owner_name") val ownerName: String,
    @SerializedName("owner_photo") val ownerPhoto: String?,
    @SerializedName("intro_quote") val introQuote: String,
    @SerializedName("duration_options") val durationOptions: List<String>,
    @SerializedName("employment_options") val employmentOptions: List<String>
)

data class RoomShareSubmitRequest(
    @SerializedName("room_id") val roomId: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("intro_message") val introMessage: String,
    @SerializedName("preferred_move_in_date") val preferredMoveInDate: String,
    @SerializedName("duration_of_stay") val durationOfStay: String,
    @SerializedName("employment_status") val employmentStatus: String
)

data class RoomShareSubmitResponse(
    val success: Boolean,
    val message: String,
    val data: RoomShareSubmitData?,
    val error: String? = null
)

data class RoomShareSubmitData(
    @SerializedName("request_id") val requestId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("room_title") val roomTitle: String,
    @SerializedName("intro_message") val introMessage: String,
    @SerializedName("preferred_move_in_date") val preferredMoveInDate: String,
    @SerializedName("duration_of_stay") val durationOfStay: String,
    @SerializedName("employment_status") val employmentStatus: String,
    val status: String
)

data class RoomShareRequestDetailResponse(
    val success: Boolean,
    val data: RoomShareRequestDetailData
)

data class RoomShareRequestDetailData(
    @SerializedName("request_id") val id: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("room_title") val roomTitle: String,
    @SerializedName("requester_email") val requesterEmail: String,
    @SerializedName("requester_name") val requesterName: String?,
    @SerializedName("requester_photo") val requesterPhoto: String?,
    @SerializedName("intro_message") val introMessage: String,
    @SerializedName("preferred_move_in_date") val preferredMoveInDate: String,
    @SerializedName("duration_of_stay") val durationOfStay: String,
    @SerializedName("employment_status") val employmentStatus: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String
)

data class UpdateRoomShareStatusRequest(
    val status: String
)

data class RoomShareVerificationResponse(
    val success: Boolean,
    val data: RoomShareVerificationData,
    val message: String? = null
)

data class RoomShareVerificationData(
    @SerializedName("request_id") val requestId: String,
    val title: String,
    val subtitle: String,
    @SerializedName("camera_enabled") val cameraEnabled: Boolean,
    @SerializedName("gallery_enabled") val galleryEnabled: Boolean,
    @SerializedName("verify_button_text") val verifyButtonText: String
)

data class RoomShareFinalReviewResponse(
    val success: Boolean,
    val data: RoomShareFinalReviewData
)

data class RoomShareFinalReviewData(
    @SerializedName("request_id") val id: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("room_title") val roomTitle: String,
    @SerializedName("owner_name") val ownerName: String,
    @SerializedName("owner_photo") val ownerPhoto: String?,
    @SerializedName("your_share_monthly") val yourShareMonthly: Double,
    @SerializedName("group_security_deposit") val groupSecurityDeposit: Double,
    @SerializedName("total_move_in") val totalMoveIn: Double,
    @SerializedName("preferred_move_in_date") val preferredMoveInDate: String,
    @SerializedName("duration_of_stay") val durationOfStay: String,
    @SerializedName("employment_status") val employmentStatus: String,
    val title: String,
    val subtitle: String,
    @SerializedName("button_text") val buttonText: String
)

data class SendRoomShareRequestResponse(
    val success: Boolean,
    val message: String? = null,
    val data: SendRoomShareRequestData?,
    val error: String? = null
)

data class SendRoomShareRequestData(
    @SerializedName("request_id") val requestId: String,
    @SerializedName("room_id") val roomId: String,
    val title: String,
    val subtitle: String,
    @SerializedName("back_button_text") val backButtonText: String,
    @SerializedName("message_owner_button_text") val messageOwnerButtonText: String,
    @SerializedName("owner_email") val ownerEmail: String,
    @SerializedName("room_title") val roomTitle: String,
    val status: String
)

data class RoomBookingConfirmationData(
    val bookingId: String,
    val roomTitle: String,
    val monthlyRent: String,
    val securityDeposit: String,
    val serviceFee: String,
    val totalDueNow: String,
    val paymentMethodLast4: String,
    val paymentStatus: String,
    val confirmationTitle: String,
    val confirmationMessage: String,
    val nextSteps: List<String>,
    val backButtonText: String
)

data class RoomBookingDetailData(
    @SerializedName("booking_id") val bookingId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("room_title") val roomTitle: String,
    @SerializedName("monthly_rent") val monthlyRent: String,
    @SerializedName("security_deposit") val securityDeposit: String,
    @SerializedName("service_fee") val serviceFee: String,
    @SerializedName("total_due_now") val totalDueNow: String,
    @SerializedName("payment_method_last4") val paymentMethodLast4: String,
    @SerializedName("payment_status") val paymentStatus: String,
    @SerializedName("created_at") val createdAt: String
)



data class NotificationListResponse(
    val success: Boolean,
    val count: Int,
    val notifications: List<ApiNotification>
)

data class ApiNotification(
    val id: String,
    val title: String,
    val message: String,
    @SerializedName("notification_type") val notificationType: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("related_id") val relatedId: String?,
    @SerializedName("is_read") val isRead: Boolean
)

data class MarkNotificationReadRequest(
    @SerializedName("notification_id") val notificationId: String
)



data class BookingHistoryResponse(
    val success: Boolean,
    val count: Int,
    val bookings: List<BookingHistoryItem>
)

data class BookingHistoryItem(
    val id: String,
    @SerializedName("room_title") val roomTitle: String,
    val location: String?,
    @SerializedName("created_at") val createdAt: String,
    val amount: String,
    val status: String,
    @SerializedName("is_hotel") val isHotel: Boolean = false
)

// Account Actions Requests
data class ChangeEmailRequest(
    @SerializedName("current_email") val currentEmail: String,
    @SerializedName("new_email") val newEmail: String
)

data class ChangeEmailResponse(
    val success: Boolean,
    val message: String,
    @SerializedName("new_email") val newEmail: String?,
    val error: String? = null
)

data class ChangePasswordRequest(
    val email: String,
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)

data class DeleteAccountRequest(
    val email: String,
    val password: String
)
data class AILocationAgentRequest(
    val query: String
)

data class AILocationAgentResponse(
    val success: Boolean,
    val text: String,
    val results: List<AILocationAgentResult>?,
    val roommates: List<DiscoverRoommate>?,
    val intent: String?,
    val location: String?,
    val budget: Double?,
    @SerializedName("ai_powered") val aiPowered: Boolean
)

data class AILocationAgentResult(
    val id: Any?, // Can be Int or String (for external)
    val title: String,
    val address: String,
    val city: String?,
    val price: String?,
    val type: String?,
    val category: String?,
    @SerializedName("is_local") val isLocal: Boolean,
    val source: String?,
    val stars: Int?,
    val status: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val dist_km: Double? = null
)

// AI Chatbot Models
data class AIChatbotHistoryResponse(
    val success: Boolean,
    val messages: List<AIChatbotMessage>,
    val suggestions: List<String>?
)

data class AIChatbotMessage(
    val role: String,
    val content: String
)

data class AIChatbotSendResponse(
    val success: Boolean,
    val response: String
)

// Hotel Models
data class HotelListResponse(
    val success: Boolean,
    val hotels: List<HotelItem>,
    val recommended: List<HotelItem>?,
    @SerializedName("user_areas") val userAreas: List<String>?
)

data class HotelItem(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val description: String?,
    @SerializedName("starting_price") val startingPrice: Double,
    val rating: Double?,
    val stars: Double?,
    @SerializedName("total_rooms") val totalRooms: Int,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("is_external") val isExternal: Boolean,
    val amenities: List<String>?,
    val reason: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean?
)

data class HotelDetailResponse(
    val success: Boolean,
    val hotel: HotelItem,
    val rooms: List<HotelRoomItem>?
)

data class HotelRoomItem(
    val id: String,
    @SerializedName("room_number") val roomNumber: String,
    @SerializedName("room_type") val roomType: String,
    val capacity: Int,
    @SerializedName("price_per_night") val pricePerNight: Double,
    val description: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val amenities: List<String>?,
    val available: Boolean
)

data class HotelRoomBookingRequest(
    @SerializedName("hotel_id") val hotelId: String,
    @SerializedName("room_id") val roomId: String,
    val email: String,
    @SerializedName("check_in") val checkIn: String,
    @SerializedName("check_out") val checkOut: String,
    val guests: Int,
    @SerializedName("total_price") val totalPrice: Double,
    @SerializedName("hotel_name") val hotelName: String? = null,
    @SerializedName("hotel_address") val hotelAddress: String? = null,
    @SerializedName("price") val price: Double? = null
)

data class HotelRoomBookingResponse(
    val success: Boolean,
    val message: String
)
