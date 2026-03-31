import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { matchAPI } from '@/lib/api';
import {
  Users, Sparkles, ArrowRight, Search, TrendingUp,
  MapPin, MessageSquare, ShieldCheck, Heart, Filter
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=200`;

// ── Skeleton ──────────────────────────────────────────────────────────────────
function MatchSkeleton() {
  return (
    <div className="animate-pulse bg-card rounded-2xl border border-border/40 p-4 space-y-3">
      <div className="flex items-center gap-3">
        <div className="w-12 h-12 rounded-xl bg-muted shrink-0" />
        <div className="flex-1 space-y-2">
          <div className="h-3.5 bg-muted rounded-full w-3/5" />
          <div className="h-3 bg-muted rounded-full w-2/5" />
          <div className="h-5 bg-muted rounded-full w-1/3" />
        </div>
      </div>
      <div className="h-16 bg-muted rounded-xl" />
      <div className="flex gap-2 pt-1">
        <div className="h-9 flex-1 bg-muted rounded-xl" />
        <div className="h-9 flex-1 bg-muted rounded-xl" />
      </div>
    </div>
  );
}

// ── Section header ────────────────────────────────────────────────────────────
function SectionHeader({ icon: Icon, iconBg, iconColor, title, subtitle, action }) {
  return (
    <div className="flex items-center justify-between">
      <div className="flex items-center gap-3">
        <div className={cn('w-9 h-9 rounded-xl flex items-center justify-center shrink-0', iconBg)}>
          <Icon className={cn('w-4 h-4', iconColor)} />
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

// ── Match card ────────────────────────────────────────────────────────────────
function MatchCard({ match, score, email, onClick }) {
  const [saved, setSaved] = useState(match.is_favorite || false);

  const scoreBg =
    score >= 85 ? 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/15' :
    score >= 70 ? 'bg-primary/8 text-primary border-primary/15' :
                  'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/15';

  return (
    <div
      onClick={onClick}
      className="group bg-card rounded-2xl overflow-hidden border border-border/50 hover:border-primary/25 hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 cursor-pointer flex flex-col"
    >
      <div className="p-4 flex-1 flex flex-col">
        {/* Avatar row */}
        <div className="flex items-start gap-3 mb-3">
          <div className="relative shrink-0">
            <img
              src={match.photo || avatarUrl(match.full_name)}
              alt=""
              className="w-11 h-11 rounded-xl object-cover border-2 border-card shadow-md group-hover:scale-105 transition-transform duration-300"
            />
            <span className="absolute -bottom-0.5 -right-0.5 w-3.5 h-3.5 rounded-full bg-emerald-500 border-2 border-card" />
          </div>

          <div className="flex-1 min-w-0">
            <h3 className="text-sm font-extrabold text-foreground truncate group-hover:text-primary transition-colors pr-6">
              {match.full_name || 'Anonymous User'}
            </h3>
            <p className="text-xs text-muted-foreground font-medium flex items-center gap-1 mt-0.5 truncate">
              <MapPin size={11} className="text-primary/60 shrink-0" />
              {match.preferred_city || 'Location not set'}
            </p>
            {/* Score badge */}
            <span className={cn(
              'inline-flex items-center gap-1 mt-1.5 text-[9px] font-extrabold uppercase tracking-wider px-2 py-1 rounded-md border',
              scoreBg
            )}>
              <TrendingUp size={10} /> {score}% match
            </span>
          </div>

          <button
            onClick={async (e) => {
              e.stopPropagation();
              try {
                await favoriteAPI.save(email, match.email);
                setSaved(!saved);
                toast.success(saved ? "Removed from favorites" : "Saved to favorites!");
              } catch {
                toast.error("Failed to update favorites");
              }
            }}
            className={cn(
              'w-7 h-7 rounded-lg flex items-center justify-center transition-all shrink-0',
              saved
                ? 'bg-rose-500/10 text-rose-500'
                : 'bg-muted/50 text-muted-foreground hover:bg-rose-500/10 hover:text-rose-500'
            )}
          >
            <Heart size={13} className={saved ? 'fill-rose-500' : ''} />
          </button>
        </div>

        {/* AI insight */}
        {match.ai_explanation && (
          <div className="bg-muted/40 rounded-xl p-3 mb-3 border border-border/40">
            <div className="flex items-center gap-1.5 mb-1.5">
              <Sparkles size={11} className="text-primary" />
              <span className="text-[9px] font-extrabold uppercase tracking-widest text-primary">AI Insight</span>
            </div>
            <p className="text-[11px] text-muted-foreground leading-relaxed font-medium italic line-clamp-2">
              "{match.ai_explanation}"
            </p>
          </div>
        )}

        {/* Tags */}
        {match.tags?.length > 0 && (
          <div className="flex flex-wrap gap-1.5 mb-3">
            {match.tags.slice(0, 3).map((tag, i) => (
              <span key={i}
                className="text-[9px] font-extrabold uppercase tracking-wider text-primary/70 bg-primary/8 px-2 py-1 rounded-md">
                {tag}
              </span>
            ))}
          </div>
        )}

        {/* Actions */}
        <div className="mt-auto flex gap-2 pt-3 border-t border-border/40">
          <Button
            variant="ghost"
            size="sm"
            onClick={(e) => e.stopPropagation()}
            className="flex-1 h-8 rounded-xl font-extrabold text-xs gap-1.5 hover:bg-primary/8 text-primary"
          >
            <MessageSquare size={13} /> Message
          </Button>
          <Button
            size="sm"
            className="flex-1 h-8 rounded-xl font-extrabold text-xs gap-1 bg-muted/40 text-primary border-none shadow-none hover:bg-primary hover:text-white transition-all"
          >
            Profile <ArrowRight size={12} />
          </Button>
        </div>
      </div>
    </div>
  );
}

// ── Main page ─────────────────────────────────────────────────────────────────
export default function MatchesPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');

  useEffect(() => {
    if (!email) return;
    matchAPI.getMatches(email)
      .then(res => { if (res.data?.matches) setMatches(res.data.matches); })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [email]);

  const filtered = search
    ? matches.filter(m => (m.full_name || '').toLowerCase().includes(search.toLowerCase()))
    : matches;

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header ──────────────────────────────────────────────────────────── */}
      <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-2 mt-4 sm:mt-6">
        <div>
          <div className="inline-flex items-center gap-1.5 bg-primary/8 border border-primary/15 rounded-full px-3 py-1.5 mb-3">
            <Sparkles size={11} className="text-primary" />
            <span className="text-[10px] font-extrabold uppercase tracking-widest text-primary">
              AI-Powered Matching
            </span>
          </div>
          <h1 className="text-xl sm:text-2xl font-extrabold text-foreground tracking-tight">
            Your Compatibility Matches
          </h1>
          <p className="text-[10px] text-muted-foreground font-semibold uppercase tracking-wider mt-1">
            People who share your lifestyle and preferences
          </p>
        </div>
        {!loading && filtered.length > 0 && (
          <p className="text-xs font-extrabold text-muted-foreground shrink-0">
            {filtered.length} match{filtered.length !== 1 ? 'es' : ''}
          </p>
        )}
      </div>

      {/* ── Search ──────────────────────────────────────────────────────────── */}
      <div className="bg-card rounded-2xl p-3 sm:p-4 border border-border/50 flex gap-2 sm:gap-3 shadow-sm">
        <div className="flex-1 relative">
          <Search size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
          <input
            type="text"
            placeholder="Search matches by name…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2.5 rounded-xl bg-background border border-input text-foreground placeholder:text-muted-foreground/50 focus:border-primary focus:ring-2 focus:ring-primary/15 outline-none text-sm font-medium transition-colors"
          />
        </div>
        <Button
          variant="outline"
          size="sm"
          className="h-10 px-4 rounded-xl font-extrabold text-xs gap-1.5 border-border/60 shrink-0"
        >
          <Filter size={13} /> Filter
        </Button>
      </div>

      {/* ── Matches grid ────────────────────────────────────────────────────── */}
      <section className="space-y-4">
        <SectionHeader
          icon={Users}
          iconBg="bg-primary/8"
          iconColor="text-primary"
          title="All Matches"
          subtitle="Ranked by compatibility score"
          action={null}
        />

        {loading ? (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {[...Array(6)].map((_, i) => <MatchSkeleton key={i} />)}
          </div>
        ) : filtered.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-14 px-4 text-center space-y-3 rounded-2xl border border-border/50 bg-card">
            <div className="w-11 h-11 rounded-xl bg-muted flex items-center justify-center">
              <Users size={20} className="text-muted-foreground" />
            </div>
            <div>
              <p className="text-sm font-extrabold text-foreground">No matches found</p>
              <p className="text-xs text-muted-foreground mt-0.5">
                {search
                  ? 'Try a different name.'
                  : 'Complete your profile to help our AI find your perfect roommate.'}
              </p>
            </div>
            {!search && (
              <Button
                size="sm"
                onClick={() => navigate('/dashboard/profile')}
                className="h-8 px-5 rounded-xl font-extrabold text-xs gap-1.5 shadow-sm shadow-primary/15"
              >
                Update Profile <ArrowRight size={12} />
              </Button>
            )}
            {search && (
              <Button
                variant="outline"
                size="sm"
                onClick={() => setSearch('')}
                className="h-8 px-4 rounded-xl font-extrabold text-xs"
              >
                Clear Search
              </Button>
            )}
          </div>
        ) : (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {filtered.map((match, idx) => {
              const score = Math.min(100, Math.round(match.compatibility_score));
              return (
                <MatchCard
                  key={match.id || `match-${idx}`}
                  match={match}
                  score={score}
                  email={email}
                  onClick={() => navigate(`/dashboard/match/${match.id}`)}
                />
              );
            })}
          </div>
        )}
      </section>
    </div>
  );
}