import { useState, useEffect, useRef } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { aiAPI, matchAPI } from '@/lib/api';
import { useNavigate } from 'react-router-dom';
import {
  Sparkles, Send, MapPin, Phone,
  Navigation, Users, ArrowRight,
  History, Info, Paperclip, CheckCircle2,
  AlertCircle
} from 'lucide-react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import toast from 'react-hot-toast';

// ── Palette ──────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Fallback avatar ───────────────────────────────────────────────────────────
const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=100`;

// ── Typing dots indicator ─────────────────────────────────────────────────────
function TypingIndicator() {
  return (
    <div className="flex items-start gap-3">
      <div className="w-8 h-8 rounded-xl bg-primary/10 border border-primary/15 flex items-center justify-center shrink-0 mt-0.5">
        <Sparkles className="w-4 h-4 text-primary animate-pulse" />
      </div>
      <div className="bg-card border border-border/50 rounded-xl rounded-tl-none px-5 py-3.5 flex items-center gap-1.5 shadow-sm">
        {[0, 0.15, 0.3].map((delay, i) => (
          <span
            key={i}
            className="w-1.5 h-1.5 bg-primary/50 rounded-full animate-bounce"
            style={{ animationDelay: `${delay}s`, animationDuration: '0.9s' }}
          />
        ))}
        <span className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest ml-1.5">
          Thinking…
        </span>
      </div>
    </div>
  );
}

// ── Result card ───────────────────────────────────────────────────────────────
function ResultCard({ r, onAction }) {
  return (
    <Card className="overflow-hidden border-border/50 shadow-sm hover:shadow-md hover:border-primary/20 transition-all duration-200 rounded-xl bg-card group/card">
      <div className="flex flex-col sm:flex-row">
        {/* Thumbnail */}
        <div className="w-full sm:w-40 h-36 sm:h-auto relative shrink-0 overflow-hidden">
          <img
            src="https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500&auto=format&fit=crop"
            alt=""
            className="w-full h-full object-cover transition-transform duration-500 group-hover/card:scale-105"
          />
          <div className="absolute top-2 left-2">
            <span className="text-[9px] font-extrabold uppercase tracking-wider bg-white/95 text-primary rounded-md px-2 py-1 shadow-sm">
              {r.stars || 4}★
            </span>
          </div>
        </div>

        {/* Details */}
        <div className="flex-1 p-4 sm:p-5 flex flex-col justify-between gap-3">
          <div>
            <div className="flex items-start justify-between gap-2 mb-1.5">
              <h3 className="font-extrabold text-sm text-foreground leading-snug group-hover/card:text-primary transition-colors">
                {r.title}
              </h3>
              <Badge className="bg-emerald-50 dark:bg-emerald-950/30 text-emerald-600 dark:text-emerald-400 border-none text-[9px] font-extrabold uppercase tracking-wider rounded-md px-2 py-0.5 h-auto shrink-0">
                Verified
              </Badge>
            </div>
            <p className="text-xs text-muted-foreground flex items-center gap-1 font-medium">
              <MapPin className="w-3 h-3 text-primary shrink-0" />
              <span className="truncate">{r.address}</span>
            </p>
          </div>

          <div className="flex items-center justify-between gap-2 pt-3 border-t border-border/50">
            <div>
              <p className="text-[9px] font-extrabold text-muted-foreground uppercase tracking-wider mb-0.5">
                {r.category === 'room' ? 'Per Month' : 'Per Night'}
              </p>
              <p className="text-base font-extrabold text-primary leading-none">
                ₹{r.price}
              </p>
            </div>
            <div className="flex items-center gap-1.5">
              <button
                onClick={() => window.open(`tel:${r.phone}`)}
                className="w-8 h-8 rounded-lg border border-border/60 flex items-center justify-center text-muted-foreground hover:text-primary hover:border-primary/30 hover:bg-primary/5 transition-colors"
                disabled={!r.phone}
              >
                <Phone className="w-3.5 h-3.5" />
              </button>
              <button
                onClick={() => window.open(`https://maps.google.com/?q=${encodeURIComponent(r.address)}`)}
                className="w-8 h-8 rounded-lg border border-border/60 flex items-center justify-center text-muted-foreground hover:text-primary hover:border-primary/30 hover:bg-primary/5 transition-colors"
              >
                <Navigation className="w-3.5 h-3.5" />
              </button>
              {(() => {
                const isAvailable = r.status === undefined || r.status === 'AVAILABLE';
                return (
                  <Button
                    size="sm"
                    disabled={!isAvailable}
                    onClick={() => onAction(r)}
                    className={cn(
                      "h-8 px-4 rounded-lg font-extrabold text-xs shadow-sm shadow-primary/15 transition-all",
                      isAvailable ? "hover:-translate-y-0.5" : "bg-muted text-muted-foreground cursor-not-allowed shadow-none"
                    )}
                  >
                    {isAvailable ? 'Details' : 'Booked'}
                  </Button>
                );
              })()}
            </div>
          </div>
        </div>
      </div>
    </Card>
  );
}

