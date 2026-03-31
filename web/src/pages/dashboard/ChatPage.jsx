import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { chatAPI } from '@/lib/api';
import {
  ArrowLeft, Send, Phone, Home,
  MoreVertical, Smile, Paperclip,
  CheckCheck, ShieldCheck, AlertCircle,
  Search, Mic
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=100`;

const formatTime = (t) => {
  try { return new Date(t).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); }
  catch { return ''; }
};

// ── Loading skeleton ──────────────────────────────────────────────────────────
function MessageSkeleton({ mine }) {
  return (
    <div className={cn('flex gap-2.5 animate-pulse', mine ? 'justify-end' : 'justify-start')}>
      {!mine && <div className="w-8 h-8 rounded-xl bg-muted shrink-0" />}
      <div className={cn('h-10 rounded-2xl bg-muted', mine ? 'w-40' : 'w-52')} />
    </div>
  );
}

// ── Room action card ──────────────────────────────────────────────────────────
function RoomCard({ msg, navigate }) {
  return (
    <Card
      onClick={() => navigate(`/dashboard/room/${msg.roomId}`)}
      className="rounded-2xl border-primary/20 bg-card shadow-sm hover:shadow-md hover:border-primary/30 transition-all cursor-pointer overflow-hidden group/room max-w-[260px]"
    >
      <div className="p-4 space-y-2.5">
        <Badge className="bg-primary/10 text-primary border-none text-[9px] font-extrabold uppercase tracking-wider rounded-md px-2 py-0.5 h-auto">
          Shared Property
        </Badge>
        <div className="flex items-start justify-between gap-2">
          <div>
            <h4 className="font-extrabold text-sm text-foreground group-hover/room:text-primary transition-colors leading-tight">
              {msg.roomTitle}
            </h4>
            <p className="text-base font-extrabold text-primary mt-1 leading-none tabular-nums">
              ₹{msg.roomPrice}
              <span className="text-[10px] font-semibold text-muted-foreground"> /mo</span>
            </p>
          </div>
          <div className="w-9 h-9 rounded-xl bg-primary/8 flex items-center justify-center shrink-0">
            <Home className="w-4 h-4 text-primary" />
          </div>
        </div>
        <Button
          size="sm"
          variant="outline"
          className="w-full h-8 rounded-xl text-xs font-extrabold border-primary/20 text-primary hover:bg-primary hover:text-white hover:border-primary transition-all"
        >
          View Profile
        </Button>
      </div>
    </Card>
  );
}

// ── Single message ────────────────────────────────────────────────────────────
function MessageBubble({ msg, type, navigate }) {
  const isMe = msg.isMe;

  return (
    <div className={cn('flex w-full gap-2.5', isMe ? 'justify-end' : 'justify-start')}>
      {/* Other user avatar */}
      {!isMe && (
        <img
          src={msg.photo || avatarUrl(msg.sender)}
          alt=""
          className="w-8 h-8 rounded-xl object-cover border border-border/40 shrink-0 mt-0.5"
        />
      )}

      <div className={cn('flex flex-col gap-1', isMe ? 'items-end' : 'items-start')}>
        {/* Sender name in group */}
        {!isMe && type === 'group' && (
          <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-wide ml-1">
            {msg.sender}
          </p>
        )}

        {/* Bubble or room card */}
        {msg.isRoomAction ? (
          <RoomCard msg={msg} navigate={navigate} />
        ) : (
          <div className={cn(
            'px-4 py-3 rounded-2xl text-sm font-medium leading-relaxed shadow-sm max-w-[75vw] sm:max-w-[420px] break-words',
            isMe
              ? 'bg-primary text-white rounded-tr-none shadow-primary/15'
              : 'bg-card border border-border/50 text-foreground rounded-tl-none'
          )}>
            {msg.text}
          </div>
        )}

        {/* Timestamp + status */}
        <div className={cn(
          'flex items-center gap-1 text-[10px] text-muted-foreground/50 font-medium px-1',
          isMe ? 'flex-row-reverse' : 'flex-row'
        )}>
          <span>{formatTime(msg.time)}</span>
          {isMe && (
            msg.status === 'sending'
              ? <div className="w-1.5 h-1.5 rounded-full bg-muted-foreground/30 animate-pulse" />
              : msg.status === 'failed'
              ? <AlertCircle className="w-3 h-3 text-rose-500" />
              : <CheckCheck className="w-3 h-3 text-primary/60" />
          )}
        </div>
      </div>

      {/* My avatar placeholder for spacing */}
      {isMe && <div className="w-8 shrink-0" />}
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function ChatPage() {
  const { type, chatId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [messages, setMessages] = useState([]);
  const [chatInfo, setChatInfo] = useState({});
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(true);
  const bottomRef = useRef(null);
  const inputRef = useRef(null);

  useEffect(() => {
    if (!email || !chatId) return;
    const load = async () => {
      try {
        if (type === 'direct') {
          const res = await chatAPI.getDirectChat(chatId, email);
          if (res.data) {
            setChatInfo({ name: res.data.other_user_name, photo: res.data.other_user_photo });
            setMessages((res.data.messages || []).map(m => ({
              id: m.id, text: m.content, isMe: m.is_current_user,
              sender: m.sender_name, time: m.created_at,
            })));
          }
        } else {
          const res = await chatAPI.startGroupChat(email);
          if (res.data?.data) {
            setChatInfo({ name: res.data.data.groupName });
            setMessages((res.data.data.messages || []).map(m => ({
              id: m.id, text: m.content, isMe: m.is_current_user,
              sender: m.sender_name, time: m.created_at, photo: m.sender_photo,
              isRoomAction: m.message_type === 'ROOM_DETAILS',
              roomTitle: m.room_title, roomPrice: m.room_price, roomId: m.room_id,
            })));
          }
        }
      } catch {}
      setLoading(false);
    };
    load();
  }, [email, chatId, type]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = async () => {
    const text = input.trim();
    if (!text) return;
    setInput('');
    inputRef.current?.focus();

    const tempId = Date.now();
    const newMsg = { id: tempId, text, isMe: true, sender: 'You', time: new Date().toISOString(), status: 'sending' };
    setMessages(prev => [...prev, newMsg]);

    try {
      if (type === 'direct') {
        await chatAPI.sendDirectMessage({ chat_id: parseInt(chatId), sender_email: email, message: text });
      } else {
        await chatAPI.sendGroupMessage({ chat_id: parseInt(chatId), sender_email: email, message: text });
      }
      setMessages(prev => prev.map(m => m.id === tempId ? { ...m, status: 'sent' } : m));
    } catch {
      setMessages(prev => prev.map(m => m.id === tempId ? { ...m, status: 'failed' } : m));
    }
  };

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 flex flex-col h-[calc(100svh-5rem)] sm:h-[calc(100svh-6rem)]">

      {/* ── Header ───────────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between py-3 sm:py-4 border-b border-border/50 shrink-0 gap-3">
        {/* Left: back + avatar + name */}
        <div className="flex items-center gap-2.5 min-w-0">
          <button
            onClick={() => navigate('/dashboard/messages')}
            className="w-8 h-8 rounded-xl flex items-center justify-center text-muted-foreground hover:bg-muted hover:text-foreground transition-colors shrink-0"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>

          <div className="relative shrink-0">
            <img
              src={chatInfo.photo || avatarUrl(chatInfo.name)}
              alt=""
              className="w-9 h-9 rounded-xl object-cover border border-border/40"
            />
            <span className="absolute -bottom-0.5 -right-0.5 w-3 h-3 rounded-full bg-emerald-500 border-2 border-background" />
          </div>

          <div className="min-w-0">
            <p className="text-sm font-extrabold text-foreground leading-tight truncate">
              {chatInfo.name || 'Conversation'}
            </p>
            <p className="text-[10px] text-emerald-500 font-extrabold uppercase tracking-widest leading-tight">
              Active now
            </p>
          </div>
        </div>

        {/* Right: actions */}
        <div className="flex items-center gap-1 shrink-0">
          <button className="hidden sm:flex w-8 h-8 rounded-xl items-center justify-center text-muted-foreground hover:bg-muted hover:text-foreground transition-colors">
            <Search className="w-4 h-4" />
          </button>
          <button className="w-8 h-8 rounded-xl flex items-center justify-center text-muted-foreground hover:bg-muted hover:text-foreground transition-colors">
            <MoreVertical className="w-4 h-4" />
          </button>
          <Button
            size="sm"
            variant="outline"
            className="hidden md:flex h-8 px-3 rounded-xl font-extrabold text-xs gap-1.5 border-border/60 text-muted-foreground hover:text-primary hover:border-primary/30"
          >
            <Phone className="w-3.5 h-3.5" /> Call
          </Button>
        </div>
      </div>

      {/* ── E2E notice ───────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-center gap-2 py-2 border-b border-border/40 shrink-0">
        <ShieldCheck className="w-3 h-3 text-primary/50" />
        <p className="text-[10px] font-semibold text-muted-foreground/60 uppercase tracking-wider">
          End-to-end encrypted
        </p>
      </div>

      {/* ── Messages feed ────────────────────────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto py-4 sm:py-5 space-y-3 sm:space-y-4 min-h-0">
        {loading ? (
          <div className="space-y-4 px-1">
            {[false, true, false, true, false].map((mine, i) => (
              <MessageSkeleton key={i} mine={mine} />
            ))}
          </div>
        ) : messages.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-full text-center px-4 space-y-3">
            <div
              className="w-12 h-12 rounded-2xl flex items-center justify-center shadow-md shadow-primary/15"
              style={{ background: `linear-gradient(135deg, ${P.blue}22 0%, ${P.blueDark}22 100%)` }}
            >
              <Smile className="w-6 h-6 text-primary" />
            </div>
            <div className="space-y-1">
              <p className="text-sm font-extrabold text-foreground">Say Hello!</p>
              <p className="text-xs text-muted-foreground max-w-xs leading-relaxed">
                Start with a friendly introduction — matches are often made in the first few messages.
              </p>
            </div>
          </div>
        ) : (
          messages.map((msg, idx) => (
            <MessageBubble key={msg.id || idx} msg={msg} type={type} navigate={navigate} />
          ))
        )}
        <div ref={bottomRef} />
      </div>

      {/* ── Input bar ────────────────────────────────────────────────────────── */}
      <div className="shrink-0 pb-3 sm:pb-4 pt-2">
        <div className="flex items-center gap-2 bg-card border border-border/60 rounded-2xl px-2 sm:px-3 py-2 shadow-sm focus-within:ring-2 focus-within:ring-primary/20 focus-within:border-primary/30 transition-all">
          {/* Attachment */}
          <button className="hidden sm:flex w-8 h-8 rounded-xl items-center justify-center text-muted-foreground hover:text-foreground hover:bg-muted transition-colors shrink-0">
            <Paperclip className="w-4 h-4" />
          </button>

          {/* Emoji */}
          <button className="hidden sm:flex w-8 h-8 rounded-xl items-center justify-center text-muted-foreground hover:text-amber-500 hover:bg-muted transition-colors shrink-0">
            <Smile className="w-4 h-4" />
          </button>

          <input
            ref={inputRef}
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
            placeholder="Message…"
            disabled={loading}
            className="flex-1 bg-transparent text-sm font-medium text-foreground placeholder:text-muted-foreground/60 focus:outline-none py-2 px-1 min-w-0"
          />

          {/* Mic */}
          <button className="hidden sm:flex w-8 h-8 rounded-xl items-center justify-center text-muted-foreground hover:text-foreground hover:bg-muted transition-colors shrink-0">
            <Mic className="w-4 h-4" />
          </button>

          {/* Send */}
          <button
            onClick={handleSend}
            disabled={!input.trim() || loading}
            className={cn(
              'h-9 w-9 sm:w-auto sm:px-5 rounded-xl font-extrabold text-xs flex items-center justify-center gap-1.5 shrink-0 transition-all',
              'bg-primary text-white shadow-sm shadow-primary/20',
              'hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/25',
              'disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:translate-y-0'
            )}
          >
            <span className="hidden sm:inline">Send</span>
            <Send className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>
    </div>
  );
}