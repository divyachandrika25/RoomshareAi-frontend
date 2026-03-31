import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { roomAPI } from '@/lib/api';
import {
  ArrowLeft, CheckCircle2, Home, MapPin, Calendar,
  Sparkles, PartyPopper, ChevronRight, User, Star,
  MessageCircle, Shield, Gift, LayoutDashboard, Search,
  Phone, BadgeCheck, Clock, Building2
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import toast from 'react-hot-toast';

/* ─── Design tokens (matching the Kotlin AppTheme) ─── */
const T = {
  blue50:   '#EFF6FF',
  blue100:  '#DBEAFE',
  blue200:  '#BFDBFE',
  blue400:  '#60A5FA',
  blue500:  '#3B82F6',
  blue600:  '#2563EB',
  blue700:  '#1D4ED8',
  blue800:  '#1E40AF',
  blue900:  '#1E3A8A',
  indigo500:'#6366F1',
  indigo600:'#4F46E5',
  slate50:  '#F8FAFC',
  slate100: '#F1F5F9',
  slate200: '#E2E8F0',
  slate400: '#94A3B8',
  slate600: '#475569',
  slate700: '#334155',
  slate900: '#0F172A',
  success:  '#10B981',
  gold:     '#F59E0B',
  rose:     '#F43F5E',
};

const heroGradient = `linear-gradient(135deg, ${T.blue800} 0%, ${T.blue600} 55%, ${T.indigo500} 100%)`;
const ctaGradient  = `linear-gradient(135deg, ${T.blue700} 0%, ${T.blue600} 50%, ${T.indigo500} 100%)`;

export default function RoomShareFinalReviewPage() {
  const { requestId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [data,    setData]    = useState(null);

  useEffect(() => {
    if (!requestId) return;
    setLoading(true);
    roomAPI.getRoomShareFinalReview(requestId)
      .then(res => { if (res.data?.success) setData(res.data.data); })
      .catch(err => { console.error(err); toast.error("Failed to load request details"); })
      .finally(() => setLoading(false));
  }, [requestId]);

  if (loading) return <LoadingState />;

  const name       = user?.fullName || 'User';
  const title      = data?.title      || 'Luxury Apartment';
  const moveInDate = data?.move_in_date || 'Oct 15, 2026';
  const city       = data?.city         || 'Bangalore';
  const hostName   = data?.host_name    || 'Dinesh Karthik';
  const hostImage  = data?.host_image   || `https://ui-avatars.com/api/?name=${encodeURIComponent(hostName)}&background=2563EB&color=fff&size=200`;

  return (
    <div style={{ background: T.slate50, minHeight: '100vh' }}>

      {/* ── Top nav bar ── */}
      <TopBar onBack={() => navigate(-1)} />

      <div style={{ maxWidth: 1152, margin: '0 auto', padding: '0 16px 64px' }}>

        {/* ── Hero Banner ── */}
        <HeroBanner name={name} title={title} />

        {/* ── Stats strip ── */}
        <StatsStrip moveInDate={moveInDate} city={city} />

        {/* ── Main grid ── */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 340px), 1fr))',
          gap: 20,
          marginTop: 24,
        }}>
          {/* Left — host + celebration */}
          <HostCard
            hostName={hostName}
            hostImage={hostImage}
            title={title}
          />

          {/* Centre — approval details */}
          <ApprovalDetails moveInDate={moveInDate} city={city} />

          {/* Right — next steps */}
          <NextStepsCard
            onDashboard={() => navigate('/dashboard')}
            onFindMore={() => navigate('/dashboard/matches')}
          />
        </div>

        {/* ── Bottom CTA ── */}
        <BottomCTA
          onDashboard={() => navigate('/dashboard')}
          onFindMore={() => navigate('/dashboard/matches')}
        />
      </div>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800;900&display=swap');
        * { font-family: 'Plus Jakarta Sans', sans-serif; box-sizing: border-box; }
        @keyframes fadeUp   { from { opacity:0; transform:translateY(24px); } to { opacity:1; transform:translateY(0); } }
        @keyframes scalePop { from { opacity:0; transform:scale(0.88); } to { opacity:1; transform:scale(1); } }
        @keyframes pulse    { 0%,100%{transform:scale(1);} 50%{transform:scale(1.08);} }
        @keyframes bounce   { 0%,100%{transform:translateY(0);} 50%{transform:translateY(-6px);} }
        @keyframes shimmer  { 0%{background-position:-400px 0} 100%{background-position:400px 0} }
        .fade-up  { animation: fadeUp   0.6s ease both; }
        .scale-pop{ animation: scalePop 0.5s ease both; }
        .pulse-icon { animation: pulse 2.5s ease-in-out infinite; }
        .bounce-icon{ animation: bounce 1.8s ease-in-out infinite; }

        /* stagger helpers */
        .d1 { animation-delay: 0.05s }
        .d2 { animation-delay: 0.12s }
        .d3 { animation-delay: 0.20s }
        .d4 { animation-delay: 0.28s }
        .d5 { animation-delay: 0.36s }

        .card-hover { transition: transform 0.22s ease, box-shadow 0.22s ease; }
        .card-hover:hover { transform: translateY(-3px); box-shadow: 0 16px 40px rgba(30,64,175,0.13); }

        .btn-primary {
          background: ${ctaGradient};
          color: #fff; border: none; border-radius: 14px;
          font-size: 13px; font-weight: 800; letter-spacing: 0.04em;
          padding: 0 24px; height: 52px; cursor: pointer;
          transition: opacity .2s, transform .15s;
          display: inline-flex; align-items: center; justify-content: center; gap: 8px;
        }
        .btn-primary:hover  { opacity: 0.9; transform: translateY(-1px); }
        .btn-primary:active { transform: translateY(0); }

        .btn-outline {
          background: #fff; color: ${T.blue700};
          border: 1.5px solid ${T.blue200}; border-radius: 14px;
          font-size: 13px; font-weight: 800; letter-spacing: 0.04em;
          padding: 0 24px; height: 52px; cursor: pointer;
          transition: background .2s, transform .15s;
          display: inline-flex; align-items: center; justify-content: center; gap: 8px;
        }
        .btn-outline:hover  { background: ${T.blue50}; transform: translateY(-1px); }

        @media (max-width: 640px) {
          .hero-title { font-size: 24px !important; }
          .stats-grid { grid-template-columns: 1fr 1fr !important; }
          .bottom-cta-row { flex-direction: column !important; }
          .bottom-cta-row button { width: 100% !important; }
        }
      `}</style>
    </div>
  );
}

/* ─── TOP BAR ─── */
function TopBar({ onBack }) {
  return (
    <div style={{
      background: '#fff',
      borderBottom: `1px solid ${T.slate100}`,
      position: 'sticky', top: 0, zIndex: 50,
    }}>
      <div style={{ maxWidth: 1152, margin: '0 auto', padding: '0 16px', height: 64,
                    display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <button onClick={onBack} style={{
          display:'flex', alignItems:'center', gap: 8,
          background: T.blue50, border: 'none', borderRadius: 12,
          padding: '8px 14px', cursor: 'pointer',
          color: T.blue700, fontWeight: 700, fontSize: 13,
        }}>
          <ArrowLeft size={16} />
          Back
        </button>
        {/* Logo pill */}
        <div style={{
          display: 'flex', alignItems: 'center', gap: 8,
          background: T.blue50, borderRadius: 20, padding: '6px 14px',
        }}>
          <div style={{
            width: 22, height: 22, borderRadius: '50%',
            background: heroGradient,
          }} />
          <span style={{ fontSize: 13, fontWeight: 800, color: T.blue800 }}>RoomShare AI</span>
        </div>
        <div style={{ width: 80 }} /> {/* balance */}
      </div>
    </div>
  );
}

/* ─── HERO BANNER ─── */
function HeroBanner({ name, title }) {
  return (
    <div className="scale-pop" style={{
      background: heroGradient,
      borderRadius: '0 0 28px 28px',
      padding: '52px 24px 80px',
      textAlign: 'center',
      position: 'relative',
      overflow: 'hidden',
      marginBottom: -52,
    }}>
      {/* Decorative circles */}
      <div style={{ position:'absolute', width:260, height:260, borderRadius:'50%',
        background:'rgba(255,255,255,0.05)', top:-80, left:-80 }} />
      <div style={{ position:'absolute', width:180, height:180, borderRadius:'50%',
        background:'rgba(255,255,255,0.05)', top:-40, right:-50 }} />
      <div style={{ position:'absolute', width:120, height:120, borderRadius:'50%',
        background:'rgba(255,255,255,0.04)', bottom:-30, right:'30%' }} />

      {/* Success icon */}
      <div className="pulse-icon" style={{ position:'relative', display:'inline-block', marginBottom: 20 }}>
        <div style={{
          width: 80, height: 80, borderRadius: 24,
          background: 'rgba(255,255,255,0.18)',
          border: '2px solid rgba(255,255,255,0.3)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          margin: '0 auto',
        }}>
          <CheckCircle2 size={38} color="#fff" />
        </div>
        <div className="bounce-icon" style={{ position:'absolute', top:-8, right:-8 }}>
          <Sparkles size={20} color={T.gold} />
        </div>
        <div style={{ position:'absolute', bottom:-8, left:-8, animation:'pulse 2s ease infinite' }}>
          <PartyPopper size={20} color="#A5B4FC" />
        </div>
      </div>

      <h1 className="hero-title" style={{
        fontSize: 30, fontWeight: 900, color: '#fff',
        letterSpacing: '-0.5px', lineHeight: 1.2, marginBottom: 8,
      }}>
        Congratulations, {name}! 🎉
      </h1>
      <p style={{ fontSize: 13, color: 'rgba(255,255,255,0.8)', fontWeight: 600, maxWidth: 440, margin: '0 auto' }}>
        Your room share application for <strong style={{ color: '#fff' }}>{title}</strong> has been officially approved.
      </p>

      {/* Verified pill */}
      <div style={{
        display: 'inline-flex', alignItems: 'center', gap: 6,
        background: 'rgba(255,255,255,0.16)', border: '1px solid rgba(255,255,255,0.25)',
        borderRadius: 20, padding: '6px 14px', marginTop: 18,
      }}>
        <BadgeCheck size={13} color="#fff" />
        <span style={{ fontSize: 11, fontWeight: 800, color: '#fff', letterSpacing: '0.06em' }}>
          APPLICATION APPROVED
        </span>
      </div>
    </div>
  );
}

/* ─── STATS STRIP ─── */
function StatsStrip({ moveInDate, city }) {
  const stats = [
    { icon: <CheckCircle2 size={18} color={T.success} />,   bg: '#ECFDF5', label: 'Status',      value: 'Approved',   color: T.success },
    { icon: <Calendar     size={18} color={T.blue600} />,   bg: T.blue50,  label: 'Move-in',     value: moveInDate,   color: T.blue700 },
    { icon: <MapPin       size={18} color={T.gold} />,      bg: '#FFFBEB', label: 'City',        value: city,         color: '#92400E' },
    { icon: <Shield       size={18} color={T.indigo500} />, bg: '#EEF2FF', label: 'Verification',value: 'Complete',   color: T.indigo600 },
  ];

  return (
    <div className="d3 fade-up" style={{
      display: 'grid',
      gridTemplateColumns: 'repeat(4, 1fr)',
      gap: 12,
      background: '#fff',
      border: `1px solid ${T.slate200}`,
      borderRadius: 20,
      padding: 16,
      boxShadow: '0 4px 24px rgba(30,64,175,0.08)',
      position: 'relative',
      zIndex: 10,
      maxWidth: 900,
      margin: '0 auto',
    }} className="stats-grid d3 fade-up">
      {stats.map((s, i) => (
        <div key={i} style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center',
          gap: 6, padding: '8px 4px',
        }}>
          <div style={{
            width: 40, height: 40, borderRadius: 12,
            background: s.bg, display: 'flex', alignItems: 'center', justifyContent: 'center',
          }}>
            {s.icon}
          </div>
          <span style={{ fontSize: 9, fontWeight: 800, color: T.slate400, textTransform: 'uppercase', letterSpacing: '0.08em' }}>
            {s.label}
          </span>
          <span style={{ fontSize: 12, fontWeight: 800, color: s.color, textAlign: 'center' }}>
            {s.value}
          </span>
        </div>
      ))}
    </div>
  );
}

/* ─── HOST CARD ─── */
function HostCard({ hostName, hostImage, title }) {
  return (
    <div className="card-hover d3 fade-up" style={{
      background: '#fff',
      borderRadius: 24,
      boxShadow: '0 4px 24px rgba(30,64,175,0.08)',
      overflow: 'hidden',
    }}>
      {/* Card header */}
      <div style={{ background: heroGradient, padding: '20px 20px 36px', position: 'relative', overflow: 'hidden' }}>
        <div style={{ position:'absolute', width:120, height:120, borderRadius:'50%',
          background:'rgba(255,255,255,0.06)', top:-30, right:-30 }} />
        <SectionLabel icon={<User size={14} color="#fff" />} label="Meet Your Host" light />
      </div>

      <div style={{ padding: 20, marginTop: -28 }}>
        {/* Host profile */}
        <div style={{
          display: 'flex', alignItems: 'center', gap: 14,
          background: '#fff',
          border: `1px solid ${T.slate200}`,
          borderRadius: 18, padding: 14,
          boxShadow: '0 2px 12px rgba(30,64,175,0.06)',
          marginBottom: 16,
        }}>
          <div style={{ position: 'relative', flexShrink: 0 }}>
            <img
              src={hostImage}
              alt={hostName}
              style={{ width: 52, height: 52, borderRadius: 16, objectFit: 'cover',
                       border: `2.5px solid ${T.blue200}` }}
            />
            <div style={{
              position: 'absolute', bottom: 0, right: 0,
              width: 14, height: 14, background: T.success,
              borderRadius: '50%', border: '2px solid #fff',
            }} />
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <p style={{ fontSize: 14, fontWeight: 800, color: T.slate900, marginBottom: 3 }}>{hostName}</p>
            <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              <Star size={11} color={T.gold} fill={T.gold} />
              <span style={{ fontSize: 11, fontWeight: 700, color: T.slate600 }}>Highly Rated Host</span>
            </div>
          </div>
          <button className="btn-outline" style={{ height: 36, padding: '0 14px', fontSize: 11, borderRadius: 10 }}>
            Profile
          </button>
        </div>

        {/* Property info */}
        <div style={{
          background: T.blue50, border: `1px solid ${T.blue100}`,
          borderRadius: 14, padding: 14, marginBottom: 16,
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{ width: 30, height: 30, background: T.blue600, borderRadius: 9,
                          display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Building2 size={14} color="#fff" />
            </div>
            <div>
              <p style={{ fontSize: 9, fontWeight: 800, color: T.slate400, textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                PROPERTY
              </p>
              <p style={{ fontSize: 13, fontWeight: 800, color: T.slate900 }}>{title}</p>
            </div>
          </div>
        </div>

        {/* Celebration note */}
        <div style={{
          background: 'linear-gradient(135deg, #ECFDF5 0%, #D1FAE5 100%)',
          border: '1px solid #A7F3D0',
          borderRadius: 16, padding: 16,
          position: 'relative', overflow: 'hidden',
        }}>
          <div style={{ position: 'absolute', top: -20, right: -20, opacity: 0.08 }}>
            <PartyPopper size={80} color="#059669" />
          </div>
          <div style={{ display: 'flex', gap: 10, position: 'relative' }}>
            <div style={{ width: 32, height: 32, background: '#D1FAE5', borderRadius: 10, flexShrink: 0,
                          display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Sparkles size={15} color="#059669" />
            </div>
            <div>
              <p style={{ fontSize: 13, fontWeight: 800, color: '#065F46', marginBottom: 3 }}>
                Your New Journey Awaits!
              </p>
              <p style={{ fontSize: 11, fontWeight: 600, color: '#047857', lineHeight: 1.5 }}>
                Home verification complete. Chat with your host and start preparing for your move.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ─── APPROVAL DETAILS ─── */
function ApprovalDetails({ moveInDate, city }) {
  const details = [
    { icon: <Calendar size={16} color={T.blue600} />, bg: T.blue50, label: 'MOVE-IN DATE',  value: moveInDate,     border: T.blue100 },
    { icon: <MapPin   size={16} color={T.gold} />,    bg: '#FFFBEB', label: 'CITY',         value: city,           border: '#FDE68A' },
    { icon: <Clock    size={16} color={T.indigo500}/>, bg:'#EEF2FF', label: 'LEASE TERM',   value: '12 Months',    border: '#C7D2FE' },
    { icon: <Shield   size={16} color={T.success} />, bg: '#ECFDF5', label: 'VERIFICATION', value: 'Complete ✓',   border: '#A7F3D0' },
  ];

  return (
    <div className="card-hover d4 fade-up" style={{
      background: '#fff', borderRadius: 24,
      boxShadow: '0 4px 24px rgba(30,64,175,0.08)',
      overflow: 'hidden',
    }}>
      <div style={{ background: `linear-gradient(135deg, ${T.indigo600}, ${T.blue600})`, padding: '20px 20px 36px', position: 'relative', overflow: 'hidden' }}>
        <div style={{ position:'absolute', width:100, height:100, borderRadius:'50%',
          background:'rgba(255,255,255,0.06)', bottom:-30, left:-20 }} />
        <SectionLabel icon={<CheckCircle2 size={14} color="#fff" />} label="Approval Details" light />
      </div>

      <div style={{ padding: 20, marginTop: -28 }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10, marginBottom: 16 }}>
          {details.map((d, i) => (
            <div key={i} style={{
              background: d.bg, border: `1px solid ${d.border}`,
              borderRadius: 14, padding: 14,
              display: 'flex', flexDirection: 'column', gap: 6,
            }}>
              <div style={{ width: 32, height: 32, background: '#fff', borderRadius: 9, border: `1px solid ${d.border}`,
                            display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                {d.icon}
              </div>
              <span style={{ fontSize: 8, fontWeight: 900, color: T.slate400, textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                {d.label}
              </span>
              <span style={{ fontSize: 12, fontWeight: 800, color: T.slate900 }}>{d.value}</span>
            </div>
          ))}
        </div>

        {/* Checklist */}
        <div style={{ border: `1px solid ${T.slate100}`, borderRadius: 16, overflow: 'hidden' }}>
          <div style={{ padding: '12px 16px', background: T.slate50, borderBottom: `1px solid ${T.slate100}` }}>
            <p style={{ fontSize: 10, fontWeight: 800, color: T.slate400, textTransform: 'uppercase', letterSpacing: '0.08em', margin: 0 }}>
              Completion Checklist
            </p>
          </div>
          {[
            { label: 'Application submitted', done: true },
            { label: 'Identity verified',     done: true },
            { label: 'Host approved',         done: true },
            { label: 'Agreement signed',      done: true },
            { label: 'Move-in confirmed',     done: true },
          ].map((item, i) => (
            <div key={i} style={{
              display: 'flex', alignItems: 'center', gap: 10,
              padding: '11px 16px',
              borderBottom: i < 4 ? `1px solid ${T.slate100}` : 'none',
            }}>
              <div style={{
                width: 20, height: 20, borderRadius: 6,
                background: item.done ? T.success : T.slate100,
                display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
              }}>
                {item.done && <CheckCircle2 size={12} color="#fff" />}
              </div>
              <span style={{ fontSize: 12, fontWeight: 600, color: item.done ? T.slate900 : T.slate400 }}>
                {item.label}
              </span>
              {item.done && (
                <span style={{ marginLeft: 'auto', fontSize: 9, fontWeight: 800,
                               color: T.success, textTransform: 'uppercase', letterSpacing: '0.06em' }}>
                  Done
                </span>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

/* ─── NEXT STEPS CARD ─── */
function NextStepsCard({ onDashboard, onFindMore }) {
  const steps = [
    { icon: <MessageCircle size={16} color={T.blue600} />, bg: T.blue50, label: 'Chat with Host', sub: 'Coordinate move-in details', border: T.blue100 },
    { icon: <Home          size={16} color={T.indigo500}/>, bg:'#EEF2FF', label: 'Inspect the Room', sub: 'Schedule a walkthrough',  border:'#C7D2FE' },
    { icon: <Calendar      size={16} color={T.success} />, bg:'#ECFDF5', label: 'Confirm Move Date', sub: 'Lock in your timeline',   border:'#A7F3D0' },
  ];

  return (
    <div className="card-hover d5 fade-up" style={{
      background: '#fff', borderRadius: 24,
      boxShadow: '0 4px 24px rgba(30,64,175,0.08)',
      overflow: 'hidden',
    }}>
      <div style={{ background: `linear-gradient(135deg, ${T.blue600}, ${T.indigo500})`, padding: '20px 20px 36px', position: 'relative', overflow:'hidden' }}>
        <div style={{ position:'absolute', width:110, height:110, borderRadius:'50%',
          background:'rgba(255,255,255,0.06)', top:-30, left:-30 }} />
        <SectionLabel icon={<ChevronRight size={14} color="#fff" />} label="Next Steps" light />
      </div>

      <div style={{ padding: 20, marginTop: -28 }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 10, marginBottom: 16 }}>
          {steps.map((s, i) => (
            <div key={i} style={{
              display: 'flex', alignItems: 'center', gap: 12,
              background: s.bg, border: `1px solid ${s.border}`,
              borderRadius: 14, padding: '12px 14px',
            }}>
              <div style={{ width: 36, height: 36, background: '#fff', border: `1px solid ${s.border}`,
                            borderRadius: 11, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                {s.icon}
              </div>
              <div style={{ flex: 1, minWidth: 0 }}>
                <p style={{ fontSize: 13, fontWeight: 800, color: T.slate900, margin: 0 }}>{s.label}</p>
                <p style={{ fontSize: 11, color: T.slate400, fontWeight: 600, margin: 0 }}>{s.sub}</p>
              </div>
              <ChevronRight size={15} color={T.slate400} />
            </div>
          ))}
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          <button className="btn-primary" style={{ width: '100%' }} onClick={onDashboard}>
            <LayoutDashboard size={16} />
            Dashboard
          </button>
          <button className="btn-outline" style={{ width: '100%' }} onClick={onFindMore}>
            <Search size={16} />
            Find More Rooms
          </button>
        </div>

        {/* Referral hint */}
        <div style={{
          marginTop: 14, background: '#FFF7ED', border: '1px solid #FED7AA',
          borderRadius: 14, padding: '12px 14px',
          display: 'flex', alignItems: 'flex-start', gap: 10,
        }}>
          <div style={{ width: 28, height: 28, background: '#FFEDD5', borderRadius: 8,
                        display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
            <Gift size={13} color={T.gold} />
          </div>
          <p style={{ fontSize: 11, color: '#92400E', fontWeight: 600, lineHeight: 1.5, margin: 0 }}>
            Invite friends and earn <strong>₹500 referral bonus</strong> for every successful sign-up.
          </p>
        </div>

        {/* Support */}
        <div style={{
          marginTop: 12, border: `1px solid ${T.blue100}`,
          borderRadius: 14, padding: '12px 14px',
          display: 'flex', alignItems: 'center', gap: 10,
          cursor: 'pointer',
        }}>
          <div style={{ width: 32, height: 32, background: T.blue50, borderRadius: 10,
                        display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
            <Phone size={14} color={T.blue600} />
          </div>
          <div style={{ flex: 1 }}>
            <p style={{ fontSize: 12, fontWeight: 800, color: T.slate900, margin: 0 }}>Need Help?</p>
            <p style={{ fontSize: 11, color: T.blue600, fontWeight: 700, margin: 0 }}>Contact Support →</p>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ─── BOTTOM CTA ─── */
function BottomCTA({ onDashboard, onFindMore }) {
  return (
    <div className="d5 fade-up" style={{
      marginTop: 24,
      background: heroGradient,
      borderRadius: 24,
      padding: '32px 24px',
      position: 'relative', overflow: 'hidden',
    }}>
      <div style={{ position:'absolute', width:200, height:200, borderRadius:'50%',
        background:'rgba(255,255,255,0.05)', top:-60, right:-60 }} />
      <div style={{ position:'absolute', width:140, height:140, borderRadius:'50%',
        background:'rgba(255,255,255,0.04)', bottom:-40, left:40 }} />

      <div style={{
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        flexWrap: 'wrap', gap: 20, position: 'relative',
      }} className="bottom-cta-row">
        <div>
          <p style={{ fontSize: 20, fontWeight: 900, color: '#fff', marginBottom: 6, letterSpacing: '-0.3px' }}>
            Ready to start your new chapter?
          </p>
          <p style={{ fontSize: 13, color: 'rgba(255,255,255,0.75)', fontWeight: 600, margin: 0 }}>
            Everything is set — your approved home is waiting.
          </p>
        </div>
        <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
          <button onClick={onDashboard} style={{
            background: '#fff', color: T.blue700, border: 'none',
            borderRadius: 14, padding: '0 24px', height: 52,
            fontWeight: 800, fontSize: 13, cursor: 'pointer',
            display: 'flex', alignItems: 'center', gap: 8,
            transition: 'transform .15s',
          }}
            onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-2px)'}
            onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0)'}
          >
            <LayoutDashboard size={16} />
            Go to Dashboard
          </button>
          <button onClick={onFindMore} style={{
            background: 'rgba(255,255,255,0.16)', color: '#fff',
            border: '1.5px solid rgba(255,255,255,0.28)',
            borderRadius: 14, padding: '0 24px', height: 52,
            fontWeight: 800, fontSize: 13, cursor: 'pointer',
            display: 'flex', alignItems: 'center', gap: 8,
            transition: 'transform .15s',
          }}
            onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-2px)'}
            onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0)'}
          >
            <Search size={16} />
            Explore More Rooms
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── SECTION LABEL ─── */
function SectionLabel({ icon, label, light }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
      <div style={{
        width: 28, height: 28, borderRadius: 8,
        background: light ? 'rgba(255,255,255,0.18)' : T.blue50,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        {icon}
      </div>
      <span style={{
        fontSize: 12, fontWeight: 800, letterSpacing: '0.06em',
        textTransform: 'uppercase',
        color: light ? 'rgba(255,255,255,0.9)' : T.blue700,
      }}>
        {label}
      </span>
    </div>
  );
}

/* ─── LOADING STATE ─── */
function LoadingState() {
  return (
    <div style={{ minHeight: '100vh', background: T.slate50 }}>
      <style>{`
        @keyframes shimmer { 0%{background-position:-800px 0} 100%{background-position:800px 0} }
        .shimmer { background: linear-gradient(90deg, #F1F5F9 25%, #E2E8F0 50%, #F1F5F9 75%);
                   background-size: 800px 100%; animation: shimmer 1.5s infinite; }
      `}</style>
      {/* Top bar */}
      <div style={{ background: '#fff', height: 64, borderBottom: `1px solid ${T.slate100}` }} />
      {/* Hero */}
      <div className="shimmer" style={{ height: 260, borderRadius: '0 0 28px 28px' }} />
      <div style={{ maxWidth: 1152, margin: '0 auto', padding: '20px 16px', display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 20 }}>
        {[1,2,3].map(i => (
          <div key={i} className="shimmer" style={{ height: 420, borderRadius: 24 }} />
        ))}
      </div>
    </div>
  );
}