// ── Roommate chip ─────────────────────────────────────────────────────────────
function RoommateStrip({ roommates, navigate }) {
  return (
    <div className="rounded-xl border border-border/50 bg-muted/30 p-4 mt-3">
      <div className="flex items-center gap-2 mb-3">
        <div className="w-7 h-7 rounded-lg bg-primary/10 flex items-center justify-center">
          <Users className="w-3.5 h-3.5 text-primary" />
        </div>
        <p className="text-xs font-extrabold text-foreground uppercase tracking-wide">
          Roommates nearby
        </p>
      </div>
      <div className="flex gap-2.5 overflow-x-auto pb-1 scrollbar-hide">
        {roommates.map((rm, k) => (
          <button
            key={k}
            onClick={() => navigate(`/dashboard/roommate/${rm.email}`)}
            className="flex-shrink-0 flex items-center gap-2.5 bg-card border border-border/60 hover:border-primary/30 hover:shadow-sm rounded-xl px-3 py-2.5 transition-all group/rm"
          >
            <img
              src={rm.photos?.[0]?.image || avatarUrl(rm.full_name)}
              alt=""
              className="w-8 h-8 rounded-lg object-cover border border-border/40"
            />
            <div className="text-left min-w-0">
              <p className="text-xs font-extrabold text-foreground truncate group-hover/rm:text-primary transition-colors leading-tight max-w-[90px]">
                {rm.full_name || 'Anonymous'}
              </p>
              <p className="text-[9px] text-muted-foreground uppercase tracking-wide font-semibold">
                {rm.room_status?.replace('_', ' ') || 'Seeking'}
              </p>
            </div>
            <ArrowRight className="w-3 h-3 text-primary opacity-0 group-hover/rm:opacity-100 group-hover/rm:translate-x-0.5 transition-all" />
          </button>
        ))}
      </div>
    </div>
  );
}

