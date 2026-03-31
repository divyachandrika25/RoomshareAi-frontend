import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { matchAPI } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import toast from 'react-hot-toast';
import {
  ArrowLeft, TrendingUp, Moon, Droplets, Users,
  MapPin, IndianRupee, Sparkles, ChevronRight,
  ShieldCheck, Info, MessageCircle, Heart
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

const avatarUrl = (name) =>
  `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=200`;

// ── Section header (matches HomePage) ────────────────────────────────────────
function SectionHeader({ icon: Icon, iconBg, iconColor, title, subtitle }) {
  return (
    <div className="flex items-center gap-3">
      <div className={cn('w-9 h-9 rounded-xl flex items-center justify-center shrink-0', iconBg)}>
        <Icon className={cn('w-4 h-4', iconColor)} />
      </div>
      <div>
        <h2 className="text-base sm:text-lg font-extrabold text-foreground tracking-tight leading-tight">
          {title}
        </h2>
        {subtitle && (
          <p className="text-[10px] text-muted-foreground font-semibold uppercase tracking-wider">
            {subtitle}
          </p>
        )}
      </div>
    </div>
  );
}

export default function MatchDetailPage() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [match, setMatch] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    if (user?.email) {
      matchAPI.getMatchDetail(matchId, user.email)
        .then(res => {
          if (res.data) {
            setMatch(res.data);
            setSaved(res.data.is_favorite || false);
          }
        })
        .catch(() => {})
        .finally(() => setLoading(false));
    }
  }, [matchId, user?.email]);

  const toggleSave = async () => {
    if (!user || !match) {
      toast.error('Please login to save favorites');
      return;
    }

    try {
      const res = await matchAPI.saveFavoriteMatch(user.email, match.email);
      setSaved(res.data.saved);
      toast.success(res.data.message);
    } catch (err) {
      toast.error('Failed to update favorite');
    }
  };

  if (loading) return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
      <div className="w-10 h-10 rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
      <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">
        Analysing synergy…
      </p>
    </div>
  );

  if (!match) return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
      <div className="w-11 h-11 rounded-xl bg-muted flex items-center justify-center">
        <Info size={20} className="text-muted-foreground" />
      </div>
      <div className="text-center">
        <p className="text-sm font-extrabold text-foreground">Match not found</p>
        <p className="text-xs text-muted-foreground mt-0.5">This profile may no longer be available.</p>
      </div>
      <Button variant="outline" size="sm" onClick={() => navigate(-1)}
        className="h-9 px-5 rounded-xl font-extrabold text-xs gap-1.5">
        <ArrowLeft size={13} /> Back to Matches
      </Button>
    </div>
  );

  const score = Math.round(match.compatibility_score);

  const preferences = [
    { icon: MapPin,       label: 'Target Location', value: match.preferred_city },
    { icon: IndianRupee,  label: 'Monthly Budget',  value: match.monthly_budget ? `₹${Number(match.monthly_budget).toLocaleString()}` : null },
    { icon: Moon,         label: 'Sleep Habits',    value: match.sleep_schedule },
    { icon: Droplets,     label: 'Living Standard', value: match.cleanliness },
    { icon: Users,        label: 'Vibe Level',      value: match.social_interaction },
  ].filter(i => i.value);

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Top nav ──────────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between mt-4 sm:mt-6">
        <button
          onClick={() => navigate(-1)}
          className="inline-flex items-center gap-2 text-sm font-extrabold text-muted-foreground hover:text-foreground transition-colors group"
        >
          <span className="group-hover:-translate-x-0.5 transition-transform">
            <ArrowLeft size={16} />
          </span>
          Back to Matches
        </button>

        <button
          onClick={toggleSave}
          className={cn(
            'w-9 h-9 rounded-xl border flex items-center justify-center transition-all',
            saved
              ? 'bg-rose-500/10 border-rose-500/20 text-rose-500'
              : 'bg-card border-border/50 text-muted-foreground hover:border-rose-500/20 hover:text-rose-500'
          )}
        >
          <Heart size={15} className={saved ? 'fill-rose-500' : ''} />
        </button>
      </div>

      {/* ── Hero profile card ─────────────────────────────────────────────────── */}
      <div className="bg-card rounded-2xl border border-border/50 overflow-hidden">
        {/* Colour bar */}
        <div className="h-1.5 w-full bg-gradient-to-r from-primary via-violet-500 to-primary/40" />

        <div className="p-5 sm:p-6 lg:p-8">
          <div className="flex flex-col sm:flex-row items-center sm:items-end gap-6">
            {/* Avatar */}
            <div className="relative shrink-0">
              <img
                src={match.photo || avatarUrl(match.full_name)}
                alt=""
                className="w-24 h-24 sm:w-28 sm:h-28 rounded-2xl object-cover border-4 border-card shadow-lg"
              />
              <div className="absolute -bottom-1.5 -right-1.5 w-7 h-7 bg-primary rounded-lg border-2 border-card flex items-center justify-center shadow-md">
                <ShieldCheck size={13} className="text-white" />
              </div>
            </div>

            {/* Info */}
            <div className="flex-1 text-center sm:text-left min-w-0">
              <div className="flex flex-col sm:flex-row sm:items-center gap-2 mb-2">
                <h1 className="text-xl sm:text-2xl font-extrabold text-foreground tracking-tight leading-tight">
                  {match.full_name || 'Anonymous'}{match.age ? `, ${match.age}` : ''}
                </h1>
                <span className="inline-flex items-center gap-1 bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 text-[9px] font-extrabold uppercase tracking-wider px-2.5 py-1 rounded-md border border-emerald-500/15 self-center sm:self-auto">
                  <TrendingUp size={10} /> High compatibility
                </span>
              </div>

              <div className="flex flex-wrap items-center justify-center sm:justify-start gap-x-3 gap-y-1 text-xs font-semibold text-muted-foreground">
                {match.preferred_city && (
                  <span className="flex items-center gap-1">
                    <MapPin size={11} className="text-primary/60" />
                    {match.preferred_city}
                  </span>
                )}
                {match.email && (
                  <>
                    <span className="text-border">·</span>
                    <span className="truncate max-w-[180px]">{match.email}</span>
                  </>
                )}
              </div>

              {/* Tags */}
              {match.tags?.length > 0 && (
                <div className="flex flex-wrap gap-1.5 mt-3 justify-center sm:justify-start">
                  {match.tags.slice(0, 4).map((tag, i) => (
                    <span key={i}
                      className="text-[9px] font-extrabold uppercase tracking-wider text-primary/70 bg-primary/8 px-2 py-1 rounded-md">
                      {tag}
                    </span>
                  ))}
                </div>
              )}
            </div>

            {/* Score badge */}
            <div className="shrink-0 flex flex-col items-center gap-1 bg-primary/8 border border-primary/15 rounded-2xl px-6 py-4">
              <TrendingUp size={16} className="text-primary" />
              <p className="text-3xl sm:text-4xl font-extrabold text-primary leading-none tabular-nums">
                {score}%
              </p>
              <p className="text-[9px] font-extrabold uppercase tracking-widest text-muted-foreground">
                Harmony score
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* ── AI Synergy Analysis ───────────────────────────────────────────────── */}
      {match.ai_explanation && (
        <section className="space-y-4">
          <SectionHeader
            icon={Sparkles}
            iconBg="bg-violet-500/10"
            iconColor="text-violet-500"
            title="AI Synergy Analysis"
            subtitle="Real-time Gen-AI insight"
          />

          <div className="bg-card rounded-2xl border border-border/50 p-5 sm:p-6 relative overflow-hidden group hover:border-primary/25 hover:shadow-md hover:shadow-primary/5 transition-all duration-200">
            {/* Decorative sparkle */}
            <Sparkles size={48} className="absolute top-4 right-5 text-primary/5 group-hover:text-primary/8 transition-colors" />

            <p className="text-sm sm:text-base text-foreground font-semibold leading-relaxed italic relative z-10">
              "{match.ai_explanation}"
            </p>

            <div className="mt-4 flex items-center gap-3 relative z-10">
              <div className="h-px flex-1 bg-border/50" />
              <span className="text-[9px] font-extrabold uppercase tracking-widest text-primary/60 px-2">
                AI Generated
              </span>
              <div className="h-px flex-1 bg-border/50" />
            </div>
          </div>
        </section>
      )}

      {/* ── Preference Breakdown ──────────────────────────────────────────────── */}
      {preferences.length > 0 && (
        <section className="space-y-4">
          <SectionHeader
            icon={Info}
            iconBg="bg-primary/8"
            iconColor="text-primary"
            title="Preference Breakdown"
            subtitle="Lifestyle & living habits"
          />

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
            {preferences.map((item, i) => (
              <div
                key={i}
                className="bg-card rounded-2xl border border-border/50 hover:border-primary/25 hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 p-4 flex items-center gap-4"
              >
                <div className="w-9 h-9 rounded-xl bg-primary/8 flex items-center justify-center shrink-0 group-hover:bg-primary transition-colors">
                  <item.icon size={15} className="text-primary" />
                </div>
                <div className="min-w-0">
                  <p className="text-[9px] font-extrabold uppercase tracking-widest text-muted-foreground mb-0.5">
                    {item.label}
                  </p>
                  <p className="text-sm font-extrabold text-foreground truncate">{item.value}</p>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* ── CTA banner (matches HomePage AI CTA pattern) ─────────────────────── */}
      <section
        className="rounded-2xl border border-primary/20 bg-card p-5 sm:p-6 flex flex-col sm:flex-row items-start sm:items-center gap-5 hover:border-primary/40 hover:shadow-md hover:shadow-primary/5 transition-all"
      >
        {/* Avatar mini */}
        <div className="relative shrink-0">
          <img
            src={match.photo || avatarUrl(match.full_name)}
            alt=""
            className="w-11 h-11 rounded-xl object-cover border-2 border-card shadow-md"
          />
          <span className="absolute -bottom-0.5 -right-0.5 w-3.5 h-3.5 rounded-full bg-emerald-500 border-2 border-card" />
        </div>

        <div className="flex-1 min-w-0">
          <p className="text-sm font-extrabold text-foreground leading-tight">
            Looks like a great match!
          </p>
          <p className="text-[10px] font-semibold text-muted-foreground uppercase tracking-wider mt-0.5">
            Reach out to discuss room preferences and move-in dates
          </p>
        </div>

        <div className="flex flex-col sm:flex-row gap-2 w-full sm:w-auto shrink-0">
          <Button
            size="sm"
            variant="outline"
            className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1.5 border-border/60"
          >
            <MessageCircle size={13} /> Message
          </Button>
          <Button
            size="sm"
            onClick={() => navigate(`/dashboard/roommate/${match.email}`)}
            className="h-9 px-4 rounded-xl font-extrabold text-xs gap-1 shadow-sm shadow-primary/15 hover:-translate-y-0.5 transition-transform"
          >
            View Profile <ChevronRight size={13} />
          </Button>
        </div>
      </section>

    </div>
  );
}