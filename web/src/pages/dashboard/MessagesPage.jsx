import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { chatAPI } from '@/lib/api';
import {
  MessageCircle, Search, ChevronRight, Users,
  MoreHorizontal, Plus, Filter, MessageSquareText,
  Clock, Sparkles, Building2, Info
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=200`;

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

export default function MessagesPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [inbox, setInbox] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');

  useEffect(() => {
    if (!email) return;
    chatAPI.getMessagesInbox(email)
      .then(res => { if (res.data?.messages) setInbox(res.data.messages); })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [email]);

  const filtered = search
    ? inbox.filter(i => (i.title || '').toLowerCase().includes(search.toLowerCase()))
    : inbox;

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header Section ─────────────────────────────────────────────────── */}
      <div className="mt-4 sm:mt-6">
        <SectionHeader
          icon={MessageCircle}
          iconBg="bg-primary/8"
          iconColor="text-primary"
          title="Inbox & Chats"
          subtitle={`${inbox.length} total conversations`}
          action={
            <div className="flex items-center gap-2">
              <div className="relative max-w-sm w-full sm:w-auto group">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground group-focus-within:text-primary transition-colors pointer-events-none" />
                <input
                  type="text"
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  placeholder="Search chats…"
                  className="w-full sm:w-64 pl-9 pr-4 py-2 rounded-xl bg-card border border-border/60 text-sm font-medium text-foreground placeholder:text-muted-foreground/60 focus:outline-none focus:border-primary/30 focus:ring-2 focus:ring-primary/10 transition-all"
                />
              </div>
              <Button size="sm" className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1.5 shadow-sm shadow-primary/15 shrink-0 hover:-translate-y-0.5 transition-transform">
                <Plus size={14} /> New Chat
              </Button>
            </div>
          }
        />
      </div>

      {/* ── Chat List ──────────────────────────────────────────────────────── */}
      <div className="space-y-4">
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 gap-3">
            <div className="w-10 h-10 rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
            <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">
              Syncing Conversations…
            </p>
          </div>
        ) : filtered.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 px-4 text-center space-y-4 rounded-2xl border border-border/50 bg-card">
            <div className="w-12 h-12 rounded-2xl bg-muted flex items-center justify-center text-muted-foreground/30">
              <MessageSquareText size={24} />
            </div>
            <div>
              <p className="text-sm font-extrabold text-foreground">No messages found</p>
              <p className="text-xs text-muted-foreground mt-0.5 max-w-xs mx-auto">
                {search ? "We couldn't find any chats matching your search." : "Your message history will appear here once you start matching with roommates."}
              </p>
            </div>
            <Button variant="outline" size="sm" onClick={() => search ? setSearch('') : navigate('/dashboard/matches')} className="h-9 px-5 rounded-xl font-extrabold text-xs">
              {search ? 'Clear Search' : 'Find Matches'}
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-3">
            {filtered.map((item, idx) => (
              <div
                key={`${item.conversation_type}-${item.conversation_id}-${idx}`}
                onClick={() => {
                  const path = item.conversation_type === 'direct'
                    ? `/dashboard/chat/direct/${item.conversation_id}`
                    : `/dashboard/chat/group/${item.conversation_id}`;
                  navigate(path);
                }}
                className="group relative bg-card hover:bg-muted/10 border border-border/50 hover:border-primary/25 rounded-2xl p-4 sm:p-5 flex items-center gap-4 cursor-pointer transition-all duration-200 hover:shadow-md hover:shadow-primary/5"
              >
                {/* Unread indicator */}
                {item.unread_count > 0 && (
                  <div className="absolute left-0 top-1/2 -translate-y-1/2 w-1.5 h-8 bg-primary rounded-r-full shadow-lg shadow-primary/20" />
                )}

                {/* Avatar */}
                <div className="relative shrink-0">
                  <img
                    src={item.avatar || avatarUrl(item.title)}
                    alt=""
                    className="w-12 h-12 sm:w-14 sm:h-14 rounded-2xl object-cover border-2 border-card shadow-sm group-hover:scale-105 transition-transform duration-500"
                  />
                  {item.conversation_type === 'group' && (
                    <div className="absolute -bottom-1 -right-1 w-5 h-5 rounded-lg bg-primary flex items-center justify-center border-2 border-card shadow-md">
                      <Users className="w-2.5 h-2.5 text-white" />
                    </div>
                  )}
                  {/* Status dot (online mock) */}
                  <div className="absolute top-0 right-0 w-3.5 h-3.5 rounded-full bg-emerald-500 border-2 border-card translate-x-0.5 -translate-y-0.5" />
                </div>

                {/* Content */}
                <div className="flex-1 min-w-0 py-0.5">
                  <div className="flex items-center justify-between gap-3 mb-1">
                    <div className="flex items-center gap-2 overflow-hidden">
                      <h3 className={cn(
                        "font-extrabold text-foreground truncate group-hover:text-primary transition-colors",
                        item.unread_count > 0 ? "text-base" : "text-sm sm:text-base"
                      )}>
                        {item.title}
                      </h3>
                      {item.conversation_type === 'group' && (
                        <span className="text-[9px] font-extrabold uppercase tracking-widest text-primary/70 bg-primary/8 px-1.5 py-0.5 rounded-md shrink-0">
                          Group
                        </span>
                      )}
                    </div>
                    <span className="text-[10px] font-extrabold text-muted-foreground uppercase flex items-center gap-1 shrink-0">
                      <Clock size={10} /> {item.time || 'JUST NOW'}
                    </span>
                  </div>

                  <div className="flex items-center justify-between gap-3">
                    <p className={cn(
                      "text-xs sm:text-sm truncate font-medium max-w-[80%]",
                      item.unread_count > 0 ? "text-foreground font-extrabold" : "text-muted-foreground"
                    )}>
                      {item.subtitle || 'Tap to view conversation'}
                    </p>
                    {item.unread_count > 0 && (
                      <Badge className="bg-primary text-white text-[10px] font-extrabold rounded-lg px-2 py-0.5 h-5 min-w-[20px] flex items-center justify-center border-none shadow-sm shadow-primary/20">
                        {item.unread_count}
                      </Badge>
                    )}
                  </div>
                </div>

                <div className="shrink-0 group-hover:translate-x-1 transition-transform opacity-0 group-hover:opacity-100 hidden sm:block">
                  <div className="w-8 h-8 rounded-xl bg-primary/8 flex items-center justify-center">
                    <ChevronRight size={14} className="text-primary" />
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* ── AI CTA Banner ───────────────────────────────────────────────────── */}
      <section
        onClick={() => navigate('/dashboard/ai-assistant')}
        className="rounded-2xl border border-primary/20 bg-card p-5 sm:p-6 flex flex-col sm:flex-row items-start sm:items-center gap-4 cursor-pointer group hover:border-primary/40 hover:shadow-md hover:shadow-primary/5 transition-all"
      >
        <div
          className="w-11 h-11 rounded-xl flex items-center justify-center shrink-0 shadow-md shadow-primary/20 group-hover:scale-105 transition-transform"
          style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
        >
          <Sparkles className="w-5 h-5 text-white" />
        </div>

        <div className="flex-1 min-w-0">
          <p className="text-sm font-extrabold text-foreground leading-tight">
            Need help finding a flatmate?
          </p>
          <p className="text-xs text-muted-foreground font-medium mt-0.5 leading-relaxed">
            Ask our AI Assistant for personalised recommendations near your workplace or college.
          </p>
        </div>

        <Button
          size="sm"
          className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1.5 shadow-sm shadow-primary/15 shrink-0 hover:-translate-y-0.5 transition-transform"
        >
          Ask AI <ChevronRight className="w-3.5 h-3.5" />
        </Button>
      </section>
    </div>
  );
}
