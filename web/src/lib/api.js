import axios from 'axios';

const API_BASE = 'http://localhost:8000/api';

const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

// Auto-attach token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token');
  if (token) config.headers.Authorization = `Token ${token}`;
  return config;
});

// ─── Auth ───
export const authAPI = {
  register: (data) => api.post('/register/', data),
  login: (data) => api.post('/login/', data),
  logout: () => api.post('/logout/'),
  sendOtp: (email) => api.post('/send-otp/', { email }),
  verifyOtp: (email, otp) => api.post('/verify-otp/', { email, otp }),
  forgotPassword: (email) => api.post('/forgot-password/', { email }),
  resetPassword: (data) => api.post('/reset-password/', data),
};

// ─── Onboarding ───
export const onboardingAPI = {
  lifestyle: (data) => api.post('/lifestyle/', data),
  budgetLocation: (data) => api.post('/budget-location/', data),
};

// ─── Profile ───
export const profileAPI = {
  getDashboard: (email) => api.get(`/profile-dashboard/${email}/`),
  update: (formData) => api.post('/profile-update/', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }),
  uploadPhoto: (formData) => api.post('/profile-photo-upload/', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }),
  getAccountSettings: (email) => api.get(`/account-settings/${email}/`),
  updateAccountSettings: (data) => api.post('/account-settings/', data),
  changeEmail: (data) => api.post('/change-email/', data),
  changePassword: (data) => api.post('/change-password/', data),
  deleteAccount: (data) => api.post('/delete-account/', data),
  updateSubscription: (data) => api.post('/update-subscription/', data),
  createPaymentIntent: (data) => api.post('/create-payment-intent/', data),
};

// ─── Discover / Matching ───
export const matchAPI = {
  getMatches: (email) => api.get(`/matches/${email}/`),
  getMatchDetail: (id) => api.get(`/match-detail/${id}/`),
  discoverRoommates: (email, search) =>
    api.get(`/discover-roommates/${email}/`, { params: { search } }),
  getRoommateProfile: (currentEmail, targetEmail) =>
    api.get(`/roommate-profile/${currentEmail}/${targetEmail}/`),
  getAICompatibility: (currentEmail, targetEmail) =>
    api.get(`/ai-compatibility/${currentEmail}/${targetEmail}/`),
  requestRoomshare: (userEmail, targetEmail) =>
    api.post('/room-share/request/', { user_email: userEmail, target_email: targetEmail }),
};

// ─── Favorites ───
export const favoriteAPI = {
  save: (userEmail, matchedUserEmail) =>
    api.post('/save-favorite/', { user_email: userEmail, matched_user_email: matchedUserEmail }),
  getAll: (email) => api.get(`/favorites/${email}/`),
};

// ─── Rooms ───
export const roomAPI = {
  getHomeRooms: (email, search) =>
    api.get(`/home-rooms/${email}/`, { params: { search } }),
  getHomeRoomDetail: (roomId, email) =>
    api.get(`/home-room-detail/${roomId}/${email}/`),
  createOrUpdateListed: (formData) =>
    api.post('/listed-room/', formData, { headers: { 'Content-Type': 'multipart/form-data' } }),
  getListedRoom: (email) => api.get(`/listed-room/${email}/`),
  getRoomShareForm: (roomId, email) =>
    api.get(`/room-share-form/${roomId}/${email}/`),
  submitRoomShareRequest: (data) => api.post('/submit-room-share-request/', data),
  getRoomShareRequestDetail: (id) => api.get(`/room-share-request/${id}/`),
  getRoomShareRequestSent: (id) => api.get(`/room-share-request-sent/${id}/`),
  updateRoomShareStatus: (id, status) =>
    api.patch(`/room-share-request/${id}/`, { status }),
  getRoomShareVerification: (id) => api.get(`/room-share-verification/${id}/`),
  uploadIdentityDocument: (formData) =>
    api.post('/upload-identity-document/', formData, { headers: { 'Content-Type': 'multipart/form-data' } }),
  sendRoomShareRequest: (data) => api.post('/send-room-share-request/', data),
  getRoomShareFinalReview: (id) => api.get(`/room-share-final-review/${id}/`),
  toggleFavorite: (email, room_id) => api.post('/rooms/toggle-favorite/', { email, room_id }),
  getFavorites: (email) => api.get(`/rooms/favorites/${email}/`),
};

// ─── Chat / Messages ───
export const chatAPI = {
  getMessagesInbox: (email, search) =>
    api.get(`/messages/${email}/`, { params: { search } }),
  createDirectChat: (userEmail, otherUserEmail) =>
    api.post('/direct-chat/create/', { user_email: userEmail, other_user_email: otherUserEmail }),
  getDirectChat: (chatId, email) => api.get(`/direct-chat/${chatId}/${email}/`),
  sendDirectMessage: (data) => api.post('/direct-chat/send-message/', data),
  startGroupChat: (userEmail) => api.post('/start-group-chat/', { user_email: userEmail }),
  sendGroupMessage: (data) => api.post('/group-chat/send-message/', data),
  shareRoomDetails: (data) => api.post('/group-chat/share-room-details/', data),
  toggleMute: (chatId, isMuted) => api.post('/group-chat/toggle-mute/', { chat_id: chatId, is_muted: isMuted }),
  getEmojis: () => api.get('/group-chat/emojis/'),
  getViewGroup: (email) => api.get(`/view-group/${email}/`),
};

// ─── Notifications ───
export const notificationAPI = {
  getAll: (email) => api.get(`/notifications/${email}/`),
  markRead: (notificationId) =>
    api.post('/notifications/mark-read/', { notification_id: notificationId }),
};

// ─── Tours ───
export const tourAPI = {
  schedule: (data) => api.post('/schedule-room-tour/', data),
  getDetail: (scheduleId) => api.get(`/room-tour/${scheduleId}/`),
};

// ─── Bookings ───
export const bookingAPI = {
  confirm: (data) => api.post('/confirm-room-booking/', data),
  getDetail: (bookingId) => api.get(`/room-booking/${bookingId}/`),
  getHistory: (email) => api.get(`/booking-history/${email}/`),
};

// ─── AI Agent ───
export const aiAPI = {
  locationAgent: (query, email) => api.post('/ai-agent/location/', { query, email }),
};

// ─── AI Chatbot ───
export const chatbotAPI = {
  getHistory: (email) => api.get(`/chatbot/${email}/`),
  sendMessage: (email, message) => api.post('/chatbot/', { email, message }),
  clearHistory: (email) => api.delete(`/chatbot/${email}/`),
};

// ─── Hotels & Rooms ───
export const hotelAPI = {
  getAll: (params) => api.get('/hotels/', { params }),
  getDetail: (hotelId, checkIn, checkOut) =>
    api.get(`/hotels/${hotelId}/`, { params: { check_in: checkIn, check_out: checkOut } }),
  bookRoom: (data) => api.post('/hotel-booking/', data),
  getMyBookings: (email) => api.get(`/my-hotel-bookings/${email}/`),
  toggleFavorite: (email, hotel_id) => api.post('/hotels/toggle-favorite/', { email, hotel_id }),
  getFavorites: (email) => api.get(`/hotels/favorites/${email}/`),
};

// ─── Recommendations ───
export const recommendationAPI = {
  get: (email) => api.get(`/recommendations/${email}/`),
};

// ─── Notification Count ───
export const notificationCountAPI = {
  get: (email) => api.get(`/notification-count/${email}/`),
};

export default api;
