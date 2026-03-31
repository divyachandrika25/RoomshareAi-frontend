import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { matchAPI, favoriteAPI, chatAPI } from '@/lib/api';
import {
  ArrowLeft, Heart, MessageCircle, Sparkles, MapPin,
  Briefcase, DollarSign, Calendar, Moon, Droplets,
  Users, TrendingUp, Shield, Star, Share2, AlertCircle, Info,
  CheckCircle2, Zap, Home
} from 'lucide-react';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
} from "@/components/ui/dialog";
import { cn } from "@/lib/utils";

// ── Palette tokens (mirrors your CSS vars but as inline fallbacks) ──────────
const P = {
  blue: '#1E63FF',
  blueDark: '#0d1e3c',
  emerald: '#10b981',
  rose: '#f43f5e',
  amber: '#f59e0b',
};

// ── Tiny reusable atoms ──────────────────────────────────────────────────────

function SectionHeading({ icon: Icon, children, color = 'text-primary' }) {
  return (
    <div className="flex items-center gap-2.5 mb-5">
      <div className={cn("w-8 h-8 rounded-lg flex items-center justify-center bg-primary/8", color)}>
        <Icon className="w-4 h-4" />
      </div>
      <h2 className="text-base font-extrabold text-foreground tracking-tight">{children}</h2>
    </div>
  );
}

function InfoPill({ icon: Icon, label, value }) {
  if (!value) return null;
  return (
    <div className="flex items-center gap-3 p-4 bg-card rounded-xl border border-border/50 hover:border-primary/30 hover:shadow-md hover:shadow-primary/5 transition-all duration-200 group">
      <div className="w-9 h-9 rounded-lg bg-muted flex items-center justify-center shrink-0 group-hover:bg-primary/10 transition-colors">
        <Icon className="w-4 h-4 text-muted-foreground group-hover:text-primary transition-colors" />
      </div>
      <div className="min-w-0">
        <p className="text-[9px] text-muted-foreground font-extrabold uppercase tracking-[0.15em] leading-none mb-1">{label}</p>
        <p className="text-sm font-semibold text-foreground truncate">{value}</p>
      </div>
    </div>
  );
}

function ScoreBar({ label, value, color = 'bg-primary' }) {
  return (
    <div className="space-y-1.5">
      <div className="flex justify-between items-center">
        <span className="text-xs font-bold text-foreground">{label}</span>
        <span className="text-xs font-extrabold text-primary tabular-nums">{value}%</span>
      </div>
      <div className="h-1.5 w-full bg-muted rounded-full overflow-hidden">
        <div
          className={cn("h-full rounded-full transition-all duration-700", color)}
          style={{ width: `${value}%` }}
        />
      </div>
    </div>
  );
}

// ── Loading skeleton ──────────────────────────────────────────────────────────

function LoadingSkeleton() {
  return (
    <div className="max-w-6xl mx-auto px-4 py-8 space-y-6 animate-pulse">
      <div className="h-8 w-24 bg-muted rounded-xl" />
      <div className="h-56 sm:h-72 bg-muted rounded-2xl" />
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-3">
        {[...Array(4)].map((_, i) => <div key={i} className="h-20 bg-muted rounded-xl" />)}
      </div>
    </div>
  );
}

// ── AI Dialog ────────────────────────────────────────────────────────────────

