import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { bookingAPI } from '@/lib/api';
import {
  ArrowLeft, Calendar, CheckCircle, Clock, XCircle,
  Receipt, MapPin, ChevronRight, ShieldCheck, Home, Hash
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Status config ─────────────────────────────────────────────────────────────
const STATUS = {
  CONFIRMED: {
    icon: CheckCircle,
    label: 'Confirmed',
    cls: 'bg-emerald-50 dark:bg-emerald-950/30 text-emerald-600 dark:text-emerald-400 border-emerald-100 dark:border-emerald-900/40',
  },
  PENDING: {
    icon: Clock,
    label: 'Pending',
    cls: 'bg-amber-50 dark:bg-amber-950/30 text-amber-600 dark:text-amber-400 border-amber-100 dark:border-amber-900/40',
  },
  CANCELLED: {
    icon: XCircle,
    label: 'Cancelled',
    cls: 'bg-rose-50 dark:bg-rose-950/30 text-rose-600 dark:text-rose-400 border-rose-100 dark:border-rose-900/40',
  },
};

const getStatus = (s) => STATUS[s?.toUpperCase()] ?? {
  icon: Clock,
  label: s || 'Processing',
  cls: 'bg-muted text-muted-foreground border-border/60',
};

// ── Skeleton row ──────────────────────────────────────────────────────────────
function SkeletonRow() {
  return (
    <div className="animate-pulse rounded-2xl border border-border/40 bg-card p-4 sm:p-5 flex gap-4">
      <div className="w-12 h-12 rounded-xl bg-muted shrink-0" />
      <div className="flex-1 space-y-2.5">
        <div className="h-3.5 bg-muted rounded-full w-2/5" />
        <div className="h-3 bg-muted rounded-full w-3/5" />
        <div className="h-3 bg-muted rounded-full w-1/4" />
      </div>
      <div className="w-20 h-8 bg-muted rounded-xl shrink-0" />
    </div>
  );
}

// ── Booking card ──────────────────────────────────────────────────────────────
function BookingCard({ b, idx, onClick }) {
  const { icon: StatusIcon, label, cls } = getStatus(b.status);

  return (
    <Card
      onClick={onClick}
      className="group rounded-2xl border-border/50 shadow-sm hover:shadow-md hover:border-primary/20 transition-all duration-200 overflow-hidden cursor-pointer bg-card"
    >
      <div className="p-4 sm:p-5 flex flex-col sm:flex-row sm:items-center gap-4">

        {/* Icon */}
        <div className="w-11 h-11 rounded-xl bg-primary/8 border border-primary/15 flex items-center justify-center shrink-0 group-hover:bg-primary/12 transition-colors">
          <Home className="w-5 h-5 text-primary" />
        </div>

        {/* Main info */}
        <div className="flex-1 min-w-0 space-y-1">
          <div className="flex items-center gap-2 flex-wrap">
            <Badge
              variant="secondary"
              className="bg-muted text-muted-foreground border-none text-[9px] font-extrabold uppercase tracking-wider rounded-md px-2 py-0.5 h-auto"
            >
              {b.is_hotel ? 'Hotel Stay' : 'Room Share'}
            </Badge>
          </div>
          <h3 className="text-sm font-extrabold text-foreground leading-tight group-hover:text-primary transition-colors truncate pr-2">
            {b.room_title}
          </h3>
          <p className="text-xs text-muted-foreground flex items-center gap-1 font-medium">
            <MapPin className="w-3 h-3 text-primary shrink-0" />
            <span className="truncate">{b.location || 'Chennai, Tamil Nadu'}</span>
          </p>
        </div>

        {/* Right: status + amount */}
        <div className="flex sm:flex-col items-center sm:items-end justify-between sm:justify-center gap-3 pt-3 sm:pt-0 border-t sm:border-t-0 border-border/40 sm:pl-4 shrink-0">
          <Badge
            variant="outline"
            className={cn(
              "flex items-center gap-1.5 text-[9px] font-extrabold uppercase tracking-wider px-2.5 py-1 h-auto rounded-lg border",
              cls
            )}
          >
            <StatusIcon className="w-3 h-3" /> {label}
          </Badge>
          <div className="text-right">
            <p className="text-[9px] text-muted-foreground font-extrabold uppercase tracking-wider leading-none mb-0.5">
              Total
            </p>
            <p className="text-base font-extrabold text-foreground tabular-nums">
              ₹{b.amount || '0'}
            </p>
          </div>
        </div>
      </div>

      {/* Footer strip */}
      <div className="px-4 sm:px-5 py-2.5 border-t border-border/40 bg-muted/20 flex items-center justify-between gap-2">
        <div className="flex items-center gap-3 text-[10px] text-muted-foreground font-semibold flex-wrap">
          <span className="flex items-center gap-1">
            <Calendar className="w-3 h-3 text-primary/60" />
            {b.created_at || 'Recent'}
          </span>
          {b.id && (
            <span className="hidden sm:flex items-center gap-1">
              <Hash className="w-3 h-3 text-primary/60" />
              {b.id.toString().slice(0, 8).toUpperCase()}
            </span>
          )}
        </div>
        <span className="flex items-center gap-0.5 text-[10px] font-extrabold text-primary uppercase tracking-wide group-hover:translate-x-0.5 transition-transform">
          Details <ChevronRight className="w-3.5 h-3.5" />
        </span>
      </div>
    </Card>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function BookingHistoryPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!email) return;
    bookingAPI.getHistory(email)
      .then(res => { if (res.data?.bookings) setBookings(res.data.bookings); })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [email]);

  const handleCardClick = (b) => {
    if (b.is_hotel) navigate(`/dashboard/hotel/${b.id}`);
    else navigate(`/dashboard/room/${b.room_id || b.id}`);
  };

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-5 sm:space-y-6">

      {/* ── Page header ───────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between pt-4 sm:pt-6">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => navigate(-1)}
          className="h-9 px-3 rounded-xl text-muted-foreground hover:text-foreground hover:bg-muted gap-1.5 font-bold"
        >
          <ArrowLeft className="w-4 h-4" /> Back
        </Button>
        <Button
          variant="outline"
          size="sm"
          className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1.5 border-border/60"
        >
          <Receipt className="w-3.5 h-3.5" /> Export PDF
        </Button>
      </div>

      {/* ── Title block ───────────────────────────────────────────────────────── */}
      <div className="pb-4 border-b border-border/50">
        <h1 className="text-xl sm:text-2xl font-extrabold text-foreground tracking-tight">
          Booking History
        </h1>
        {!loading && (
          <p className="text-xs text-muted-foreground font-semibold mt-0.5">
            {bookings.length === 0
              ? 'No bookings yet'
              : `${bookings.length} transaction${bookings.length !== 1 ? 's' : ''} found`}
          </p>
        )}
      </div>

      {/* ── States ────────────────────────────────────────────────────────────── */}
      {loading ? (
        <div className="space-y-3">
          {[...Array(4)].map((_, i) => <SkeletonRow key={i} />)}
        </div>
      ) : bookings.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-20 px-4 text-center space-y-4">
          <div
            className="w-14 h-14 rounded-2xl flex items-center justify-center shadow-md shadow-primary/15"
            style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
          >
            <Calendar className="w-7 h-7 text-white" />
          </div>
          <div className="space-y-1">
            <h3 className="text-base font-extrabold text-foreground">No Bookings Yet</h3>
            <p className="text-sm text-muted-foreground max-w-xs leading-relaxed">
              You haven't made any bookings. Explore matches to find your perfect room or hotel.
            </p>
          </div>
          <Button
            onClick={() => navigate('/dashboard/matches')}
            className="h-10 px-6 rounded-xl font-extrabold text-sm shadow-md shadow-primary/15 hover:-translate-y-0.5 transition-transform"
          >
            Explore Matches
          </Button>
        </div>
      ) : (
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-3 sm:gap-4">
          {bookings.map((b, idx) => (
            <BookingCard
              key={b.id || idx}
              b={b}
              idx={idx}
              onClick={() => handleCardClick(b)}
            />
          ))}
        </div>
      )}

      {/* ── Payment protection notice ─────────────────────────────────────────── */}
      {!loading && (
        <div className="rounded-2xl border border-border/50 bg-card p-5 sm:p-6 flex flex-col sm:flex-row items-start sm:items-center gap-4">
          <div className="w-10 h-10 rounded-xl bg-emerald-50 dark:bg-emerald-950/30 border border-emerald-100 dark:border-emerald-900/40 flex items-center justify-center shrink-0">
            <ShieldCheck className="w-5 h-5 text-emerald-500" />
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-extrabold text-foreground mb-0.5">Payment Protection</p>
            <p className="text-xs text-muted-foreground leading-relaxed">
              All payments are protected by our escrow system. Funds are only released after you successfully move in.
            </p>
          </div>
          <Button
            variant="ghost"
            size="sm"
            className="h-8 px-3 rounded-lg font-extrabold text-xs text-primary hover:bg-primary/8 shrink-0"
          >
            Learn More
          </Button>
        </div>
      )}
    </div>
  );
}