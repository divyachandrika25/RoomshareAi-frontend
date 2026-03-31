import { Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { SidebarProvider, SidebarTrigger, SidebarInset } from "@/components/ui/sidebar";
import { AppSidebar, MobileTopBar } from "@/components/AppSidebar";
import { 
  Bell, Heart, Sparkles, User, LogOut, 
  Search, Settings, MessageSquare, ChevronRight, Menu ,Star
} from 'lucide-react';
import { useState, useEffect } from 'react';
import { Toaster } from 'react-hot-toast';
import { cn } from "@/lib/utils";
import { notificationCountAPI } from '@/lib/api';

export default function DashboardLayout() {
  const { user, logout, isPremium } = useAuth();
  const navigate = useNavigate();
  const [showLogout, setShowLogout] = useState(false);
  const [notifCount, setNotifCount] = useState(0);

  useEffect(() => {
    if (user?.email) {
      const fetchCount = async () => {
        try {
          const res = await notificationCountAPI.get(user.email);
          setNotifCount(res.data.count || 0);
        } catch (e) {}
      };
      fetchCount();
      const interval = setInterval(fetchCount, 30000); // Refresh every 30s
      return () => clearInterval(interval);
    }
  }, [user]);

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <SidebarProvider className="min-h-screen bg-background">
      <AppSidebar />
      <SidebarInset className="bg-background">
        <MobileTopBar />
        {/* Professional Header (Desktop) */}
        <header className="sticky top-0 z-40 h-16 w-full border-b border-border/60 bg-card/80 backdrop-blur-md hidden md:block">
          <div className="flex h-full items-center justify-between px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
            <div className="flex items-center gap-4">
  
            </div>

            <div className="flex items-center gap-2 sm:gap-4">
              <div className="flex items-center gap-1 sm:gap-2 mr-2">
                <button onClick={() => navigate('/dashboard/ai-assistant')} className="p-2 rounded-xl bg-gradient-to-r from-indigo-600 to-violet-600 text-white hover:shadow-lg hover:shadow-indigo-500/20 transition-all flex items-center gap-2 group px-3" title="AI Assistant">
                  <Sparkles className="w-4 h-4 group-hover:rotate-12 transition-transform" />
                  <span className="text-xs font-bold hidden sm:inline">Ask AI</span>
                </button>
                <div className="w-px h-6 bg-border mx-1"></div>
                <button onClick={() => navigate('/dashboard/notifications')} className="p-2 rounded-xl hover:bg-muted text-muted-foreground transition-all relative">
                  <Bell className="w-5 h-5" />
                  {notifCount > 0 && (
                    <span className="absolute -top-0.5 -right-0.5 min-w-[18px] h-[18px] bg-destructive text-white text-[10px] font-bold rounded-full flex items-center justify-center px-1 border-2 border-background">
                      {notifCount > 99 ? '99+' : notifCount}
                    </span>
                  )}
                </button>
                <button onClick={() => navigate('/dashboard/messages')} className="p-2 rounded-xl hover:bg-muted text-muted-foreground transition-all relative">
                  <MessageSquare className="w-5 h-5" />
                </button>
              </div>

              <div className="relative">
                <button 
                  onClick={() => setShowLogout(!showLogout)} 
                  className="group flex items-center justify-center sm:gap-3 p-1 sm:px-2 sm:py-1.5 rounded-xl hover:bg-muted transition-all border border-transparent hover:border-border/60"
                >
                  <div className="w-9 h-9 sm:w-8 sm:h-8 rounded-full bg-gradient-to-br from-indigo-500 to-violet-500 flex items-center justify-center text-white font-bold text-sm shadow-md ring-2 ring-background group-hover:ring-primary/20 transition-all">
                    {(user?.fullName || user?.email || 'U')[0].toUpperCase()}
                  </div>
                  <div className="hidden sm:block text-left">
                    <p className="text-xs font-bold text-foreground leading-tight truncate max-w-[100px]">
                      {user?.fullName || user?.email?.split('@')[0] || 'User'}
                    </p>
                    <p className="text-[9px] text-muted-foreground font-semibold flex items-center gap-0.5">
                      {isPremium ? (
                        <span className="flex items-center gap-1 text-amber-500 font-black">
                          <Star className="w-2 h-2 fill-amber-500" /> PREMIUM
                        </span>
                      ) : (
                        <>Free Plan <ChevronRight className="w-2 h-2" /></>
                      )}
                    </p>
                  </div>
                </button>

                {showLogout && (
                  <>
                    <div className="fixed inset-0 z-40" onClick={() => setShowLogout(false)}></div>
                    <div className="absolute right-0 top-12 bg-card rounded-xl shadow-2xl border border-border py-3 w-56 z-50 animate-in fade-in slide-in-from-top-2 duration-300">
                      <div className="px-4 py-2 border-b border-border mb-2 pb-3">
                        <p className="text-xs font-bold text-foreground truncate">{user?.email}</p>
                        <p className="text-[10px] text-indigo-400 font-bold uppercase tracking-wider mt-0.5">Verified Account</p>
                      </div>
                      <button onClick={() => { navigate('/dashboard/profile'); setShowLogout(false); }} className="flex items-center gap-3 px-4 py-2.5 w-full text-left text-foreground hover:bg-muted text-sm font-semibold transition-colors">
                        <User className="w-4 h-4 text-muted-foreground" /> My Profile
                      </button>
                      <button onClick={() => { navigate('/dashboard/settings'); setShowLogout(false); }} className="flex items-center gap-3 px-4 py-2.5 w-full text-left text-foreground hover:bg-muted text-sm font-semibold transition-colors">
                        <Settings className="w-4 h-4 text-muted-foreground" /> Settings
                      </button>
                      <div className="h-px bg-border my-2"></div>
                      <button onClick={handleLogout} className="flex items-center gap-3 px-4 py-2.5 w-full text-left text-red-400 hover:bg-red-500/10 text-sm font-bold transition-colors">
                        <LogOut className="w-4 h-4" /> Sign Out
                      </button>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        </header>

        {/* Dynamic Main Content Container */}
        <main className="flex-1 w-full flex flex-col items-center">
          <div className="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-in fade-in duration-700">
            <Outlet />
          </div>
        </main>
      </SidebarInset>
    </SidebarProvider>
  );
}