function AIDialog({ open, onClose, compatibility }) {
  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="rounded-2xl max-w-lg w-[calc(100%-2rem)] mx-auto p-0 overflow-hidden border-none shadow-2xl">
        {/* Header band */}
        <div
          className="relative overflow-hidden p-6 sm:p-8 text-white text-center"
          style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
        >
          {/* Decorative circles */}
          <div className="absolute -top-10 -right-10 w-40 h-40 rounded-full bg-white/10 blur-2xl pointer-events-none" />
          <div className="absolute -bottom-6 -left-6 w-28 h-28 rounded-full bg-white/5 blur-xl pointer-events-none" />

          <div className="relative z-10">
            <div className="w-12 h-12 rounded-2xl bg-white/15 border border-white/20 flex items-center justify-center mx-auto mb-4">
              <Sparkles className="w-6 h-6" />
            </div>
            <p className="text-xs font-extrabold uppercase tracking-[0.2em] text-blue-200 mb-2">AI Harmony Insight</p>
            <p className="text-5xl sm:text-6xl font-black tabular-nums leading-none">
              {Math.round(compatibility?.total_match || 0)}
              <span className="text-2xl text-blue-200">%</span>
            </p>
            <p className="text-xs text-blue-100 mt-2 font-semibold uppercase tracking-widest">Co-living Compatibility</p>
          </div>
        </div>

        {/* Body */}
        <div className="p-5 sm:p-7 space-y-5 bg-card max-h-[55vh] overflow-y-auto">
          {compatibility?.headline && (
            <p className="text-sm text-muted-foreground font-medium leading-relaxed italic text-center border-l-4 border-primary/30 pl-4">
              "{compatibility.headline}"
            </p>
          )}

          {compatibility?.breakdown?.map((b, i) => (
            <div key={i} className="space-y-2">
              <div className="flex items-center justify-between gap-2">
                <p className="text-xs font-extrabold text-foreground uppercase tracking-wide">{b.title}</p>
                <Badge className="bg-primary/10 text-primary border-none font-extrabold text-[10px] rounded-md px-2">{b.score}%</Badge>
              </div>
              <div className="h-2 w-full bg-muted rounded-full overflow-hidden">
                <div
                  className="h-full rounded-full"
                  style={{ width: `${b.score}%`, background: `linear-gradient(90deg, ${P.blue}, ${P.blue}99)` }}
                />
              </div>
              <p className="text-xs text-muted-foreground leading-relaxed">{b.note}</p>
            </div>
          ))}

          {compatibility?.conflict_detection && (
            <div className="bg-rose-50 dark:bg-rose-950/20 rounded-xl p-4 border border-rose-100 dark:border-rose-900/30">
              <div className="flex items-center gap-2 mb-1.5">
                <AlertCircle className="w-4 h-4 text-rose-500 shrink-0" />
                <p className="text-xs font-extrabold text-rose-700 dark:text-rose-400 uppercase tracking-wide">
                   Security Protocol: {compatibility.conflict_detection.title}
                </p>
              </div>
              <p className="text-xs text-rose-600 dark:text-rose-400 leading-relaxed font-semibold">
                {compatibility.conflict_detection.message}
              </p>
            </div>
          )}
        </div>

        <div className="px-5 pb-5 sm:px-7 sm:pb-7">
          <Button
            onClick={onClose}
            className="w-full h-11 rounded-xl font-extrabold text-sm shadow-lg shadow-primary/20"
          >
            Acknowledge Insight
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}

// ── Main Page ────────────────────────────────────────────────────────────────

