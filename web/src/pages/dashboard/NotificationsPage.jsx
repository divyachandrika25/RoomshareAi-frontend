import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { notificationAPI } from '@/lib/api';
import {
  Bell, Users, Calendar, Home, MessageCircle,
  CheckCircle, ChevronRight, BellOff, Sparkles,
  Info, AlertTriangle, ShieldCheck, Clock, Check,
  MoreVertical
} from 'lucide-react';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Section header ────────────────────────────────────────────────────────────
function SectionHeader({ icon: Icon, iconBg, iconColor, title, subtitle, action }) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-3">
      <div className="flex items-center gap-3">
        <div className={cn("w-9 h-9 rounded-xl flex items-center justify-center shrink-0", iconBg)}>
          <Icon className={cn("w-4 h-4", iconColor)} />
        </div>
        <div>
          <h2 className="text-lg sm:text-xl font-extrabold text-foreground tracking-tight leading-tight">
            {title}
          </h2>
          <p className="text-[10px] text-muted-foreground font-semibold uppercase tracking-wider">
            {subtitle}
          </p>
        </div>
      </div>
      {action}
    </div>
  );
}

export default function NotificationsPage() {
  const { user } = useAuth();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!email) return;
    notificationAPI.getAll(email)
      .then(res => { if (res.data?.notifications) setNotifications(res.data.notifications); })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [email]);

  const markRead = async (id) => {
    try {
      await notificationAPI.markRead(id);
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, is_read: true } : n));
    } catch {}
  };

  const markAllRead = async () => {
    const unreadIds = notifications.filter(n => !n.is_read).map(n => n.id);
    if (unreadIds.length === 0) return;
    try {
      await Promise.all(unreadIds.map(id => notificationAPI.markRead(id)));
      setNotifications(prev => prev.map(n => ({ ...n, is_read: true })));
      toast.success('All notifications marked as read');
    } catch {
      toast.error('Failed to mark all read');
    }
  };

  const iconForType = (type) => {
    switch (type) {
      case 'MATCH': return Users;
      case 'TOUR': return Calendar;
      case 'ROOM': return Home;
      case 'CHAT': return MessageCircle;
      case 'BOOKING': return ShieldCheck;
      case 'AI': return Sparkles;
      case 'SYSTEM': return Info;
      default: return Bell;
    }
  };

  const colorForType = (type, isRead) => {
    if (isRead) return "bg-muted text-muted-foreground border-border/50";
    switch (type) {
      case 'MATCH': return "bg-emerald-500/10 text-emerald-600 border-emerald-500/20";
      case 'TOUR': return "bg-violet-500/10 text-violet-600 border-violet-500/20";
      case 'ROOM': return "bg-amber-500/10 text-amber-600 border-amber-500/20";
      case 'CHAT': return "bg-blue-500/10 text-blue-600 border-blue-500/20";
      case 'AI': return "bg-pink-500/10 text-pink-600 border-pink-500/20";
      case 'BOOKING': return "bg-teal-500/10 text-teal-600 border-teal-500/20";
      default: return "bg-primary/10 text-primary border-primary/20";
    }
  };

  const timeAgo = (dateStr) => {
    try {
      const d = new Date(dateStr), now = new Date(), diff = (now - d) / 1000;
      if (diff < 60) return 'Just now';
      if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
      if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
      return `${Math.floor(diff / 86400)}d ago`;
    } catch { return ''; }
  };

  const unreadCount = notifications.filter(n => !n.is_read).length;

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header Section ─────────────────────────────────────────────────── */}
      <div className="mt-4 sm:mt-6">
        <SectionHeader
          icon={Bell}
          iconBg="bg-primary/8"
          iconColor="text-primary"
          title="Activity Feed"
          subtitle={`${unreadCount} unread updates tracking your roommate matching status`}
          action={
            unreadCount > 0 && (
              <Button
                variant="outline"
                size="sm"
                onClick={markAllRead}
                className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1.5 border-border/60 hover:bg-primary/5 hover:text-primary transition-all"
              >
                <Check size={14} /> Mark all read
              </Button>
            )
          }
        />
      </div>

      {/* ── Notifications List ────────────────────────────────────────────── */}
      <div className="space-y-4">
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 gap-3">
            <div className="w-10 h-10 rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
            <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">
              Syncing Updates…
            </p>
          </div>
        ) : notifications.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 px-4 text-center space-y-4 rounded-2xl border border-border/50 bg-card">
            <div className="w-12 h-12 rounded-2xl bg-muted flex items-center justify-center text-muted-foreground/30">
              <BellOff size={24} />
            </div>
            <div>
              <p className="text-sm font-extrabold text-foreground">All caught up!</p>
              <p className="text-xs text-muted-foreground mt-0.5 max-w-xs mx-auto">
                No new notifications yet. We'll alert you when there's an update.
              </p>
            </div>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-3">
            {notifications.map((notif, idx) => {
              const Icon = iconForType(notif.notification_type);
              const isUnread = !notif.is_read;
              return (
                <div
                  key={notif.id || idx}
                  onClick={() => markRead(notif.id)}
                  className={cn(
                    "group relative bg-card hover:bg-muted/10 border border-border/50 hover:border-primary/25 rounded-2xl p-4 sm:p-5 flex items-start gap-4 cursor-pointer transition-all duration-200 hover:shadow-md hover:shadow-primary/5",
                    !isUnread && "opacity-75 grayscale-[0.3]"
                  )}
                >
                  {/* Unread indicator */}
                  {isUnread && (
                    <div className="absolute left-0 top-1/2 -translate-y-1/2 w-1.5 h-8 bg-primary rounded-r-full shadow-lg shadow-primary/20" />
                  )}

                  {/* Icon Box */}
                  <div className={cn(
                    "w-12 h-12 sm:w-14 sm:h-14 rounded-2xl flex items-center justify-center shrink-0 border border-border/10 shadow-sm transition-transform group-hover:scale-105",
                    colorForType(notif.notification_type, !isUnread)
                  )}>
                    <Icon size={24} className="sm:size-26" />
                  </div>

                  {/* Content */}
                  <div className="flex-1 min-w-0 pt-0.5">
                    <div className="flex items-center justify-between gap-3 mb-1">
                      <div className="flex items-center gap-2">
                        <span className="text-[9px] font-extrabold uppercase tracking-widest text-primary/70 bg-primary/8 px-1.5 py-0.5 rounded-md">
                          {notif.notification_type || 'UPDATE'}
                        </span>
                        <span className="text-[10px] font-extrabold text-muted-foreground uppercase flex items-center gap-1 shrink-0">
                          <Clock size={10} /> {timeAgo(notif.created_at)}
                        </span>
                      </div>
                      {isUnread && (
                        <div className="w-2 h-2 rounded-full bg-primary" />
                      )}
                    </div>

                    <h3 className={cn(
                      "font-extrabold text-foreground tracking-tight leading-tight mb-1 group-hover:text-primary transition-colors",
                      isUnread ? "text-base" : "text-sm sm:text-base opacity-80"
                    )}>
                      {notif.title}
                    </h3>
                    <p className="text-xs sm:text-sm text-muted-foreground font-medium leading-relaxed line-clamp-2">
                      {notif.message}
                    </p>
                  </div>

                  <div className="shrink-0 group-hover:translate-x-1 transition-transform opacity-0 group-hover:opacity-100 hidden sm:block self-center">
                    <div className="w-8 h-8 rounded-xl bg-primary/8 flex items-center justify-center">
                      <ChevronRight size={14} className="text-primary" />
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      {/* ── AI/Safety Banner ────────────────────────────────────────────────── */}
      <section
        className="rounded-3xl border border-primary/20 bg-[#0d1e3c] p-6 sm:p-10 flex flex-col md:flex-row items-center gap-8 relative overflow-hidden text-white"
      >
        <div className="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10" />
        <div className="absolute -bottom-20 -right-20 w-80 h-80 bg-white/5 rounded-full blur-[100px]" />

        <div className="w-20 h-20 rounded-2xl bg-white/10 backdrop-blur-md flex items-center justify-center border border-white/20 shrink-0 group-hover:rotate-6 transition-transform relative z-10">
          <ShieldCheck className="w-10 h-10 text-white" />
        </div>

        <div className="flex-1 text-center md:text-left relative z-10">
          <h2 className="text-2xl sm:text-3xl font-extrabold mb-3 tracking-tight">Stay Safe & Private</h2>
          <p className="text-blue-100/70 text-sm sm:text-base font-medium max-w-xl leading-relaxed">
            Always use RoomShare's secure chat to communicate before sharing personal details or making payments.
            Your safety is our priority.
          </p>
        </div>

        <Button
          size="lg"
          className="bg-white text-primary hover:bg-white/90 rounded-2xl font-extrabold h-12 px-8 shrink-0 shadow-2xl shadow-black/20 text-sm relative z-10"
        >
          Safety Hub
        </Button>
      </section>
    </div>
  );
}
