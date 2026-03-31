import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { hotelAPI } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import {
  Building2, Star, MapPin, Search, Sparkles, ArrowRight,
  Loader2, Heart, Filter
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import toast from 'react-hot-toast';

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Skeleton card ─────────────────────────────────────────────────────────────
function HotelSkeleton() {
  return (
    <div className="animate-pulse rounded-2xl border border-border/40 bg-card overflow-hidden">
      <div className="h-44 bg-muted" />
      <div className="p-4 space-y-2.5">
        <div className="h-4 bg-muted rounded-full w-3/4" />
        <div className="h-3 bg-muted rounded-full w-1/2" />
        <div className="flex gap-1.5 mt-3">
          <div className="h-6 w-14 bg-muted rounded-md" />
          <div className="h-6 w-16 bg-muted rounded-md" />
        </div>
        <div className="h-px bg-muted mt-3" />
        <div className="flex justify-between items-center pt-1">
          <div className="h-5 w-20 bg-muted rounded-full" />
          <div className="w-8 h-8 bg-muted rounded-xl" />
        </div>
      </div>
    </div>
  );
}

// ── Hotel card ────────────────────────────────────────────────────────────────
function HotelCard({ hotel, isRec = false, onClick, user }) {
  return (
    <div
      onClick={onClick}
      className="group bg-card rounded-2xl overflow-hidden border border-border/50 hover:border-primary/25 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 cursor-pointer flex flex-col"
    >
      {/* Image */}
      <div className="relative h-44 overflow-hidden bg-muted">
        {hotel.image_url ? (
          <img
            src={hotel.image_url}
            alt={hotel.name}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
          />
        ) : (
          <div className="w-full h-full flex flex-col items-center justify-center text-muted-foreground/30">
            <Building2 size={42} />
          </div>
        )}
        <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />

        {/* Rating badge */}
        {hotel.rating && (
          <div className="absolute top-3 left-3">
            <span className="flex items-center gap-1 text-xs font-extrabold bg-white/95 text-foreground rounded-lg px-2 py-1 shadow-sm">
              <Star size={12} className="fill-amber-400 text-amber-400" />
              {hotel.rating}
            </span>
          </div>
        )}

        {/* Recommended badge */}
        {isRec && (
          <div className="absolute top-3 right-3">
            <span className="flex items-center gap-1 text-[9px] font-extrabold uppercase tracking-wider bg-gradient-to-r from-violet-500 to-pink-500 text-white rounded-md px-2 py-1 shadow">
              <Sparkles size={10} /> For You
            </span>
          </div>
        )}

        {/* Price */}
        <div className="absolute bottom-3 right-3 bg-black/70 backdrop-blur-sm text-white px-2.5 py-1.5 rounded-lg">
          <span className="text-sm font-extrabold">₹{hotel.starting_price?.toLocaleString()}</span>
          <span className="text-[10px] opacity-70">/night</span>
        </div>
      </div>

      {/* Details */}
      <div className="p-4 flex-1 flex flex-col">
        <h3 className="text-sm font-extrabold text-foreground leading-snug group-hover:text-primary transition-colors truncate">
          {hotel.name}
        </h3>
        <p className="text-xs text-muted-foreground flex items-center gap-1 mt-1 font-medium">
          <MapPin className="w-3 h-3 text-primary/60 shrink-0" />
          <span className="truncate">{hotel.city}</span>
          {hotel.stars && (
            <>
              <span className="mx-0.5">·</span>
              <span className="flex items-center gap-0.5">
                {Array.from({ length: Math.min(Math.round(hotel.stars), 5) }, (_, i) => (
                  <Star key={i} size={10} className="fill-amber-400 text-amber-400" />
                ))}
              </span>
            </>
          )}
        </p>

        {/* Amenities */}
        {hotel.amenities?.length > 0 && (
          <div className="flex flex-wrap gap-1 mt-2.5">
            {hotel.amenities.slice(0, 3).map((a, i) => (
              <span key={i} className="text-[9px] font-extrabold uppercase tracking-wider text-primary/70 bg-primary/8 px-2 py-0.5 rounded-md">
                {a.trim()}
              </span>
            ))}
            {hotel.amenities.length > 3 && (
              <span className="text-[9px] text-muted-foreground font-semibold px-1 py-0.5">
                +{hotel.amenities.length - 3}
              </span>
            )}
          </div>
        )}

        {/* Reason tag for recommended */}
        {isRec && hotel.reason && (
          <p className="text-[10px] text-violet-500 font-semibold italic mt-2 truncate">{hotel.reason}</p>
        )}

        {/* Footer */}
        <div className="mt-auto pt-3 border-t border-border/40 flex items-center justify-between">
          <p className="text-xs text-muted-foreground font-medium">
            {hotel.total_rooms} room{hotel.total_rooms > 1 ? 's' : ''}
          </p>
          <div className="w-8 h-8 rounded-xl bg-primary/8 flex items-center justify-center group-hover:bg-primary transition-colors">
            <ArrowRight className="w-3.5 h-3.5 text-primary group-hover:text-white transition-colors" />
          </div>
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
          <h2 className="text-base sm:text-lg font-extrabold text-foreground tracking-tight leading-tight">{title}</h2>
          <p className="text-[10px] text-muted-foreground font-semibold uppercase tracking-wider">{subtitle}</p>
        </div>
      </div>
      {action}
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function HotelListPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [hotels, setHotels] = useState([]);
  const [recommended, setRecommended] = useState([]);
  const [userAreas, setUserAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchHotels();
  }, [email]);

  const fetchHotels = async (params = {}) => {
    try {
      setLoading(true);
      const res = await hotelAPI.getAll({ ...params, email });
      setHotels(res.data.hotels || []);
      setRecommended(res.data.recommended || []);
      setUserAreas(res.data.user_areas || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    fetchHotels({ search });
  };

  const allResults = [...recommended, ...hotels];
  const q = search.toLowerCase();
  const filtered = q && !loading
    ? allResults.filter(h => h.name.toLowerCase().includes(q) || (h.city || '').toLowerCase().includes(q))
    : null;

  const navigateToHotel = (h) => {
    if (h.is_external) {
      navigate(`/dashboard/hotel/${h.id}`, {
        state: {
          externalHotel: {
            title: h.name,
            address: h.address,
            city: h.city,
            rating: h.rating,
            stars: h.stars,
            price: h.starting_price,
            phone: h.phone,
            website: h.website,
            image: h.image_url,
            amenities: h.amenities || [],
          }
        }
      });
    } else {
      navigate(`/dashboard/hotel/${h.id}`);
    }
  };

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header ──────────────────────────────────────────────────────────── */}
      <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-3 mt-4 sm:mt-6">
        <div>
          <h1 className="text-xl sm:text-2xl font-extrabold text-foreground tracking-tight">
            Hotels & Stays
          </h1>
          <p className="text-sm text-muted-foreground mt-0.5">
            {userAreas.length > 0
              ? <>Personalized for <span className="font-semibold text-primary">{userAreas.join(', ')}</span></>
              : 'Find and book rooms at premium hotels'
            }
          </p>
        </div>

        {/* Inline search */}
        <div className="relative max-w-sm w-full sm:w-auto">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground pointer-events-none" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
            placeholder="Search hotels, cities…"
            className="w-full sm:w-72 pl-10 pr-20 py-2.5 rounded-xl bg-card border border-border/60 text-sm font-medium text-foreground placeholder:text-muted-foreground/60 focus:outline-none focus:border-primary/30 focus:ring-2 focus:ring-primary/10 transition-all"
          />
          <Button
            size="sm"
            onClick={handleSearch}
            className="absolute right-1.5 top-1/2 -translate-y-1/2 h-7 px-3 rounded-lg text-xs font-extrabold"
          >
            Search
          </Button>
        </div>
      </div>

      {/* ── Loading state ───────────────────────────────────────────────────── */}
      {loading ? (
        <div className="space-y-8">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {[...Array(6)].map((_, i) => <HotelSkeleton key={i} />)}
          </div>
        </div>
      ) : filtered ? (
        /* ── Filtered search results ─────────────────────────────────────── */
        <section className="space-y-4">
          <p className="text-sm text-muted-foreground font-medium">
            {filtered.length} result{filtered.length !== 1 ? 's' : ''} for "<span className="font-semibold text-foreground">{search}</span>"
          </p>
          {filtered.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-14 px-4 text-center space-y-3 rounded-2xl border border-border/50 bg-card">
              <Building2 size={42} className="text-muted-foreground/30" />
              <p className="text-sm font-extrabold text-foreground">No hotels found</p>
              <p className="text-xs text-muted-foreground">Try a different search term or city.</p>
              <Button variant="outline" size="sm" onClick={() => { setSearch(''); }} className="h-8 px-4 rounded-xl font-extrabold text-xs">
                Clear Search
              </Button>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
              {filtered.map(h => (
                <HotelCard
                  key={h.id}
                  hotel={h}
                  user={user}
                  isRec={!!h.reason}
                  onClick={() => navigateToHotel(h)}
                />
              ))}
            </div>
          )}
        </section>
      ) : (
        <>
          {/* ── Recommended section ──────────────────────────────────────── */}
          {recommended.length > 0 && (
            <section className="space-y-4">
              <SectionHeader
                icon={Sparkles}
                iconBg="bg-violet-50 dark:bg-violet-950/30"
                iconColor="text-violet-500"
                title="Recommended for You"
                subtitle={`Based on your area: ${userAreas.join(', ')}`}
              />
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
                {recommended.map(h => (
                  <HotelCard
                    key={`rec-${h.id}`}
                    hotel={h}
                    user={user}
                    isRec
                    onClick={() => navigateToHotel(h)}
                  />
                ))}
              </div>
            </section>
          )}

          {/* ── All hotels section ──────────────────────────────────────── */}
          <section className="space-y-4">
            <SectionHeader
              icon={Building2}
              iconBg="bg-primary/8"
              iconColor="text-primary"
              title="All Hotels"
              subtitle={`${hotels.length} hotel${hotels.length !== 1 ? 's' : ''} available`}
            />

            {hotels.length === 0 && recommended.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-14 px-4 text-center space-y-3 rounded-2xl border border-border/50 bg-card">
                <Building2 size={42} className="text-muted-foreground/30" />
                <p className="text-sm font-extrabold text-foreground">No hotels available</p>
                <p className="text-xs text-muted-foreground">New listings are added regularly. Check back soon!</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
                {hotels.map(h => (
                  <HotelCard
                    key={h.id}
                    hotel={h}
                    user={user}
                    onClick={() => navigateToHotel(h)}
                  />
                ))}
              </div>
            )}
          </section>
        </>
      )}
    </div>
  );
}