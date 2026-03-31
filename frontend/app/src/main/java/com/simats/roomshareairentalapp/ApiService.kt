package com.simats.roomshareairentalapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("logout/")
    suspend fun logout(): Response<CommonResponse>

    @POST("send-otp/")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<CommonResponse>

    @POST("verify-otp/")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<CommonResponse>

    @POST("forgot-password/")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<CommonResponse>

    @POST("reset-password/")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<CommonResponse>

    @POST("lifestyle/")
    suspend fun lifestyle(@Body request: LifestyleRequest): Response<LifestyleResponse>

    @POST("budget-location/")
    suspend fun budgetLocation(@Body request: BudgetLocationRequest): Response<BudgetLocationResponse>

    @GET("matches/{email}/")
    suspend fun getMatches(@Path("email") email: String, @Query("location") location: String? = null): Response<MatchListResponse>

    @GET("match-detail/{match_id}/")
    suspend fun getMatchDetail(@Path("match_id") matchId: String): Response<MatchDetailResponse>

    @GET("profile-dashboard/{email}/")
    suspend fun getProfileDashboard(@Path("email") email: String): Response<ProfileDashboardResponse>

    @Multipart
    @POST("profile-update/")
    suspend fun updateProfile(
        @Part("email") email: RequestBody,
        @Part("full_name") fullName: RequestBody,
        @Part("age") age: RequestBody,
        @Part("room_status") roomStatus: RequestBody,
        @Part("about_me") aboutMe: RequestBody,
        @Part("occupation") occupation: RequestBody,
        @Part("target_area") targetArea: RequestBody,
        @Part("budget_range") budgetRange: RequestBody,
        @Part("move_in_date") moveInDate: RequestBody,
        @Part photo: MultipartBody.Part? = null
    ): Response<UserProfileResponse>

    @Multipart
    @POST("profile-photo-upload/")
    suspend fun uploadProfilePhoto(
        @Part("email") email: RequestBody,
        @Part("source") source: RequestBody?,
        @Part photo: MultipartBody.Part
    ): Response<UserProfileResponse>

    @POST("save-favorite/")
    suspend fun saveFavorite(@Body request: SaveFavoriteRequest): Response<CommonResponse>

    @GET("favorites/{email}/")
    suspend fun getFavorites(@Path("email") email: String): Response<FavoriteListResponse>

    @GET("discover-roommates/{email}/")
    suspend fun discoverRoommates(
        @Path("email") email: String,
        @Query("search") search: String? = null
    ): Response<DiscoverRoommateResponse>

    @GET("roommate-profile/{current_email}/{target_email}/")
    suspend fun getRoommateProfileDetail(
        @Path("current_email") currentEmail: String,
        @Path("target_email") targetEmail: String
    ): Response<RoommateProfileDetailResponse>

    @GET("ai-compatibility/{current_email}/{target_email}/")
    suspend fun getAICompatibility(
        @Path("current_email") currentEmail: String,
        @Path("target_email") targetEmail: String
    ): Response<AICompatibilityResponse>



    @POST("direct-chat/create/")
    suspend fun createDirectChat(@Body request: CreateDirectChatRequest): Response<CreateDirectChatResponse>

    @GET("direct-chat/{chat_id}/{email}/")
    suspend fun getDirectChatDetail(
        @Path("chat_id") chatId: String,
        @Path("email") email: String
    ): Response<DirectChatData>

    @POST("direct-chat/send-message/")
    suspend fun sendDirectChatMessage(@Body request: SendDirectChatMessageRequest): Response<CreateDirectChatResponse>

    @GET("messages/{email}/")
    suspend fun getMessagesInbox(
        @Path("email") email: String,
        @Query("search") search: String? = null
    ): Response<MessagesInboxResponse>

    @Multipart
    @POST("listed-room/")
    suspend fun createOrUpdateListedRoom(
        @Part("email") email: RequestBody,
        @Part("apartment_title") title: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("monthly_rent") rent: RequestBody,
        @Part("description") description: RequestBody,
        @Part("status") status: RequestBody?,
        @Part("bathroom_type") bathroomType: RequestBody?,
        @Part("roommate_count") roommateCount: RequestBody?,
        @Part("entry_type") entryType: RequestBody?,
        @Part photos: List<MultipartBody.Part>?
    ): Response<ListedRoomResponse>

    @GET("listed-room/{email}/")
    suspend fun getListedRoomDetail(@Path("email") email: String): Response<ListedRoomResponse>

    @GET("home-rooms/{email}/")
    suspend fun getHomeRooms(
        @Path("email") email: String,
        @Query("search") search: String? = null
    ): Response<HomeRoomListResponse>

    @GET("home-room-detail/{room_id}/{email}/")
    suspend fun getHomeRoomDetail(
        @Path("room_id") roomId: String,
        @Path("email") email: String
    ): Response<HomeRoomDetailResponse>

    @GET("room-share-form/{room_id}/{email}/")
    suspend fun getRoomShareForm(
        @Path("room_id") roomId: String,
        @Path("email") email: String
    ): Response<RoomShareFormResponse>

    @POST("submit-room-share-request/")
    suspend fun submitRoomShareRequest(@Body request: RoomShareSubmitRequest): Response<RoomShareSubmitResponse>

    @GET("room-share-request/{request_id}/")
    suspend fun getRoomShareRequestDetail(@Path("request_id") requestId: String): Response<RoomShareRequestDetailResponse>

    @GET("room-share-request-sent/{request_id}/")
    suspend fun getRoomShareRequestSent(@Path("request_id") requestId: String): Response<SendRoomShareRequestResponse>

    @PATCH("room-share-request/{request_id}/")
    suspend fun updateRoomShareStatus(
        @Path("request_id") requestId: String,
        @Body request: UpdateRoomShareStatusRequest
    ): Response<RoomShareRequestDetailResponse>

    @GET("room-share-verification/{request_id}/")
    suspend fun getRoomShareVerification(@Path("request_id") requestId: String): Response<RoomShareVerificationResponse>

    @Multipart
    @POST("upload-identity-document/")
    suspend fun uploadIdentityDocument(
        @Part("request_id") requestId: RequestBody,
        @Part("source") source: RequestBody,
        @Part("identity_document") identityDocument: MultipartBody.Part
    ): Response<RoomShareVerificationResponse>

    @POST("room-share/request/")
    suspend fun sendRoomShareRequest(@Body request: Map<String, String>): Response<SendRoomShareRequestResponse>

    @GET("room-share-final-review/{request_id}/")
    suspend fun getRoomShareFinalReview(@Path("request_id") requestId: String): Response<RoomShareFinalReviewResponse>



    @GET("notifications/{email}/")
    suspend fun getNotifications(@Path("email") email: String): Response<NotificationListResponse>

    @POST("notifications/mark-read/")
    suspend fun markNotificationRead(@Body request: MarkNotificationReadRequest): Response<CommonResponse>

    @POST("notifications/mark-all-read/")
    suspend fun markAllNotificationsRead(@Body request: Map<String, String>): Response<CommonResponse>

    @GET("account-settings/{email}/")
    suspend fun getAccountSettings(@Path("email") email: String): Response<AccountSettingsDetailResponse>

    @POST("account-settings/")
    suspend fun updateAccountSettings(@Body request: UpdateAccountSettingsRequest): Response<AccountSettingsDetailResponse>

    @GET("room-booking-detail/{booking_id}/")
    suspend fun getRoomBookingDetail(@Path("booking_id") bookingId: String): Response<RoomBookingDetailData>



    @GET("booking-history/{email}/")
    suspend fun getBookingHistory(@Path("email") email: String): Response<BookingHistoryResponse>

    @POST("change-email/")
    suspend fun changeEmail(@Body request: ChangeEmailRequest): Response<ChangeEmailResponse>

    @POST("change-password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<CommonResponse>

    @POST("delete-account/")
    suspend fun deleteAccount(@Body request: DeleteAccountRequest): Response<CommonResponse>

    @POST("ai-agent/location/")
    suspend fun aiLocationAgent(@Body request: AILocationAgentRequest): Response<AILocationAgentResponse>

    // AI Chatbot
    @GET("chatbot/{email}/")
    suspend fun getChatbotHistory(@Path("email") email: String): Response<AIChatbotHistoryResponse>

    @POST("chatbot/{email}/")
    suspend fun sendChatbotMessage(
        @Path("email") email: String,
        @Body request: Map<String, String>
    ): Response<AIChatbotSendResponse>

    @DELETE("chatbot/{email}/")
    suspend fun clearChatbotHistory(@Path("email") email: String): Response<CommonResponse>

    // Hotels
    @GET("hotels/")
    suspend fun getHotels(
        @Query("email") email: String,
        @Query("search") search: String? = null
    ): Response<HotelListResponse>

    @GET("hotels/{hotel_id}/")
    suspend fun getHotelDetail(@Path("hotel_id") hotelId: String): Response<HotelDetailResponse>

    @POST("hotels/toggle-favorite/")
    suspend fun toggleHotelFavorite(@Body request: Map<String, Any>): Response<CommonResponse>

    @POST("hotel-booking/")
    suspend fun bookHotelRoom(@Body request: HotelRoomBookingRequest): Response<HotelRoomBookingResponse>

    @POST("rooms/toggle-favorite/")
    suspend fun toggleRoomFavorite(@Body request: Map<String, String>): Response<CommonResponse>

    @POST("update-subscription/")
    suspend fun updateSubscription(@Body request: SubscriptionUpdateRequest): Response<CommonResponse>
}
