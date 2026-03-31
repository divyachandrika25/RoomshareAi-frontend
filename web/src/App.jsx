import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { Toaster } from 'react-hot-toast';
import { TooltipProvider } from '@/components/ui/tooltip';

// Layouts
import DashboardLayout from './layouts/DashboardLayout';

// Pages
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import VerifyOtpPage from './pages/auth/VerifyOtpPage';
import ForgotPasswordPage from './pages/auth/ForgotPasswordPage';
import ResetPasswordPage from './pages/auth/ResetPasswordPage';

// Dashboard Pages
import HomePage from './pages/dashboard/HomePage';
import MatchesPage from './pages/dashboard/MatchesPage';
import MatchDetailPage from './pages/dashboard/MatchDetailPage';
import MessagesPage from './pages/dashboard/MessagesPage';
import ChatPage from './pages/dashboard/ChatPage';
import ProfilePage from './pages/dashboard/ProfilePage';
import NotificationsPage from './pages/dashboard/NotificationsPage';
import SavedPage from './pages/dashboard/SavedPage';
import AIAssistantPage from './pages/dashboard/AIAssistantPage';
import RoommateProfilePage from './pages/dashboard/RoommateProfilePage';
import RoomDetailPage from './pages/dashboard/RoomDetailPage';
import BookingHistoryPage from './pages/dashboard/BookingHistoryPage';
import SettingsPage from './pages/dashboard/SettingsPage';
import HotelListPage from './pages/dashboard/HotelListPage';
import HotelDetailPage from './pages/dashboard/HotelDetailPage';
import AIChatbotPage from './pages/dashboard/AIChatbotPage';
import PricingPage from './pages/dashboard/PricingPage';
import RoomShareFormPage from './pages/dashboard/RoomShareFormPage';
import RoomShareVerificationPage from './pages/dashboard/RoomShareVerificationPage';
import RoomShareFinalReviewPage from './pages/dashboard/RoomShareFinalReviewPage';

function ProtectedRoute({ children }) {
  const { token, loading } = useAuth();
  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-10 h-10 border-3 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin" />
    </div>
  );
  return token ? children : <Navigate to="/login" replace />;
}

function PublicRoute({ children }) {
  const { token, loading } = useAuth();
  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-[#f8f9fe]">
      <div className="w-10 h-10 border-3 border-[#1e63ff]/20 border-t-[#1e63ff] rounded-full animate-spin" />
    </div>
  );
  return token ? <Navigate to="/dashboard" replace /> : children;
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <TooltipProvider>
          <Toaster position="top-center" toastOptions={{
            style: { borderRadius: '16px', padding: '12px 20px', fontWeight: 600, fontSize: '14px', background: '#1e293b', color: '#e2e8f0', border: '1px solid #334155' },
            success: { style: { background: '#064e3b', color: '#6ee7b7', border: '1px solid #065f46' } },
            error: { style: { background: '#450a0a', color: '#fca5a5', border: '1px solid #7f1d1d' } },
          }} />
          <Routes>
            {/* Public */}
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
            <Route path="/register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
            <Route path="/verify-otp" element={<VerifyOtpPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />
            <Route path="/reset-password" element={<ResetPasswordPage />} />

            {/* Protected Dashboard */}
            <Route path="/dashboard" element={<ProtectedRoute><DashboardLayout /></ProtectedRoute>}>
              <Route index element={<HomePage />} />
              <Route path="matches" element={<MatchesPage />} />
              <Route path="match/:matchId" element={<MatchDetailPage />} />
              <Route path="messages" element={<MessagesPage />} />
              <Route path="chat/:type/:chatId" element={<ChatPage />} />
              <Route path="profile" element={<ProfilePage />} />
              <Route path="notifications" element={<NotificationsPage />} />
              <Route path="saved" element={<SavedPage />} />
              <Route path="ai-assistant" element={<AIAssistantPage />} />
              <Route path="roommate/:targetEmail" element={<RoommateProfilePage />} />
              <Route path="room/:roomId" element={<RoomDetailPage />} />
              <Route path="booking-history" element={<BookingHistoryPage />} />
              <Route path="settings" element={<SettingsPage />} />
              <Route path="privacy" element={<SettingsPage />} />
              <Route path="hotels" element={<HotelListPage />} />
              <Route path="hotel/:hotelId" element={<HotelDetailPage />} />
              <Route path="ai-chatbot" element={<AIChatbotPage />} />
              <Route path="pricing" element={<PricingPage />} />
              <Route path="room-share/form/:roomId" element={<RoomShareFormPage />} />
              <Route path="room-share/verify/:requestId" element={<RoomShareVerificationPage />} />
              <Route path="room-share/final-review/:requestId" element={<RoomShareFinalReviewPage />} />
            </Route>

            {/* Catch-all */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </TooltipProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
