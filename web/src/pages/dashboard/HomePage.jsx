import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { matchAPI, roomAPI, favoriteAPI } from '@/lib/api';
import {
  Search, MapPin, Users, Home as HomeIcon, Sparkles,
  ChevronRight, Heart, Star, ArrowRight, TrendingUp,
  Filter, CheckCircle2
} from 'lucide-react';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=200`;

// ── Skeleton cards ────────────────────────────────────────────────────────────
function RoommateSkeleton() {
  return (
    <div className="animate-pulse rounded-2xl border border-border/40 bg-card p-4 space-y-3">
      <div className="flex items-center gap-3">
        <div className="w-12 h-12 rounded-xl bg-muted shrink-0" />
        <div className="flex-1 space-y-2">
          <div className="h-3.5 bg-muted rounded-full w-3/5" />
          <div className="h-3 bg-muted rounded-full w-2/5" />
        </div>
      </div>
      <div className="flex gap-1.5">
        <div className="h-6 w-16 bg-muted rounded-md" />
        <div className="h-6 w-14 bg-muted rounded-md" />
      </div>
      <div className="h-9 bg-muted rounded-xl" />
    </div>
  );
}

function RoomSkeleton() {
  return (
    <div className="animate-pulse rounded-2xl border border-border/40 bg-card overflow-hidden">
      <div className="h-44 bg-muted" />
      <div className="p-4 space-y-2.5">
        <div className="h-3 bg-muted rounded-full w-1/4" />
        <div className="h-4 bg-muted rounded-full w-4/5" />
        <div className="h-3 bg-muted rounded-full w-3/5" />
        <div className="h-px bg-muted mt-4" />
        <div className="flex justify-between items-center pt-1">
          <div className="h-5 w-24 bg-muted rounded-full" />
          <div className="w-8 h-8 bg-muted rounded-xl" />
        </div>
      </div>
    </div>
  );
}

// ── Section header ────────────────────────────────────────────────────────────
function SectionHeader({ icon: Icon, iconBg, iconColor, title, subtitle, action }) {
  return (
    <div className="flex items-center justify-between">
      <div className="flex items-center gap-3">
        <div className={cn("w-9 h-9 rounded-xl flex items-center justify-center", iconBg)}>
          <Icon className={cn("w-4 h-4", iconColor)} />
        </div>
        <div>
          <h2 className="text-base sm:text-lg font-extrabold text-foreground tracking-tight leading-tight">
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

// ── Roommate card ─────────────────────────────────────────────────────────────
function RoommateCard({ rm, email, onClick }) {
  const [saved, setSaved] = useState(rm.is_favorite || false);
  const tags = rm.tags?.length > 0 ? rm.tags.slice(0, 3) : ['Verified', 'Searching'];

  const handleFav = async (e) => {
    e.stopPropagation();
    try {
      await favoriteAPI.save(email, rm.email);
      setSaved(!saved);
      toast.success(saved ? 'Removed' : 'Saved to favorites!');
    } catch {
      toast.error('Failed to save');
    }
  };

  return (
    <div
      onClick={onClick}
      className="group bg-card rounded-2xl p-4 border border-border/50 hover:border-primary/25 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 cursor-pointer"
    >
      {/* Avatar + name + match */}
      <div className="flex items-center gap-3 mb-3">
        <div className="relative shrink-0">
          <img
            src={rm.photos?.[0]?.image || avatarUrl(rm.full_name)}
            alt=""
            className="w-11 h-11 rounded-xl object-cover border border-border/40 group-hover:scale-105 transition-transform duration-300"
          />
          <span className="absolute -bottom-0.5 -right-0.5 w-3 h-3 rounded-full bg-emerald-500 border-2 border-card" />
        </div>
        <div className="flex-1 min-w-0">
          <p className="text-sm font-extrabold text-foreground truncate leading-tight">
            {rm.full_name || 'Premium User'}{rm.age ? `, ${rm.age}` : ''}
          </p>
          <p className="text-xs text-muted-foreground font-medium flex items-center gap-1 mt-0.5 truncate">
            <MapPin className="w-3 h-3 text-primary/60 shrink-0" />
            {rm.city || 'Chennai'}
          </p>
        </div>
        {rm.match_percentage && (
          <div className="flex flex-col items-end gap-1.5 h-full self-start">
            <button
               onClick={handleFav}
               className={cn(
                 "w-7 h-7 rounded-lg flex items-center justify-center transition-all",
                 saved ? "bg-rose-50 text-rose-500 shadow-sm" : "bg-muted/50 text-muted-foreground hover:bg-rose-50 hover:text-rose-500"
               )}
            >
               <Heart size={13} className={saved ? "fill-rose-500" : ""} />
            </button>
            <Badge className="bg-emerald-50 dark:bg-emerald-950/30 text-emerald-600 dark:text-emerald-400 border-none text-[9px] font-extrabold rounded-md px-2 py-0.5 h-auto shrink-0 flex items-center gap-1">
              <TrendingUp className="w-2.5 h-2.5" />
              {Math.round(rm.match_percentage)}%
            </Badge>
            {rm.request_status && (
              <Badge className="bg-amber-50 dark:bg-amber-950/30 text-amber-600 dark:text-amber-400 border-none text-[9px] font-extrabold rounded-md px-2 py-0.5 h-auto shrink-0 flex items-center gap-1 uppercase tracking-tighter">
                {rm.request_status}
              </Badge>
            )}
          </div>
        )}
      </div>

      {/* Tags */}
      <div className="flex flex-wrap gap-1.5 mb-3">
        {tags.map(tag => (
          <span
            key={tag}
            className="text-[9px] font-extrabold uppercase tracking-wider text-primary/70 bg-primary/8 px-2 py-1 rounded-md"
          >
            {tag}
          </span>
        ))}
      </div>

      {/* CTA */}
      <Button
        size="sm"
        className="w-full h-8 rounded-xl font-extrabold text-xs bg-muted/40 text-primary border-none shadow-none hover:bg-primary hover:text-white transition-all"
      >
        Chat Now
      </Button>
    </div>
  );
}

// ── Room card ─────────────────────────────────────────────────────────────────
function RoomCard({ room, email, onClick }) {

  return (
    <div
      onClick={onClick}
      className="group bg-card rounded-2xl overflow-hidden border border-border/50 hover:border-primary/20 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 cursor-pointer flex flex-col"
    >
      {/* Image */}
      <div className="relative h-44 overflow-hidden">
        <img
          src={room.photos?.[0]?.image || 'https://images.unsplash.com/photo-1522770179533-24471fcdba45?w=500&auto=format'}
          alt=""
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />

        {/* Verified badge */}
        <div className="absolute top-3 left-3">
          <span className="flex items-center gap-1 text-[9px] font-extrabold uppercase tracking-wider bg-white/95 text-emerald-600 rounded-md px-2 py-1 shadow-sm">
            <CheckCircle2 className="w-3 h-3" /> Verified
          </span>
        </div>
      </div>

      {/* Details */}
      <div className="p-4 flex-1 flex flex-col">
        <div className="flex items-center justify-between mb-1">
          <p className="text-[9px] font-extrabold text-primary uppercase tracking-widest">
            {room.type || 'Apartment'}
          </p>
          <div className="flex items-center gap-1 text-xs font-extrabold text-foreground">
            <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
            {room.rating || '4.9'}
          </div>
        </div>

        <h3 className="text-sm font-extrabold text-foreground leading-snug mb-1 group-hover:text-primary transition-colors">
          {room.apartment_title}
        </h3>
        <p className="text-xs text-muted-foreground flex items-center gap-1 font-medium">
          <MapPin className="w-3 h-3 text-primary/60 shrink-0" />
          <span className="truncate">{room.address || room.city || 'Chennai'}</span>
        </p>

        <div className="mt-auto pt-3 border-t border-border/40 flex items-center justify-between">
          <div>
            <p className="text-[9px] text-muted-foreground font-extrabold uppercase tracking-wider">Monthly</p>
            <p className="text-base font-extrabold text-primary tabular-nums leading-tight">
              ₹{room.monthly_rent}
              <span className="text-xs font-semibold text-muted-foreground">/mo</span>
            </p>
          </div>
          <div className="w-8 h-8 rounded-xl bg-primary/8 flex items-center justify-center group-hover:bg-primary transition-colors">
            <ArrowRight className="w-3.5 h-3.5 text-primary group-hover:text-white transition-colors" />
          </div>
        </div>
      </div>
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [roommates, setRoommates] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!email) return;
    (async () => {
      setLoading(true);
      try {
        const [rmRes, roomRes] = await Promise.all([
          matchAPI.discoverRoommates(email),
          roomAPI.getHomeRooms(email),
        ]);
        if (rmRes.data?.roommates) setRoommates(rmRes.data.roommates);
        if (roomRes.data?.rooms) setRooms(roomRes.data.rooms);
      } catch {}
      setLoading(false);
    })();
  }, [email]);

  const q = search.toLowerCase();
  const filteredRoommates = q
    ? roommates.filter(r => (r.full_name || '').toLowerCase().includes(q))
    : roommates;
  const filteredRooms = q
    ? rooms.filter(r => (r.apartment_title || '').toLowerCase().includes(q))
    : rooms;

  const displayName = user?.fullName || user?.email?.split('@')[0] || 'there';

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Hero ─────────────────────────────────────────────────────────────── */}
      <section
        className="relative overflow-hidden rounded-2xl text-white py-10 sm:py-14 px-6 sm:px-12 mt-4 sm:mt-6"
        style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 60%, #080f1e 100%)` }}
      >
        {/* Decorative grid */}
        <div
          className="absolute inset-0 opacity-[0.06]"
          style={{
            backgroundImage: `repeating-linear-gradient(0deg,transparent,transparent 28px,#fff 28px,#fff 29px),
                              repeating-linear-gradient(90deg,transparent,transparent 28px,#fff 28px,#fff 29px)`
          }}
        />
        {/* Glow */}
        <div className="absolute -top-16 -right-16 w-64 h-64 bg-blue-400/20 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-12 -left-12 w-48 h-48 bg-indigo-600/15 rounded-full blur-2xl pointer-events-none" />

        <div className="relative z-10 max-w-2xl mx-auto text-center space-y-5">
          <div className="inline-flex items-center gap-1.5 bg-white/10 border border-white/15 rounded-full px-3 py-1.5">
            <Sparkles className="w-3 h-3 text-blue-200" />
            <span className="text-[10px] font-extrabold uppercase tracking-widest text-blue-100">
              AI-Powered Matching
            </span>
          </div>

          <h1 className="text-2xl sm:text-4xl font-extrabold leading-tight tracking-tight">
            Hey {displayName} 👋<br />
            Find your perfect{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-200 to-white">
              roommate & home.
            </span>
          </h1>

          <p className="text-sm sm:text-base text-blue-100/70 font-medium max-w-md mx-auto leading-relaxed">
            RoomShare AI connects you with compatible people based on lifestyle, preferences, and location.
          </p>

          {/* Search bar */}
          <div className="relative max-w-lg mx-auto">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-blue-200/60 pointer-events-none" />
            <input
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search by area, name, or budget…"
              className="w-full pl-11 pr-4 py-3.5 rounded-xl bg-white/10 border border-white/15 text-white placeholder:text-blue-200/50 text-sm font-medium focus:outline-none focus:bg-white/15 focus:border-white/30 transition-all"
            />
          </div>
        </div>
      </section>

      {/* ── Roommate matches ──────────────────────────────────────────────────── */}
      <section className="space-y-4">
        <SectionHeader
          icon={Users}
          iconBg="bg-primary/8"
          iconColor="text-primary"
          title="Top Roommate Matches"
          subtitle="Based on your compatibility profile"
          action={
            <Button
              variant="ghost"
              size="sm"
              onClick={() => navigate('/dashboard/matches')}
              className="h-8 px-3 rounded-xl text-primary hover:bg-primary/8 font-extrabold text-xs gap-1 group"
            >
              View All
              <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-0.5 transition-transform" />
            </Button>
          }
        />

        {loading ? (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {[...Array(3)].map((_, i) => <RoommateSkeleton key={i} />)}
          </div>
        ) : filteredRoommates.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-14 px-4 text-center space-y-3 rounded-2xl border border-border/50 bg-card">
            <div className="w-11 h-11 rounded-xl bg-muted flex items-center justify-center">
              <Users className="w-5 h-5 text-muted-foreground" />
            </div>
            <div>
              <p className="text-sm font-extrabold text-foreground">No roommates found</p>
              <p className="text-xs text-muted-foreground mt-0.5">
                {search ? 'Try a different search term.' : 'Complete your profile for better recommendations.'}
              </p>
            </div>
            {search && (
              <Button variant="outline" size="sm" onClick={() => setSearch('')} className="h-8 px-4 rounded-xl font-extrabold text-xs">
                Clear Search
              </Button>
            )}
          </div>
        ) : (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {filteredRoommates.slice(0, 3).map((rm, idx) => (
              <RoommateCard
                key={rm.id || rm.email || idx}
                rm={rm}
                email={email}
                onClick={() => navigate(`/dashboard/roommate/${rm.email}`)}
              />
            ))}
          </div>
        )}
      </section>

      {/* ── Rooms ─────────────────────────────────────────────────────────────── */}
      <section className="space-y-4">
        <SectionHeader
          icon={HomeIcon}
          iconBg="bg-amber-50 dark:bg-amber-950/30"
          iconColor="text-amber-500"
          title="Verified Listings"
          subtitle="Top rated housing near you"
          action={
            <Button
              variant="outline"
              size="sm"
              className="h-8 px-3 rounded-xl font-extrabold text-xs gap-1.5 border-border/60"
            >
              <Filter className="w-3.5 h-3.5" /> Filter
            </Button>
          }
        />

        {loading ? (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {[...Array(3)].map((_, i) => <RoomSkeleton key={i} />)}
          </div>
        ) : filteredRooms.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-14 px-4 text-center space-y-3 rounded-2xl border border-border/50 bg-card">
            <div className="w-11 h-11 rounded-xl bg-muted flex items-center justify-center">
              <HomeIcon className="w-5 h-5 text-muted-foreground" />
            </div>
            <div>
              <p className="text-sm font-extrabold text-foreground">No listings found</p>
              <p className="text-xs text-muted-foreground mt-0.5">
                {search ? 'Try a different search.' : 'Check back soon for new listings.'}
              </p>
            </div>
          </div>
        ) : (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {filteredRooms.map((room, idx) => (
              <RoomCard
                key={room.id || idx}
                room={room}
                email={email}
                onClick={() => navigate(`/dashboard/room/${room.id}`)}
              />
            ))}
          </div>
        )}
      </section>

      {/* ── AI CTA banner ─────────────────────────────────────────────────────── */}
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