// ── Message bubble ────────────────────────────────────────────────────────────
function MessageBubble({ msg, navigate, onAction }) {
  if (!msg.isAI) {
    return (
      <div className="flex justify-end">
        <div className="max-w-[80%] sm:max-w-[65%] bg-primary text-white rounded-2xl rounded-tr-none px-4 py-3 text-sm font-semibold leading-relaxed shadow-md shadow-primary/20">
          {msg.text}
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-start gap-2.5 sm:gap-3">
      <div className="w-8 h-8 rounded-xl bg-primary/8 border border-primary/15 flex items-center justify-center shrink-0 mt-0.5">
        <Sparkles className="w-4 h-4 text-primary" />
      </div>

      <div className="flex-1 min-w-0 space-y-3">
        {/* Text bubble */}
        <div className="bg-card border border-border/50 rounded-2xl rounded-tl-none px-4 py-3 text-sm leading-relaxed shadow-sm max-w-[85%] sm:max-w-[75%]">
          <div className="prose prose-sm max-w-none dark:prose-invert prose-p:leading-relaxed prose-p:my-1 prose-ul:my-1 prose-li:my-0.5 prose-pre:bg-muted prose-pre:text-xs prose-code:text-primary prose-code:bg-primary/8 prose-code:px-1 prose-code:rounded text-foreground">
            <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
          </div>
        </div>

        {/* Result cards */}
        {msg.results?.length > 0 && (
          <div className="space-y-2.5">
            {msg.results.map((r, j) => (
              <ResultCard key={j} r={r} onAction={onAction} />
            ))}
          </div>
        )}

        {/* Roommates strip */}
        {msg.nearbyRoommates?.length > 0 && (
          <RoommateStrip roommates={msg.nearbyRoommates} navigate={navigate} />
        )}
      </div>
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function AIAssistantPage() {
  const { user, isPremium, aiUsage, incrementAiUsage } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [messages, setMessages] = useState([
    {
      isAI: true,
      text: "👋 Hi! I'm your RoomShare AI Assistant. Ask me to find hotels, rooms, or roommates near any location — and I'll search for you in real time.",
    },
  ]);
  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isTyping]);

  const quickPrompts = [
    { label: '🏨 Hotels in Chennai under ₹2000', query: 'Hotels in Chennai under ₹2000' },
    { label: '🏠 Rooms near Anna Nagar', query: 'Rooms near Anna Nagar' },
    { label: '👥 Find roommates in Chennai', query: 'Find roommates in Chennai' },
    { label: '📍 PG near SRM University', query: 'PG near SRM University' },
  ];

  const handleSend = async (text) => {
    const query = (text || input).trim();
    if (!query || isTyping) return;

    // Rate Limit Check
    if (!isPremium && aiUsage >= 5) {
      toast.error('Daily AI limit reached. Please upgrade to Premium!');
      navigate('/dashboard/pricing');
      return;
    }

    setMessages(prev => [...prev, { text: query, isAI: false }]);
    setInput('');
    setIsTyping(true);

    try {
      incrementAiUsage();
      const res = await aiAPI.locationAgent(query, email);
      const data = res.data || {};
      setMessages(prev => [...prev, {
        isAI: true,
        text: data.text || "I couldn't process that request. Please try again.",
        results: data.results || [],
        nearbyRoommates: data.roommates || [],
        intent: data.intent,
      }]);
    } catch {
      setMessages(prev => [...prev, {
        isAI: true,
        text: 'Network error. Please check your connection and try again.',
      }]);
    } finally {
      setIsTyping(false);
    }
  };

  const handleAction = (item) => {
    if (item.is_local) {
      navigate(item.category === 'hotel' ? `/dashboard/hotel/${item.id}` : `/dashboard/room/${item.id}`);
    } else {
      // Pass external data in state to the detail page
      navigate(`/dashboard/hotel/external-${item.id}`, { state: { externalHotel: item } });
    }
  };

  const showQuickPrompts = messages.length <= 1 && !isTyping;

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 flex flex-col h-[calc(100svh-5rem)] sm:h-[calc(100svh-6rem)]">

      {/* ── Header ───────────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between py-4 sm:py-5 border-b border-border/50 shrink-0">
        <div className="flex items-center gap-3">
          <div
            className="w-9 h-9 sm:w-10 sm:h-10 rounded-xl flex items-center justify-center shadow-md shadow-primary/20 shrink-0"
            style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
          >
            <Sparkles className="w-4 h-4 sm:w-5 sm:h-5 text-white" />
          </div>
          <div>
            <h1 className="text-sm sm:text-base font-extrabold text-foreground tracking-tight leading-tight">
              AI Concierge
            </h1>
            <div className="flex items-center gap-1.5 mt-0.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-wider">
                Online · v2.0
              </span>
            </div>
          </div>
        </div>


      </div>

      {/* ── Messages ─────────────────────────────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto py-4 sm:py-5 space-y-4 sm:space-y-5 min-h-0">
        {messages.map((msg, i) => (
          <MessageBubble
            key={i}
            msg={msg}
            navigate={navigate}
            onAction={handleAction}
          />
        ))}

        {isTyping && <TypingIndicator />}

        <div ref={bottomRef} />
      </div>

      {/* ── Input area ───────────────────────────────────────────────────────── */}
      <div className="shrink-0 pb-3 sm:pb-4 pt-2 space-y-2.5">

        {/* Quick prompt chips */}
        {showQuickPrompts && (
          <div className="flex flex-wrap gap-2">
            {quickPrompts.map((p, i) => (
              <button
                key={i}
                onClick={() => handleSend(p.query)}
                className="text-xs font-semibold px-3.5 py-2 rounded-full bg-card border border-border/60 text-muted-foreground hover:border-primary/40 hover:text-primary hover:bg-primary/5 transition-all shadow-sm"
              >
                {p.label}
              </button>
            ))}
          </div>
        )}

        {/* Input row */}
        <div className="flex items-center gap-2 bg-card border border-border/60 rounded-2xl px-3 py-2 shadow-sm focus-within:ring-2 focus-within:ring-primary/20 focus-within:border-primary/30 transition-all">
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
            placeholder="Ask anything — hotels, rooms, roommates…"
            disabled={isTyping}
            className="flex-1 bg-transparent text-sm font-medium text-foreground placeholder:text-muted-foreground/60 focus:outline-none py-2 px-1 min-w-0"
          />

          <Button
            onClick={() => handleSend()}
            disabled={!input.trim() || isTyping}
            size="sm"
            className="h-9 w-9 sm:w-auto sm:px-5 rounded-xl font-extrabold text-xs gap-1.5 shadow-sm shadow-primary/15 shrink-0 disabled:opacity-40 transition-all hover:-translate-y-0.5"
          >
            <span className="hidden sm:inline">Send</span>
            <Send className="w-3.5 h-3.5" />
          </Button>
        </div>

        <p className="text-center text-[10px] text-muted-foreground/50 font-medium hidden sm:block">
          AI responses are for guidance only · Always verify listings independently
        </p>
      </div>
    </div>
  );
}