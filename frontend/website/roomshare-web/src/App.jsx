import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LandingScreen from './pages/LandingScreen';
import LoginScreen from './pages/LoginScreen';
import SignUpScreen from './pages/SignUpScreen';
import MainLayout from './layouts/MainLayout';
import HomeScreen from './pages/HomeScreen';
import MatchesScreen from './pages/MatchesScreen';
import MessagesScreen from './pages/MessagesScreen';
import ProfileScreen from './pages/ProfileScreen';
import ListRoomScreen from './pages/ListRoomScreen';
import AIAssistantScreen from './pages/AIAssistantScreen';
import SavedItemsScreen from './pages/SavedItemsScreen';
import NotificationsScreen from './pages/NotificationsScreen';
import ForgotPasswordScreen from './pages/ForgotPasswordScreen';
import ResetPasswordScreen from './pages/ResetPasswordScreen';
import EmailVerificationScreen from './pages/EmailVerificationScreen';
import OTPVerificationScreen from './pages/OTPVerificationScreen';
import PhoneVerificationScreen from './pages/PhoneVerificationScreen';
import IntroduceYourselfScreen from './pages/IntroduceYourselfScreen';
import LifestyleHabitsScreen from './pages/LifestyleHabitsScreen';
import AIProcessingScreen from './pages/AIProcessingScreen';
import AICompatibilityScreen from './pages/AICompatibilityScreen';
import AIResultsScreen from './pages/AIResultsScreen';
import ViewGroupScreen from './pages/ViewGroupScreen';
import ScheduleTourScreen from './pages/ScheduleTourScreen';
import TourSuccessScreen from './pages/TourSuccessScreen';
import RoomTourDetailScreen from './pages/RoomTourDetailScreen';
import BookingHistoryScreen from './pages/BookingHistoryScreen';
import BookingSuccessScreen from './pages/BookingSuccessScreen';
import RequestSentScreen from './pages/RequestSentScreen';
import FinalReviewScreen from './pages/FinalReviewScreen';
import AccountSettingsScreen from './pages/AccountSettingsScreen';
import PrivacySecurityScreen from './pages/PrivacySecurityScreen';
import ProfileDetailScreen from './pages/ProfileDetailScreen';
import EditProfileScreen from './pages/EditProfileScreen';
import DirectChatScreen from './pages/DirectChatScreen';
import RequestDetailScreen from './pages/RequestDetailScreen';
import BookingDetailsScreen from './pages/BookingDetailsScreen';
import SafetyVerificationScreen from './pages/SafetyVerificationScreen';
import RoomDetailScreen from './pages/RoomDetailScreen';
import BookRoomScreen from './pages/BookRoomScreen';
import { AppProvider } from './context/AppContext';

function App() {
  return (
    <AppProvider>
      <BrowserRouter>
      <div className="bg-white text-black min-h-screen selection:bg-primary-500/30">
        <Routes>
          {/* Public & Auth Routes */}
          <Route path="/" element={<LandingScreen />} />
          <Route path="/login" element={<LoginScreen />} />
          <Route path="/signup" element={<SignUpScreen />} />
          <Route path="/forgot-password" element={<ForgotPasswordScreen />} />
          <Route path="/reset-password" element={<ResetPasswordScreen />} />
          <Route path="/email-verify" element={<EmailVerificationScreen />} />
          <Route path="/phone-verify" element={<PhoneVerificationScreen />} />
          <Route path="/otp-verify" element={<OTPVerificationScreen />} />
          
          {/* Onboarding & AI Routes */}
          <Route path="/introduce-yourself" element={<IntroduceYourselfScreen />} />
          <Route path="/lifestyle-habits" element={<LifestyleHabitsScreen />} />
          <Route path="/ai-processing" element={<AIProcessingScreen />} />
          <Route path="/ai-results" element={<AIResultsScreen />} />
          <Route path="/ai-compatibility" element={<AICompatibilityScreen />} />
          
          {/* Room & Booking Utility Routes */}
          <Route path="/group/:id" element={<ViewGroupScreen />} />
          <Route path="/schedule-tour/:id" element={<ScheduleTourScreen />} />
          <Route path="/tour-success" element={<TourSuccessScreen />} />
          <Route path="/tour-details/:id" element={<RoomTourDetailScreen />} />
          <Route path="/booking-history" element={<BookingHistoryScreen />} />
          <Route path="/booking-success" element={<BookingSuccessScreen />} />
          <Route path="/booking-details" element={<BookingDetailsScreen />} />
          <Route path="/request-sent" element={<RequestSentScreen />} />
          <Route path="/final-review/:id" element={<FinalReviewScreen />} />
          <Route path="/request-detail/:id" element={<RequestDetailScreen />} />
          <Route path="/safety-verification" element={<SafetyVerificationScreen />} />
          <Route path="/list-room" element={<ListRoomScreen />} />
          
          {/* Profile & Settings Sub-routes */}
          <Route path="/account-settings" element={<AccountSettingsScreen />} />
          <Route path="/privacy-security" element={<PrivacySecurityScreen />} />
          <Route path="/profile-detail/:id" element={<ProfileDetailScreen />} />
          <Route path="/edit-profile" element={<EditProfileScreen />} />
          <Route path="/chat/:id" element={<DirectChatScreen />} />

          {/* Core App Tabs (Wrapped in MainLayout containing Navigation) */}
          <Route element={<MainLayout />}>
            <Route path="/home" element={<HomeScreen />} />
            <Route path="/matches" element={<MatchesScreen />} />
            <Route path="/saved-items" element={<SavedItemsScreen />} />
            <Route path="/messages" element={<MessagesScreen />} />
            <Route path="/profile" element={<ProfileScreen />} />
            <Route path="/ai-assistant" element={<AIAssistantScreen />} />
            <Route path="/notifications" element={<NotificationsScreen />} />
            <Route path="/room/:id" element={<RoomDetailScreen />} />
            <Route path="/book/:id" element={<BookRoomScreen />} />
          </Route>
        </Routes>
      </div>
    </BrowserRouter>
    </AppProvider>
  );
}

export default App;


