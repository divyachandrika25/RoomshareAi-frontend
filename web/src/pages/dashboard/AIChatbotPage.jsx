import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { chatbotAPI } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import { Send, Bot, Trash2, Loader2, Sparkles, MessageCircle, User } from 'lucide-react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import toast from 'react-hot-toast';
import { cn } from '@/lib/utils';

// ── Palette (matches app) ─────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Typing dots ───────────────────────────────────────────────────────────────
function TypingDots() {
  return (
    <div className="flex items-start gap-2.5">
      <div className="w-8 h-8 rounded-xl bg-primary/8 border border-primary/15 flex items-center justify-center shrink-0 mt-0.5">
        <Sparkles className="w-4 h-4 text-primary animate-pulse" />
      </div>
      <div className="bg-card border border-border/50 rounded-2xl rounded-tl-none px-4 py-3 flex items-center gap-1.5 shadow-sm">
        {[0, 0.15, 0.3].map((d, i) => (
          <span
            key={i}
            className="w-1.5 h-1.5 bg-primary/40 rounded-full animate-bounce"
            style={{ animationDelay: `${d}s`, animationDuration: '0.9s' }}
          />
        ))}
      </div>
    </div>
  );
}

// ── Empty state ───────────────────────────────────────────────────────────────
function EmptyState({ suggestions, onSelect }) {
  return (
    <div className="flex flex-col items-center justify-center h-full px-4 py-12 text-center space-y-5">
      <div
        className="w-14 h-14 rounded-2xl flex items-center justify-center shadow-lg shadow-primary/15"
        style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
      >
        <MessageCircle className="w-7 h-7 text-white" />
      </div>

      <div className="space-y-1">
        <h3 className="text-base font-extrabold text-foreground tracking-tight">
          RoomShare AI Chatbot
        </h3>
        <p className="text-sm text-muted-foreground max-w-xs leading-relaxed">
          Ask me anything about finding roommates, housing tips, budgeting, or lease advice.
        </p>
      </div>

      {suggestions.length > 0 && (
        <div className="flex flex-wrap justify-center gap-2 max-w-sm">
          {suggestions.map((s, i) => (
            <button
              key={i}
              onClick={() => onSelect(s)}
              className="text-xs font-semibold px-3.5 py-2 rounded-full bg-card border border-border/60 text-muted-foreground hover:border-primary/40 hover:text-primary hover:bg-primary/5 transition-all shadow-sm"
            >
              {s}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

// ── Message bubble ────────────────────────────────────────────────────────────
function Bubble({ msg }) {
  const isUser = msg.role === 'user';

  if (isUser) {
    return (
      <div className="flex justify-end items-end gap-2">
        <div className="max-w-[78%] sm:max-w-[65%] bg-primary text-white rounded-2xl rounded-tr-none px-4 py-3 text-sm font-semibold leading-relaxed shadow-md shadow-primary/20">
          {msg.content}
        </div>
        <div className="w-7 h-7 rounded-lg bg-primary/15 border border-primary/20 flex items-center justify-center shrink-0">
          <User className="w-3.5 h-3.5 text-primary" />
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-start gap-2.5">
      <div className="w-8 h-8 rounded-xl bg-primary/8 border border-primary/15 flex items-center justify-center shrink-0 mt-0.5">
        <Sparkles className="w-4 h-4 text-primary" />
      </div>
      <div className="max-w-[85%] sm:max-w-[72%] bg-card border border-border/50 rounded-2xl rounded-tl-none px-4 py-3 text-sm leading-relaxed shadow-sm">
        <div className="prose prose-sm max-w-none dark:prose-invert prose-p:leading-relaxed prose-p:my-1 prose-ul:my-1 prose-li:my-0.5 prose-pre:bg-muted prose-pre:text-xs prose-code:text-primary prose-code:bg-primary/8 prose-code:px-1 prose-code:rounded text-foreground">
          <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.content}</ReactMarkdown>
        </div>
      </div>
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function AIChatbotPage() {
  const { user, isPremium, aiUsage, incrementAiUsage } = useAuth();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [sending, setSending] = useState(false);
  const [loadingHistory, setLoadingHistory] = useState(true);
  const [suggestions, setSuggestions] = useState([]);
  const chatEndRef = useRef(null);
  const inputRef = useRef(null);

  useEffect(() => {
    if (user?.email) loadHistory();
  }, [user]);

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, sending]);

  const loadHistory = async () => {
    try {
      setLoadingHistory(true);
      const res = await chatbotAPI.getHistory(user.email);
      setMessages(res.data.messages || []);
      // Load dynamic suggestions from API if available
      if (res.data.suggestions?.length) setSuggestions(res.data.suggestions);
    } catch (e) {
      console.error(e);
    } finally {
      setLoadingHistory(false);
    }
  };

  const sendMessage = async (text) => {
    const content = (text || input).trim();
    if (!content || sending) return;

    // Rate Limit Check
    if (!isPremium && aiUsage >= 5) {
      toast.error('Daily AI limit reached. Please upgrade to Premium!');
      navigate('/dashboard/pricing');
      return;
    }

    setInput('');
    setMessages(prev => [...prev, { role: 'user', content }]);
    setSending(true);
    inputRef.current?.focus();

    try {
      incrementAiUsage();
      const res = await chatbotAPI.sendMessage(user?.email, content);
      setMessages(prev => [...prev, { role: 'assistant', content: res.data.response }]);
    } catch {
      setMessages(prev => [...prev, {
        role: 'assistant',
        content: "Sorry, I couldn't process that. Please try again.",
      }]);
    } finally {
      setSending(false);
    }
  };

  const clearChat = async () => {
    if (!user?.email) return;
    try {
      await chatbotAPI.clearHistory(user.email);
      setMessages([]);
      toast.success('Chat cleared');
    } catch {
      toast.error('Failed to clear chat');
    }
  };

  const isEmpty = !loadingHistory && messages.length === 0;

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
              AI Chatbot
            </h1>
            <div className="flex items-center gap-1.5 mt-0.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-wider">
                Online · RoomShare AI
              </span>
            </div>
          </div>
        </div>

        {messages.length > 0 && (
          <button
            onClick={clearChat}
            className="w-8 h-8 rounded-lg flex items-center justify-center text-muted-foreground hover:text-rose-500 hover:bg-rose-50 dark:hover:bg-rose-950/30 transition-colors"
            title="Clear chat"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        )}
      </div>

      {/* ── Messages ─────────────────────────────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto py-4 sm:py-5 min-h-0">
        {loadingHistory ? (
          <div className="flex flex-col items-center justify-center h-full gap-3">
            <Loader2 className="w-6 h-6 text-primary animate-spin" />
            <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
              Loading history…
            </p>
          </div>
        ) : isEmpty ? (
          <EmptyState suggestions={suggestions} onSelect={sendMessage} />
        ) : (
          <div className="space-y-4 sm:space-y-5">
            {messages.map((msg, i) => (
              <Bubble key={i} msg={msg} />
            ))}
            {sending && <TypingDots />}
            <div ref={chatEndRef} />
          </div>
        )}
      </div>

      {/* ── Input ────────────────────────────────────────────────────────────── */}
      <div className="shrink-0 pb-3 sm:pb-4 pt-2">
        <div className="flex items-center gap-2 bg-card border border-border/60 rounded-2xl px-3 py-2 shadow-sm focus-within:ring-2 focus-within:ring-primary/20 focus-within:border-primary/30 transition-all">
          <input
            ref={inputRef}
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && sendMessage()}
            placeholder="Ask about roommates, housing, leases…"
            disabled={sending || loadingHistory}
            className="flex-1 bg-transparent text-sm font-medium text-foreground placeholder:text-muted-foreground/60 focus:outline-none py-2 px-1 min-w-0"
          />
          <button
            onClick={() => sendMessage()}
            disabled={!input.trim() || sending}
            className={cn(
              "h-9 w-9 sm:w-auto sm:px-5 rounded-xl font-extrabold text-xs flex items-center justify-center gap-1.5 shrink-0 transition-all",
              "bg-primary text-white shadow-sm shadow-primary/20",
              "hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/25",
              "disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:translate-y-0"
            )}
          >
            {sending
              ? <Loader2 className="w-3.5 h-3.5 animate-spin" />
              : <>
                  <span className="hidden sm:inline">Send</span>
                  <Send className="w-3.5 h-3.5" />
                </>
            }
          </button>
        </div>

        <p className="text-center text-[10px] text-muted-foreground/40 font-medium mt-2 hidden sm:block">
          AI responses are for guidance only · Not a substitute for professional advice
        </p>
      </div>
    </div>
  );
}