export default function RoommateProfilePage() {
  const { targetEmail } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [profile, setProfile] = useState(null);
  const [compatibility, setCompatibility] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showAI, setShowAI] = useState(false);
  const [favorited, setFavorited] = useState(false);

  useEffect(() => {
    if (!email || !targetEmail) return;
    matchAPI.getRoommateProfile(email, targetEmail)
      .then(res => {
         if (res.data?.data) {
            setProfile(res.data.data);
            if (res.data.data.is_favorite) setFavorited(true);
         }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [email, targetEmail]);

  const fetchAI = async () => {
    if (compatibility) { setShowAI(true); return; }
    try {
      const res = await matchAPI.getAICompatibility(email, targetEmail);
      if (res.data?.data) { setCompatibility(res.data.data); setShowAI(true); }
    } catch { toast.error('Failed to load AI report'); }
  };

  const handleFavorite = async () => {
    try {
      await favoriteAPI.save(email, targetEmail);
      setFavorited(!favorited);
      toast.success(favorited ? 'Removed from saved' : 'Added to favorites!');
    } catch { toast.error('Action failed'); }
  };

  const handleRequestRoomshare = async () => {
    try {
      const res = await matchAPI.requestRoomshare(email, targetEmail);
      toast.success(res.data.message || 'Room share connection request sent!');
      
      // If the backend created a direct connection, navigate to chat
      if (res.data.type === 'direct_connection' || res.data.chat_id) {
         setTimeout(() => navigate(`/dashboard/chat/${res.data.chat_id}`), 1000);
      }
    } catch { toast.error('Request failed'); }
  };

  const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(profile?.full_name || 'U')}&background=1E63FF&color=fff&size=200`;

  if (loading) return <LoadingSkeleton />;

  if (!profile) return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] px-4 space-y-5 text-center">
      <div className="w-16 h-16 bg-muted rounded-2xl flex items-center justify-center">
        <AlertCircle className="w-8 h-8 text-muted-foreground/30" />
      </div>
      <h3 className="text-xl font-extrabold text-foreground tracking-tight">Identity not found</h3>
      <p className="text-sm text-muted-foreground italic">The profile you are looking for does not exist in our network.</p>
      <Button variant="outline" onClick={() => navigate(-1)} className="rounded-xl h-11 px-6 font-bold border-border/60">
        <ArrowLeft className="w-4 h-4 mr-2" /> Back to Search
      </Button>
    </div>
  );

  const matchPct = profile.match_percentage ? Math.round(profile.match_percentage) : null;
  const tags = profile.tags?.length > 0 ? profile.tags : ['Searching', 'Verified', 'Roomshare'];
  const detailed = profile.detailed_compatibility || {};

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-24 space-y-6 sm:space-y-8">

      {/* ── Top Nav ──────────────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between pt-4 sm:pt-6">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => navigate(-1)}
          className="h-9 px-3 rounded-xl text-muted-foreground hover:text-primary gap-1.5 font-extrabold"
        >
          <ArrowLeft className="w-4 h-4" /> Back
        </Button>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="icon"
            className="h-9 w-9 rounded-xl text-muted-foreground hover:text-foreground border-border/40"
          >
            <Share2 className="w-4 h-4" />
          </Button>
          <Button
            variant="outline"
            size="icon"
            onClick={handleFavorite}
            className={cn(
              "h-9 w-9 rounded-xl transition-all border-border/40",
              favorited
                ? "text-rose-500 bg-rose-50 border-rose-100"
                : "text-muted-foreground hover:text-rose-500"
            )}
          >
            <Heart className={cn("w-4 h-4 transition-transform", favorited && "fill-rose-500 scale-110")} />
          </Button>
        </div>
      </div>

      {/* ── Hero Card ─────────────────────────────────────────────────────────── */}
      <div className="rounded-2xl overflow-hidden border border-border/50 shadow-lg shadow-primary/5 bg-card">
        {/* Cover strip */}
        <div
          className="h-24 sm:h-36 relative"
          style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 60%, #0a1628 100%)` }}
        >
          <div
            className="absolute inset-0 opacity-[0.07]"
            style={{
              backgroundImage: `repeating-linear-gradient(0deg,transparent,transparent 24px,#fff 24px,#fff 25px),
                                repeating-linear-gradient(90deg,transparent,transparent 24px,#fff 24px,#fff 25px)`
            }}
          />
          {matchPct && (
            <div className="absolute top-4 right-4 flex flex-col items-center bg-white/10 backdrop-blur-md border border-white/20 rounded-xl px-4 py-2 text-white">
              <span className="text-2xl sm:text-3xl font-black leading-none tabular-nums">{matchPct}%</span>
              <span className="text-[9px] font-extrabold uppercase tracking-[0.2em] text-blue-200 mt-1">Match Potential</span>
            </div>
          )}
        </div>

        {/* Avatar + identity */}
        <div className="px-5 sm:px-8 pb-6 sm:pb-8 -mt-12 sm:-mt-16 relative">
          <div className="flex flex-col sm:flex-row sm:items-end gap-5">
            <div className="relative shrink-0 self-start">
              <img
                src={profile.photo || avatarUrl}
                alt={profile.full_name}
                className="w-24 h-24 sm:w-32 sm:h-32 rounded-2xl object-cover border-4 border-card shadow-xl transition-transform hover:scale-105 duration-500"
              />
              <div className="absolute -bottom-1 -right-1 w-8 h-8 bg-emerald-500 rounded-xl border-[4px] border-card flex items-center justify-center shadow-lg">
                <CheckCircle2 className="w-4 h-4 text-white" />
              </div>
            </div>

            <div className="flex-1 min-w-0 sm:pb-2">
              <div className="flex flex-wrap items-center gap-3 mb-2">
                <h1 className="text-2xl sm:text-3xl font-extrabold text-foreground tracking-tight leading-tight">
                  {profile.full_name}{profile.age ? `, ${profile.age}` : ''}
                </h1>
                <Badge className="bg-emerald-50 text-emerald-600 border-none text-[9px] font-extrabold uppercase tracking-widest rounded-lg px-2.5 py-1">
                  Identity Verified
                </Badge>
                {profile.request_status && (
                  <Badge className="bg-amber-50 text-amber-600 border-none text-[9px] font-extrabold uppercase tracking-widest rounded-lg px-2.5 py-1">
                    Roomshare {profile.request_status}
                  </Badge>
                )}
              </div>

              <div className="flex flex-wrap items-center gap-x-4 gap-y-2 text-xs text-muted-foreground font-semibold">
                <span className="flex items-center gap-1.5">
                  <MapPin className="w-3.5 h-3.5 text-primary/60" />
                  {profile.city}
                </span>
                {profile.room_status && (
                  <div className="flex items-center gap-2">
                     <div className="w-1 h-1 rounded-full bg-border" />
                     <span className="text-primary font-extrabold uppercase tracking-widest text-[10px]">
                        {profile.room_status.replace('_', ' ')}
                     </span>
                  </div>
                )}
              </div>
            </div>
          </div>

          <div className="mt-6 sm:mt-8 grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Button
              onClick={handleRequestRoomshare}
              className="h-12 sm:h-14 rounded-2xl font-extrabold text-sm gap-2.5 shadow-xl shadow-primary/20 hover:shadow-primary/30 hover:-translate-y-0.5 transition-all"
            >
              <MessageCircle className="w-5 h-5" /> Request Roomshare
            </Button>
            <Button
              onClick={fetchAI}
              variant="outline"
              className="h-12 sm:h-14 rounded-2xl font-extrabold text-sm gap-2.5 border-primary/20 text-primary hover:bg-primary/5 hover:-translate-y-0.5 transition-all"
            >
              <Sparkles className="w-5 h-5" /> View AI Compatibility Report
            </Button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 sm:gap-8">
        <div className="lg:col-span-2 space-y-6 sm:space-y-8">
          {/* Narrative */}
          {profile.about_me && (
            <Card className="rounded-2xl p-6 sm:p-8 border-border/50 shadow-sm relative overflow-hidden">
               <div className="absolute top-0 right-0 w-32 h-32 bg-primary/3 rounded-full blur-3xl" />
              <SectionHeading icon={Info}>Identity Narrative</SectionHeading>
              <p className="text-sm sm:text-base text-muted-foreground font-medium leading-relaxed italic relative z-10">
                "{profile.about_me}"
              </p>
            </Card>
          )}

          {/* Lifestyle parameters */}
          <Card className="rounded-2xl p-6 sm:p-8 border-border/50 shadow-sm">
            <SectionHeading icon={Briefcase}>Core Parameters</SectionHeading>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <InfoPill icon={Briefcase}    label="Professional Status"   value={profile.occupation} />
              <InfoPill icon={DollarSign}   label="Budget Threshold"     value={profile.budget_range || profile.monthly_budget} />
              <InfoPill icon={Calendar}     label="Activation Date"      value={profile.move_in_date} />
              <InfoPill icon={Moon}         label="Circadian Rhythm"      value={profile.sleep_schedule} />
              <InfoPill icon={Droplets}     label="Sanitation Standard"  value={profile.cleanliness} />
              <InfoPill icon={Users}        label="Social Architecture"   value={profile.social_interaction} />
            </div>
          </Card>
        </div>

        <div className="space-y-6 sm:space-y-8">
          {/* Compatibility breakdowns */}
          <Card className="rounded-2xl p-6 sm:p-8 border-border/50 shadow-sm">
            <SectionHeading icon={TrendingUp}>Harmony Metrics</SectionHeading>
            <div className="space-y-5">
              <ScoreBar label="Schedule Alignment" value={detailed.sleep_score || 0} />
              <ScoreBar label="Hygiene Symmetry"   value={detailed.cleanliness_score || 0} />
              <ScoreBar label="Social Resonance"   value={detailed.social_score || 0} />
              <ScoreBar label="Budget Parity"      value={detailed.budget_score || 0} />
            </div>
          </Card>

          {/* Interests */}
          <div
            className="rounded-2xl p-6 sm:p-8 space-y-5"
            style={{ background: `linear-gradient(135deg, ${P.blueDark} 0%, #06111f 100%)` }}
          >
            <div className="flex items-center gap-2.5">
              <div className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center">
                <Zap className="w-4 h-4 text-blue-300" />
              </div>
              <h3 className="text-sm font-extrabold text-white uppercase tracking-widest">Interlock Interests</h3>
            </div>
            <div className="flex flex-wrap gap-2.5">
              {tags.map(tag => (
                <span
                  key={tag}
                  className="text-[10px] font-extrabold uppercase tracking-widest bg-white/5 hover:bg-white/10 border border-white/10 text-blue-100/70 rounded-lg px-3 py-1.5 cursor-default transition-all"
                >
                  {tag}
                </span>
              ))}
            </div>
          </div>

          {/* Trust Nudge */}
          <div className="rounded-2xl p-5 bg-emerald-500/[0.03] border border-emerald-500/10 flex items-start gap-4">
            <Shield className="w-6 h-6 text-emerald-500 shrink-0 mt-0.5" />
            <div>
              <p className="text-[10px] font-black text-emerald-600 uppercase tracking-[0.2em] mb-1">Security Verified</p>
              <p className="text-xs text-emerald-600/70 leading-relaxed font-semibold">
                This identity has completed background protocols and is verified by the RoomShare safety team.
              </p>
            </div>
          </div>
        </div>
      </div>

      <AIDialog open={showAI} onClose={() => setShowAI(false)} compatibility={compatibility} />
    </div>
  );
}